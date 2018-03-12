/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.workflow;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BLangVM;
import org.ballerinalang.bre.bvm.ControlStack;
import org.ballerinalang.bre.bvm.StackFrame;
import org.ballerinalang.connector.api.Resource;
import org.ballerinalang.connector.impl.BServerConnectorFuture;
import org.ballerinalang.connector.impl.BWorkflow;
import org.ballerinalang.model.types.BType;
import org.ballerinalang.model.types.BTypes;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BJSON;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.util.BLangConstants;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.codegen.WorkerInfo;
import org.ballerinalang.util.codegen.WorkflowInfo;
import org.ballerinalang.util.codegen.attributes.CodeAttributeInfo;
import org.ballerinalang.util.debugger.Debugger;
import org.ballerinalang.util.debugger.DebuggerUtil;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.wso2.transport.http.netty.message.HttpCarbonRequest;

import java.util.Arrays;
import java.util.Map;

public class WorkflowExecutor {

    public static void execute(BWorkflow workflow, BServerConnectorFuture connectorFuture,
                               Map<String, Object> properties, BValue... bValues) {
        if (workflow == null) {
            connectorFuture.notifyFailure(new BallerinaException("trying to execute a null resource"));
            return;
        }
        WorkflowInfo workflowInfo = workflow.getWorkflowInfo();
        // Invoke VM.
        PackageInfo packageInfo = workflowInfo.getPackageInfo();
        ProgramFile programFile = packageInfo.getProgramFile();

        Context context = new Context(programFile);
        context.setWorkflowInfo(workflowInfo);
        context.setConnectorFuture(connectorFuture);

        //TODO remove this with a proper way
        if (properties != null) {
            properties.forEach(context::setProperty);
        }

        ControlStack controlStack = context.getControlStack();

        // Now create callee's stack-frame
        WorkerInfo defaultWorkerInfo = workflowInfo.getDefaultWorkerInfo();
        StackFrame calleeSF = new StackFrame(workflowInfo, defaultWorkerInfo, -1, new int[0]);
        controlStack.pushFrame(calleeSF);

        CodeAttributeInfo codeAttribInfo = defaultWorkerInfo.getCodeAttributeInfo();
        context.setStartIP(codeAttribInfo.getCodeAddrs());

        String[] stringReg = new String[codeAttribInfo.getMaxStringRegs()];
        Arrays.fill(stringReg, BLangConstants.STRING_NULL_VALUE);
        int[] intRegs = new int[codeAttribInfo.getMaxIntRegs()];
        long[] longRegs = new long[codeAttribInfo.getMaxLongRegs()];
        double[] doubleRegs = new double[codeAttribInfo.getMaxDoubleRegs()];
        BRefType[] refRegs = new BRefType[codeAttribInfo.getMaxRefRegs()];

        int stringParamCount = 0;
        int intParamCount = 0;
        int doubleParamCount = 0;
        int longParamCount = 0;
        int refParamCount = 0;
        BType[] bTypes = workflowInfo.getParamTypes();

        if (bValues != null) {
            for (int i = 0; i < bValues.length; i++) {
                BType btype = bTypes[i];
                BValue value = bValues[i];

                // Set default values
                if (value == null) {
                    continue;
                }

                if (btype == BTypes.typeString) {
                    stringReg[stringParamCount++] = value.stringValue();
                } else if (btype == BTypes.typeBoolean) {
                    if ("true".equalsIgnoreCase(value.stringValue())) {
                        intRegs[intParamCount++] = 1;
                    } else if ("false".equalsIgnoreCase(value.stringValue())) {
                        intRegs[intParamCount++] = 0;
                    } else {
                        throw new BallerinaException("Unsupported parameter type for parameter " + value);
                    }
                } else if (btype == BTypes.typeFloat) {
                    doubleRegs[doubleParamCount++] = ((BFloat) value).floatValue();
                } else if (btype == BTypes.typeInt) {
                    longRegs[longParamCount++] = ((BInteger) value).intValue();
                } else if (value instanceof BStruct) {
                    refRegs[refParamCount++] = (BRefType) value;
                } else if (value instanceof BRefValueArray) {
                    refRegs[refParamCount++] = (BRefType) value;
                } else if (value instanceof BJSON) {
                    refRegs[refParamCount++] = (BRefType) value;
                } else {
                    connectorFuture.notifyFailure(new BallerinaException("unsupported " +
                            "parameter type for parameter " + value));
                }
            }
        }

        // It is given that first parameter of the resource is carbon message.
        calleeSF.setLongRegs(longRegs);
        calleeSF.setDoubleRegs(doubleRegs);
        calleeSF.setStringRegs(stringReg);
        calleeSF.setIntRegs(intRegs);
        calleeSF.setRefRegs(refRegs);

        // Execute workers
        // Pass the incoming message variable into the worker invocations
        // Fix #2623
        StackFrame callerSF = new StackFrame(workflowInfo, defaultWorkerInfo, -1, new int[0]);
        callerSF.setRefRegs(new BRefType[1]);
        callerSF.getRefRegs()[0] = refRegs[0];

        BLangVM bLangVM = new BLangVM(packageInfo.getProgramFile());
        context.setAsResourceContext();
        context.startTrackWorker();
        Debugger debugger = programFile.getDebugger();
        if (debugger.isDebugEnabled()) {
            DebuggerUtil.initDebugContext(context, debugger);
        }
        bLangVM.run(context);

        connectorFuture.notifySuccess();
    }

    public static boolean execute(Resource resource, BServerConnectorFuture connectorFuture,
                                  Map<String, Object> properties, BValue... values) {
        if (resource == null) {
            connectorFuture.notifyFailure(new BallerinaException("trying to execute a null resource"));
            return false;
        }

//         ((HttpCarbonRequest)((HashMap.Node)((HashMap)((BStruct)values[0]).nativeData).entrySet().toArray()[1]).getValue()).httpRequest.headers().entries().get(3).getValue()

        HttpCarbonRequest request = (HttpCarbonRequest) ((BStruct) values[0]).getNativeData("transport_message");
        String correlationHeader = request.getHeader("Correlation");
        System.out.println("Correlating with header: " + correlationHeader);

        Context context = new Correlator().correlate(correlationHeader, true);
        if (context != null) {
            context.setStartIP((Integer) context.getProperty("ip"));
            BLangVM bLangVM = new BLangVM(context.getProgramFile());
            context.setAsResourceContext();
            context.startTrackWorker();
            Debugger debugger = context.getProgramFile().getDebugger();
            if (debugger.isDebugEnabled()) {
                DebuggerUtil.initDebugContext(context, debugger);
            }
            bLangVM.run(context);
            return true;
        }
        return false;
    }
}
