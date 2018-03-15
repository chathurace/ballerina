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

/**
 * Represents the state of a Ballerina workflow.
 */
public class WorkflowState {

    private int ip;
    private Context context;
    private int retRegIndex;

    public WorkflowState(Context context, int ip, int retRegIndex) {
        this.context = context;
        this.ip = ip;
        this.retRegIndex = retRegIndex;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getRetRegIndex() {
        return retRegIndex;
    }

    public void setRetRegIndex(int retRegIndex) {
        this.retRegIndex = retRegIndex;
    }
}
