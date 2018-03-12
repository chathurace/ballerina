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
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;

import java.util.HashMap;
import java.util.Map;

public class ContextStore {

    private static Map<String, Context> store = new HashMap<>();

    public static void storeContext(BMap<String, ?> vars, Context context) {
        String varName = "";
        String varValue = "";
        for (String n : vars.keySet()) {
            varName = n;
            BString bString = (BString) vars.get(n);
            varValue = bString.value();
        }
        String correlationKey = varName + "=" + varValue;
        store.put(correlationKey, context);
    }

    public static Context getContext(String var, boolean delete) {
        Context context = store.get(var);
        if (context != null) {
            context.getProperties().remove("new_ip");
        }
        if (delete) {
            store.remove(var);
        }
        return context;
    }

    public static Context getContext(BMap<String, ?> vars, boolean delete) {
        String varName = "";
        String varValue = "";
        for (String n : vars.keySet()) {
            varName = n;
            BString bString = (BString) vars.get(n);
            varValue = bString.value();
        }
        String correlationKey = varName + "=" + varValue;
        Context context = store.get(correlationKey);
        if (delete) {
            store.remove(correlationKey);
        }
        return context;
    }

}
