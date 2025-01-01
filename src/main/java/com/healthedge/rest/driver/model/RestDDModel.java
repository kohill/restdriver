package com.healthedge.rest.driver.model;

import com.healthedge.rest.data.RestTestData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RestDDModel {

    String testDescription;
    String relativeFileName;
    String relativeScenarioName;
    Map<String, String> customParameters;
    Map<String, Step> steps;

    public String getTestDescription() {
        return testDescription;
    }

    public RestDDModel setTestDescription(String testDescription) {
        this.testDescription = testDescription;
        return this;
    }

    public String getRelativeFileName() {
        return relativeFileName;
    }

    public RestDDModel setRelativeFileName(String relativeFileName) {
        this.relativeFileName = relativeFileName;
        return this;
    }

    public String getRelativeScenarioName() {
        return relativeScenarioName;
    }

    public RestDDModel setRelativeScenarioName(String relativeScenarioName) {
        this.relativeScenarioName = relativeScenarioName;
        return this;
    }

    public Map<String, String> getCustomParameters() {
        return customParameters;
    }

    public RestDDModel setCustomParameters(Map<String, String> customParameters) {
        this.customParameters = customParameters;
        return this;
    }

    public Map<String, Step> getSteps() {
        return steps;
    }

    public RestDDModel setSteps(Map<String, Step> steps) {
        if (steps == null) {
            throw new RuntimeException("Error parsing test data, no 'Steps' identified, please check test data");
        }
        this.steps = steps;
        return this;
    }

    public RestDDModel.Step getLastStep() {
        List<Map.Entry<String, Step>> entryList = new LinkedList<>(getSteps().entrySet());
        return entryList.get(entryList.size() - 1).getValue();
    }

    public RestDDModel merge(Step mergeFrom) {
        Map<String, Step> stepsMap = this.getSteps();
        stepsMap.keySet().forEach(stepKey -> stepsMap.get(stepKey).merge(mergeFrom));
        return this;
    }

    public class Request extends MergeableFields {
        String baseUri;
        String endpoint;
        String method;
        Map headers;
        Object body;
        Map params;
        Map queryParams;
        Map pathParams;
        String contentType;
        Boolean authRequired;
        String authToken;
        String authFunctionName;

        public String getBaseUri() {
            return baseUri;
        }

        public Request setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public Request setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public Request setMethod(String method) {
            this.method = method;
            return this;
        }

        public Map getHeaders() {
            return headers;
        }

        public Request setHeaders(Map headers) {
            this.headers = headers;
            return this;
        }

        public Object getBody() {
            return body;
        }

        public Request setBody(Object body) {
            this.body = body;
            return this;
        }

        public Map getParams() {
            return params;
        }

        public Request setParams(Map params) {
            this.params = params;
            return this;
        }

        public Map getQueryParams() {
            return queryParams;
        }

        public Request setQueryParams(Map queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Map getPathParams() {
            return pathParams;
        }

        public Request setPathParams(Map pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public String getContentType() {
            return contentType;
        }

        public Request setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Boolean getAuthRequired() {
            if (null == authRequired) {
                authRequired = true;
            }
            return authRequired;
        }

        public Request setAuthRequired(Boolean authRequired) {
            this.authRequired = authRequired;
            return this;
        }

        public String getAuthToken() {
            return authToken;
        }

        public Request setAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }


        public String getAuthFunctionName() {
            return authFunctionName;
        }

        public Request setAuthFunctionName(String authFunctionName) {
            this.authFunctionName = authFunctionName;
            return this;
        }

    }

    public class Step extends MergeableFields {
        String stepName;
        String stepDescription;
        Request request;
        String expectedStatusCode;
        Object expectedResponse;

        public String getStepName() {
            return stepName;
        }

        public Step setStepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public String getStepDescription() {
            return stepDescription;
        }

        public Step setStepDescription(String stepDescription) {
            this.stepDescription = stepDescription;
            return this;
        }

        public Request getRequest() {
            return request;
        }

        public Step setRequest(Request request) {
            this.request = request;
            return this;
        }

        public String getExpectedStatusCode() {
            if (null == expectedStatusCode) {
                expectedStatusCode = "-1";
            }
            return expectedStatusCode;
        }

        public Step setExpectedStatusCode(String expectedStatusCode) {
            this.expectedStatusCode = expectedStatusCode;
            return this;
        }

        public Integer getExpectedStatusCodeAsInt() {
            return Integer.parseInt(expectedStatusCode);
        }

        public Object getExpectedResponse() {
            return expectedResponse;
        }

        public Step setExpectedResponse(Object expectedResponse) {
            this.expectedResponse = expectedResponse;
            return this;
        }

        public String getExpectedResponseString() {
            return RestTestData.gsonBuilder.serializeNulls().create().toJson(expectedResponse);
        }

        @Override
        public <T> T merge(T mergeFrom) {
            super.merge(mergeFrom);
            if (this.getRequest() != null) {
                this.getRequest().merge(((Step) mergeFrom).getRequest());
            }
            return (T) this;
        }
    }

}
