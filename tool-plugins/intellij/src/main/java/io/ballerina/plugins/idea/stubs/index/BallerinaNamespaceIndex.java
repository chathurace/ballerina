/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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

package io.ballerina.plugins.idea.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.intellij.util.indexing.IdFilter;
import io.ballerina.plugins.idea.BallerinaFileElementType;
import io.ballerina.plugins.idea.psi.BallerinaNamespaceDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Provides namespace index support.
 */
public class BallerinaNamespaceIndex extends StringStubIndexExtension<BallerinaNamespaceDeclaration> {

    public static final StubIndexKey<String, BallerinaNamespaceDeclaration> KEY =
            StubIndexKey.createIndexKey("ballerina.namespace");

    @Override
    public int getVersion() {
        return BallerinaFileElementType.VERSION;
    }

    @NotNull
    @Override
    public StubIndexKey<String, BallerinaNamespaceDeclaration> getKey() {
        return KEY;
    }

    @NotNull
    public static Collection<BallerinaNamespaceDeclaration> find(@NotNull String name, @NotNull Project project,
                                                                 @Nullable GlobalSearchScope scope,
                                                                 @Nullable IdFilter idFilter) {
        return StubIndex.getElements(KEY, name, project, scope, idFilter, BallerinaNamespaceDeclaration.class);
    }

    public static boolean process(@NotNull String name, @NotNull Project project, @Nullable GlobalSearchScope scope,
                                  @Nullable IdFilter idFilter,
                                  @NotNull Processor<BallerinaNamespaceDeclaration> processor) {
        return StubIndex.getInstance().processElements(KEY, name, project, scope, idFilter,
                BallerinaNamespaceDeclaration.class, processor);
    }
}
