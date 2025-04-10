package examples;

import com.companyname.rest.RestContext;
import com.companyname.rest.data.RestTestData;
import com.companyname.rest.data.RestTestDataContext;
import com.companyname.rest.driver.HttpMethod;
import com.companyname.rest.driver.IRequestSpec;
import com.companyname.rest.driver.IResponse;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class RestOOExamples {

    static final String OKTA_OAUTH2 = "/oauth2/ausonkbfb50QxAYGV0h7/v1/token";
    static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final Logger LOG = LogManager.getLogger(RestOOExamples.class);

    //@Test
    // Rest Request Examples out of the box
    public void outOfTheBoxExamples() {
        //getting Test data
        Map testData = new RestTestData().fromFile("default/restServiceName/rest_example1.json").asMap("TestData1");
        //sending request and getting response as Map
        Map response = (Map) new RestContext().getSpec(OKTA_OAUTH2).queryParams(testData).contentType(CONTENT_TYPE).request(HttpMethod.POST, Map.class).asSuccess();
        //Getting element from Map and printing to log.
        LOG.info((String) response.get("access_token"));
    }

    //@Test
    // Rest Request Examples using a wrapped calls with test data management
    public void restServiceWrappedExamples() {
        RestEndpointWrapper wrapper = new RestEndpointWrapper();
        //getting default test data
        Map defaultTD = wrapper.getDefaultTD("rest_example1.json").asMap("TestData1");

        //getting static test data
        Map staticTD = wrapper.getStaticTD("rest_example2").asMap("TestData2");

        //getting test specific test data
        Map specificTD = wrapper.getTestSpecificTD().asMap("TestData3");

        //sending request with default test data and getting response as String
        Map response1 = wrapper.getResponse(defaultTD).asModel();
        //Getting element from Map and printing to log.
        LOG.info("response1: " + response1.get("access_token"));

        //sending request with static test data and getting response as XmlPath
        JsonPath response2 = wrapper.getResponse(staticTD).asJsonPath();
        //Getting element from Map and printing to log.
        LOG.info("response2: " + response2.get("access_token"));

        //sending request with test specific test data and getting response without transformation
        Map response3 = wrapper.getResponse(specificTD).asModel();
        //Getting element from Map and printing to log.
        LOG.info("response3: " + response3.get("access_token"));

    }

    public class RestEndpointWrapper implements RestTestDataContext {
        RestContext restContext = new RestContext();

        @Override
        public String getServiceName() {
            return "restServiceName";
        }

        public IRequestSpec restServiceSpec() {
            return restContext.getSpec(OKTA_OAUTH2);
        }

        public IResponse<Map> getResponse(Map testData) {
            return restServiceSpec().queryParams(testData).contentType(CONTENT_TYPE).request(HttpMethod.POST, Map.class);
        }

    }


}
