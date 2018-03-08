/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.util.workflow;

import org.ballerinalang.bre.bvm.ControlStack;
import org.ballerinalang.model.util.JsonNode;
import org.ballerinalang.model.values.BRefType;
import org.ballerinalang.model.values.WorkflowInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility methods for workflow related constructs.
 */
public class WorkflowUtils {

    private static ConcurrentHashMap<Long, Map> instanceMap =  new ConcurrentHashMap<>();


    public static void persistStack(ControlStack stack, String messageName, BRefType<JsonNode> correlationMap, int ip) {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setControlStack(stack);
        instance.setCorrelationMap(correlationMap.value());
        instance.setIp(ip);

        long instanceId = System.currentTimeMillis(); //todo : this is temporary, won't work with multiple receives
        if (!instanceMap.contains(instanceId)) {
            ConcurrentHashMap<String, WorkflowInstance> messageMap = new ConcurrentHashMap<>();
            messageMap.put(messageName, instance);
            instanceMap.put(instanceId, messageMap);
        } else {
            instanceMap.get(instanceId).put(messageName, instance);
        }

    }
}
