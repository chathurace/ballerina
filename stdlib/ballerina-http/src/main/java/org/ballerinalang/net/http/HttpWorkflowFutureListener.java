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
package org.ballerinalang.net.http;

import org.ballerinalang.connector.api.BallerinaConnectorException;
import org.ballerinalang.connector.api.ConnectorFutureListener;
import org.ballerinalang.model.values.BValue;
import org.wso2.transport.http.netty.message.HTTPCarbonMessage;

public class HttpWorkflowFutureListener implements ConnectorFutureListener {

    private HTTPCarbonMessage requestMessage;

    public HttpWorkflowFutureListener(HTTPCarbonMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    public void notifySuccess() {
        HttpUtil.sendAccepted(requestMessage);
    }

    @Override
    public void notifyReply(BValue... response) {

    }

    @Override
    public void notifyFailure(BallerinaConnectorException ex) {

    }
}
