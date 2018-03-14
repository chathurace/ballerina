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
package org.ballerinalang.runtime.threadpool;

import org.ballerinalang.bre.bvm.BLangVM;
import org.ballerinalang.connector.impl.BServerConnectorFuture;
import org.ballerinalang.connector.impl.BWorkflow;
import org.ballerinalang.model.util.StringUtils;
import org.ballerinalang.model.values.BJSON;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.workflow.WorkflowExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.transport.http.netty.message.HTTPCarbonMessage;
import org.wso2.transport.http.netty.message.HttpMessageDataStreamer;

import java.util.Map;

public class BallerinaWorkflowThread implements Runnable {

    private BServerConnectorFuture connectorFuture;
    private BWorkflow workflow;
    private BValue[] bValues;
    private Map<String, Object> properties;
    private HTTPCarbonMessage httpCarbonMessage;

    public BallerinaWorkflowThread(BWorkflow workflow, BServerConnectorFuture connectorFuture,
                                   Map<String, Object> properties, BValue[] bValues, HTTPCarbonMessage httpCarbonMessage) {
        this.workflow = workflow;
        this.connectorFuture = connectorFuture;
        this.properties = properties;
        this.bValues = bValues;
        this.httpCarbonMessage = httpCarbonMessage;
    }

    @Override
    public void run() {
        WorkflowExecutor.execute(workflow, connectorFuture, properties, httpCarbonMessage);
    }
}
