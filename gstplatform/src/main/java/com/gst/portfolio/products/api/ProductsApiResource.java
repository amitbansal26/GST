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
package com.gst.portfolio.products.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.gst.commands.domain.CommandWrapper;
import com.gst.commands.service.CommandWrapperBuilder;
import com.gst.commands.service.PortfolioCommandSourceWritePlatformService;
import com.gst.infrastructure.core.api.ApiRequestParameterHelper;
import com.gst.infrastructure.core.data.CommandProcessingResult;
import com.gst.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import com.gst.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import com.gst.infrastructure.core.service.Page;
import com.gst.infrastructure.security.service.PlatformSecurityContext;
import com.gst.portfolio.products.constants.ProductsApiConstants;
import com.gst.portfolio.products.data.ProductData;
import com.gst.portfolio.products.exception.ResourceNotFoundException;
import com.gst.portfolio.products.service.ProductReadPlatformService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/products/{type}")
@Component
@Scope("singleton")
public class ProductsApiResource {

    private final ApplicationContext applicationContext;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final DefaultToApiJsonSerializer<ProductData> toApiJsonSerializer;
    private final PlatformSecurityContext platformSecurityContext;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;

    @Autowired
    public ProductsApiResource(final ApplicationContext applicationContext, final ApiRequestParameterHelper apiRequestParameterHelper,
            final DefaultToApiJsonSerializer<ProductData> toApiJsonSerializer, final PlatformSecurityContext platformSecurityContext,
            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.applicationContext = applicationContext;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.platformSecurityContext = platformSecurityContext;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    @GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveTemplate(@PathParam("type") final String productType, @Context final UriInfo uriInfo) {
        String serviceName = productType + ProductsApiConstants.READPLATFORM_NAME;
        try {
            ProductReadPlatformService service = (ProductReadPlatformService) this.applicationContext.getBean(serviceName);
            ProductData data = service.retrieveTemplate();
            final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
            return this.toApiJsonSerializer.serialize(settings, data, service.getResponseDataParams());    
        }catch(BeansException e) {
            throw new ResourceNotFoundException() ;
        }
    }

    @GET
    @Path("{productId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveProduct(@PathParam("productId") final Long productId, @PathParam("type") final String productType,
            @Context final UriInfo uriInfo) {
        try {
            String serviceName = productType + ProductsApiConstants.READPLATFORM_NAME;
            ProductReadPlatformService service = (ProductReadPlatformService) this.applicationContext.getBean(serviceName);
            final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
            ProductData data = service.retrieveOne(productId, settings.isTemplate());
            return this.toApiJsonSerializer.serialize(settings, data, service.getResponseDataParams());
        } catch (BeansException e) {
            throw new ResourceNotFoundException();
        }
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllProducts(@PathParam("type") final String productType, @QueryParam("offset") final Integer offset, @QueryParam("limit") final Integer limit,
            @Context final UriInfo uriInfo) {
        try {
            String serviceName = productType + ProductsApiConstants.READPLATFORM_NAME;
            ProductReadPlatformService service = (ProductReadPlatformService) this.applicationContext.getBean(serviceName);
            Page<ProductData> data = service.retrieveAllProducts(offset, limit);
            final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
            return this.toApiJsonSerializer.serialize(settings, data, service.getResponseDataParams());    
        }catch(BeansException e) {
            throw new ResourceNotFoundException();
        }
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createProduct(@PathParam("type") final String productType, final String apiRequestBodyAsJson) {
        CommandWrapper commandWrapper = null;
        this.platformSecurityContext.authenticatedUser();
        commandWrapper = new CommandWrapperBuilder().createProduct(productType).withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult commandProcessingResult = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
        return this.toApiJsonSerializer.serialize(commandProcessingResult);
    }

    @POST
    @Path("{productId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String handleCommands(@PathParam("type") final String productType, @PathParam("productId") final Long productId,
            @QueryParam("command") final String commandParam, @SuppressWarnings("unused") @Context final UriInfo uriInfo, final String apiRequestBodyAsJson) {
        CommandWrapper commandWrapper = new CommandWrapperBuilder().createProductCommand(productType, commandParam, productId)
                .withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult commandProcessingResult = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
        return this.toApiJsonSerializer.serialize(commandProcessingResult);
    }

    @PUT
    @Path("{productId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateProduct(@PathParam("type") final String productType, @PathParam("productId") final Long productId,
            final String apiRequestBodyAsJson) {
        this.platformSecurityContext.authenticatedUser();
        final CommandWrapper commandRequest = new CommandWrapperBuilder().updateProduct(productType, productId)
                .withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }
}
