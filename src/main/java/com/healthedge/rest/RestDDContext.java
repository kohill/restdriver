package com.healthedge.rest;

import com.healthedge.config.props.ConfigLoader;
import com.healthedge.rest.config.DefaultRestURLSupplier;
import com.healthedge.rest.config.RestConstants;
import com.healthedge.rest.data.DefaultDataParser;
import com.healthedge.rest.data.IDataParser;
import com.healthedge.rest.data.RestTestData;
import com.healthedge.rest.driver.Header;
import com.healthedge.rest.driver.HttpMethod;
import com.healthedge.rest.driver.IRequestSpec;
import com.healthedge.rest.driver.IResponse;
import com.healthedge.rest.driver.impl.RestAssuredRequestSpecImpl;
import com.healthedge.rest.driver.model.RestDDModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RestDDContext {

    private static final IDataParser parser = ConfigLoader.getClassInstance(IDataParser.class, DefaultDataParser.class, RestConstants.Properties.REST_PARSER_CLASS);
    private static final Logger LOG = LogManager.getLogger(RestDDContext.class);
    protected static String baseUri = (String) ConfigLoader.getClassInstance(Supplier.class, DefaultRestURLSupplier.class, RestConstants.Properties.REST_URL_SUPPLIER_CLASS).get();

    protected RestDDModel model;
    protected Map<String, IRequestSpec> specMap = new LinkedHashMap<>();
    protected Map<String, IResponse> responseMap = new LinkedHashMap<>();
    protected Map<String, String> responseCache = new LinkedHashMap<>();
    protected Map<String, List<Header>> headersCache = new LinkedHashMap<>();


    public RestDDContext(RestDDModel model) {
        this.model = model;
    }

    public IResponse send(RestDDModel.Step step) {
        RestDDModel.Step parsedStep = parseStepFromCache(step);
        LOG.info("'{}.{}' Step request is being sent.", model.getRelativeScenarioName(), step.getStepName());
        responseMap.put(step.getStepName(), getSpec(parsedStep).request(HttpMethod.valueOf(parsedStep.getRequest().getMethod().toUpperCase())));
        LOG.info("'{}.{}' Step response received.", model.getRelativeScenarioName(), step.getStepName());
        if (-1 != getStatusCode(step)) {
            Assert.assertEquals(responseMap.get(step.getStepName()).getStatusCode(), getStatusCode(step),
                    "Actual Status Code: '" + responseMap.get(step.getStepName()).getStatusCode() + "' doesn't match the expected one: '" + getStatusCode(step) + "'. Test execution stopped.");
        }
        responseCache.put(step.getStepName(), responseMap.get(step.getStepName()).asString());
        headersCache.put(step.getStepName(), responseMap.get(step.getStepName()).getHeadersAsList());
        return responseMap.get(step.getStepName());
    }

    public Map<String, IResponse> sendAll() {
        Map<String, RestDDModel.Step> steps = model.getSteps();
        steps.keySet().forEach(s -> {
            send(steps.get(s));
        });
        return responseMap;
    }

    public Map<String, IResponse> getResponseMap() {
        return responseMap;
    }

    public IResponse getLastResponse() {
        List<Map.Entry<String, IResponse>> entryList = new LinkedList<>(responseMap.entrySet());
        return entryList.get(entryList.size() - 1).getValue();
    }

    public String getTestDescription() {
        return this.model.getTestDescription();

    }

    public String getExpectedResponse(RestDDModel.Step step) {
        return parseStepFromCache(step).getExpectedResponseString();
    }

    public int getStatusCode(RestDDModel.Step step) {
        return step.getExpectedStatusCodeAsInt();
    }

    private IRequestSpec getSpec(RestDDModel.Step step) {
        IRequestSpec spec = ConfigLoader.getClassInstance(IRequestSpec.class, RestAssuredRequestSpecImpl.class, RestConstants.Properties.REST_SPEC_DRIVER);
        RestDDModel.Request modelRequest = step.getRequest();
        spec.baseUri(baseUri);
        if (null != modelRequest.getBaseUri()) {
            spec.baseUri(modelRequest.getBaseUri());
        }
        if (null != modelRequest.getEndpoint()) {
            spec.basePath(modelRequest.getEndpoint());
        }
        if (null != modelRequest.getContentType()) {
            spec.contentType(modelRequest.getContentType());
        }
        if (null != modelRequest.getBody()) {
            spec.body(modelRequest.getBody());
        }
        if (null != modelRequest.getHeaders()) {
            spec.headers(modelRequest.getHeaders());
        }
        if (null != modelRequest.getParams()) {
            spec.params(modelRequest.getParams());
        }
        if (null != modelRequest.getPathParams()) {
            spec.pathParams(modelRequest.getPathParams());
        }
        if (null != modelRequest.getQueryParams()) {
            spec.queryParams(modelRequest.getQueryParams());
        }

        if (modelRequest.getAuthRequired()) {
            if (null != modelRequest.getAuthFunctionName()) {
                spec.authFunction(modelRequest.getAuthFunctionName());
            }
            if (null != modelRequest.getAuthToken()) {
                spec.authHeader(modelRequest.getAuthToken());
            }
        }
        specMap.put(step.getStepName(), spec);
        return specMap.get(step.getStepName());
    }

    protected RestDDModel.Step parseStepFromCache(RestDDModel.Step step) {
        String stepString = RestTestData.gson.toJson(step);
        String parsedString = parser.parseCache(stepString, responseCache);
        parsedString = parser.parseHeadersCache(parsedString, headersCache);
        return RestTestData.gson.fromJson(parsedString, RestDDModel.Step.class);
    }
}
