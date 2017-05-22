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
package com.gst.infrastructure.dataqueries.service;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.core.StreamingOutput;

import com.gst.infrastructure.dataqueries.data.GenericResultsetData;
import com.gst.infrastructure.dataqueries.data.ReportData;
import com.gst.infrastructure.dataqueries.data.ReportParameterData;

public interface ReadReportingService {

    StreamingOutput retrieveReportCSV(String name, String type, Map<String, String> extractedQueryParams);

    GenericResultsetData retrieveGenericResultset(String name, String type, Map<String, String> extractedQueryParams);

    String retrieveReportPDF(String name, String type, Map<String, String> extractedQueryParams);

    String getReportType(String reportName);

    Collection<ReportData> retrieveReportList();

    Collection<ReportParameterData> getAllowedParameters();

    ReportData retrieveReport(final Long id);

    Collection<String> getAllowedReportTypes();
    
  //needed for smsCampaign jobs where securityContext is null
    GenericResultsetData retrieveGenericResultSetForSmsCampaign(String name, String type, Map<String, String> extractedQueryParams);
    
    String  sqlToRunForSmsCampaign(String name, String type, Map<String, String> queryParams);
}