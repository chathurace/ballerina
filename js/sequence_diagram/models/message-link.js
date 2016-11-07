/**
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
define(['lodash', 'diagram', 'geometry'], function ( _, Diagrams) {

    var MessageLink = Diagrams.Models.DiagramElement.extend(
        /** @lends MessageLink.prototype */
        {
            /**
             * @augments Link
             * @constructs
             * @class MessageLink Represents the model for a Message in Sequence Diagrams.
             */
            initialize: function (attrs, options) {
                Diagrams.Models.DiagramElement.prototype.initialize.call(this, attrs, options);
                this.set('sourcePoint', attrs.source);
                this.set('destinationPoint', attrs.destination);
                this.source().message(this);
                this.destination().message(this);
            },

            modelName: "MessageLink",

            defaults: {},

            source: function (messagePoint) {
                if (messagePoint) {
                    this.set('sourcePoint', messagePoint);
                } else {
                    return this.get('sourcePoint');
                }
            },

            destination: function (messagePoint) {
                if (messagePoint) {
                    this.set('destinationPoint', messagePoint);
                } else {
                    return this.get('destinationPoint');
                }
            }

        });

    return Message;
});

