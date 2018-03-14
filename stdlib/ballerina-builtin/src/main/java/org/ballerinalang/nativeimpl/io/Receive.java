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
package org.ballerinalang.nativeimpl.io;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.workflow.Correlator;

@BallerinaFunction(
        packageName = "ballerina.io",
        functionName = "nreceive",
        args = {@Argument(name = "messageName", type = TypeKind.STRING),
                @Argument(name = "vars", type = TypeKind.MAP)},
        returnType = {@ReturnType(type = TypeKind.STRING)},
        isPublic = true
)
public class Receive extends AbstractNativeFunction {

    @Override
    public BValue[] execute(Context context) {
        String name = getStringArgument(context, 0);
        BMap<String, ?> vars = (BMap<String, ?>) getRefArgument(context, 0);
        String msg = "Waiting for message: " + name;
        System.out.println(msg);
        String result = msg;

        Correlator.persistState(vars, context);
        context.setProperty("new_ip", -1);

        return getBValues(new BString(result));
    }
}
