/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.gst.portfolio.shareproducts.service;

import com.gst.infrastructure.core.api.JsonCommand;
import com.gst.infrastructure.core.data.CommandProcessingResult;
import com.gst.infrastructure.core.serialization.FromJsonHelper;
import com.gst.portfolio.products.service.ProductCommandsService;
import com.gst.portfolio.shareproducts.constants.ShareProductApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;

@Service(value = "SHAREPRODUCT_COMMANDSERVICE")
public class ShareProductCommandsServiceImpl implements ProductCommandsService {

    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public ShareProductCommandsServiceImpl(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }


    public CommandProcessingResult postDividends(Long productId, JsonCommand jsonCommand) {
        return null ;
    }

    @Override
    public Object handleCommand(Long productId, String command, String jsonBody) {
        final JsonElement parsedCommand = this.fromApiJsonHelper.parse(jsonBody);
        final JsonCommand jsonCommand = JsonCommand.from(jsonBody, parsedCommand, this.fromApiJsonHelper, null, null, null, null, null,
                null, null, null, null, null);
        if (ShareProductApiConstants.PREIEW_DIVIDENDS_COMMAND_STRING.equals(command)) {
            return null ;
        } else if (ShareProductApiConstants.POST_DIVIDENdS_COMMAND_STRING.equals(command)) { return postDividends(productId,
                jsonCommand); }
        // throw unknow commandexception
        return CommandProcessingResult.empty();
    }

}
