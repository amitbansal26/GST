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
package com.gst.spm.api;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gst.infrastructure.security.service.PlatformSecurityContext;
import com.gst.portfolio.client.domain.Client;
import com.gst.portfolio.client.domain.ClientRepositoryWrapper;
import com.gst.spm.data.ScorecardData;
import com.gst.spm.domain.Scorecard;
import com.gst.spm.domain.Survey;
import com.gst.spm.exception.SurveyNotFoundException;
import com.gst.spm.service.ScorecardService;
import com.gst.spm.service.SpmService;
import com.gst.spm.util.ScorecardMapper;
import com.gst.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Path("/surveys/{surveyId}/scorecards")
@Component
@Scope("singleton")
public class ScorecardApiResource {

    private final PlatformSecurityContext securityContext;
    private final SpmService spmService;
    private final ScorecardService scorecardService;
    private final ClientRepositoryWrapper clientRepositoryWrapper;

    @Autowired
    public ScorecardApiResource(final PlatformSecurityContext securityContext, final SpmService spmService,
                                final ScorecardService scorecardService, final ClientRepositoryWrapper clientRepositoryWrapper) {
        super();
        this.securityContext = securityContext;
        this.spmService = spmService;
        this.scorecardService = scorecardService;
        this.clientRepositoryWrapper = clientRepositoryWrapper;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public List<ScorecardData> findBySurvey(@PathParam("surveyId") final Long surveyId) {
        this.securityContext.authenticatedUser();

        final Survey survey = findSurvey(surveyId);

        final List<Scorecard> scorecards = this.scorecardService.findBySurvey(survey);

        if (scorecards != null) {
            return ScorecardMapper.map(scorecards);
        }

        return Collections.EMPTY_LIST;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public void createScorecard(@PathParam("surveyId") final Long surveyId, final ScorecardData scorecardData) {
        final AppUser appUser = this.securityContext.authenticatedUser();
        final Survey survey = findSurvey(surveyId);
        final Client client = this.clientRepositoryWrapper.findOneWithNotFoundDetection(scorecardData.getClientId());
        this.scorecardService.createScorecard(ScorecardMapper.map(scorecardData, survey, appUser, client));
    }

    @Path("/clients/{clientId}")
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @Transactional
    public List<ScorecardData> findBySurveyClient(@PathParam("surveyId") final Long surveyId,
                                                  @PathParam("clientId") final Long clientId) {
        this.securityContext.authenticatedUser();
        final Survey survey = findSurvey(surveyId);
        final Client client = this.clientRepositoryWrapper.findOneWithNotFoundDetection(clientId);
        final List<Scorecard> scorecards = this.scorecardService.findBySurveyAndClient(survey, client);
        if (scorecards != null) {
            return ScorecardMapper.map(scorecards);
        }
        return Collections.EMPTY_LIST;
    }

    private Survey findSurvey(final Long surveyId) {
        final Survey survey = this.spmService.findById(surveyId);
        if (survey == null) {
            throw new SurveyNotFoundException(surveyId);
        }
        return survey;
    }
}
