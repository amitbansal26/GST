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

package com.gst.portfolio.self.security.api;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import com.gst.infrastructure.core.exception.InvalidJsonException;
import com.gst.infrastructure.core.serialization.FromJsonHelper;
import com.gst.infrastructure.security.service.PlatformSecurityContext;
import com.gst.useradministration.api.UsersApiResource;
import com.gst.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;

@Path("/self/user")
@Component
public class SelfUserApiResource {

        private final UsersApiResource usersApiResource;
        private final PlatformSecurityContext context;
        private final FromJsonHelper fromApiJsonHelper;
        private final Set<String> supportedParameters = new HashSet<>(Arrays.asList("password", "repeatPassword"));

        @Autowired
        public SelfUserApiResource(final UsersApiResource usersApiResource,
                final PlatformSecurityContext context,
                final FromJsonHelper fromApiJsonHelper){

                this.usersApiResource = usersApiResource;
                this.context = context;
                this.fromApiJsonHelper = fromApiJsonHelper;
        }

        @PUT
        public String update(final String apiRequestBodyAsJson) {
                if (StringUtils.isBlank(apiRequestBodyAsJson)) { throw new InvalidJsonException(); }

                final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
                this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap,
                        apiRequestBodyAsJson,
                        this.supportedParameters);

                final AppUser appUser = this.context.authenticatedUser();
                return this.usersApiResource.update(appUser.getId(), apiRequestBodyAsJson);
        }

}
