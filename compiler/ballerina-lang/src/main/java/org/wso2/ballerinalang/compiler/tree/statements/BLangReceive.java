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
package org.wso2.ballerinalang.compiler.tree.statements;

import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.statements.ReceiveNode;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;
import org.wso2.ballerinalang.compiler.semantics.model.types.BType;
import org.wso2.ballerinalang.compiler.tree.BLangIdentifier;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangVariableReference;
import org.wso2.ballerinalang.compiler.util.TypeTags;
import org.wso2.ballerinalang.programfile.Instruction.RegIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Receive construct for workflow support.
 */
public class BLangReceive extends BLangVariableReference implements ReceiveNode {
    public BLangExpression messageName;
    public BLangExpression correlationMap;
    public BLangIdentifier pkgAlias;
    public BLangIdentifier name;
    public List<BLangExpression> argExprs = new ArrayList<>();
    public BLangVariableReference expr;
    public List<BType> types = new ArrayList<>(0);
    public BSymbol symbol;
    protected RegIndex[] regIndexes;
    public int returnType = TypeTags.JSON;

    public BLangReceive() {}

    public BLangReceive(BLangExpression messageName, BLangExpression correlationMap) {
        this.messageName = messageName;
        this.correlationMap = correlationMap;
    }

    @Override
    public BLangExpression getMessageName() {
        return messageName;
    }

    @Override
    public BLangExpression getCorrelationMap() {
        return correlationMap;
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.RECEIVE;
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    public RegIndex[] getRegIndexes() {
        return regIndexes;
    }

    public void setRegIndexes(RegIndex[] regIndexes) {
        this.regIndexes = regIndexes;
        this.regIndex = regIndexes != null && regIndexes.length > 0 ? regIndexes[0] : null;
    }
}
