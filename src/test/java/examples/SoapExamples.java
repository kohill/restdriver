package examples;

import com.companyname.soap.SoapContext;
import com.companyname.soap.data.SoapTestData;
import com.companyname.soap.data.SoapTestDataContext;
import com.companyname.rest.driver.HttpMethod;
import com.companyname.rest.driver.IResponse;
import io.restassured.path.xml.XmlPath;

public class SoapExamples {

    //@Test
    // Soap Request Examples out of the box
    public void outOfTheBoxExamples() {
        //getting Test data
        String testData = new SoapTestData().fromFile("default/serviceName/soap_example1.xml");
        //sending request and getting response as String
        String response = new SoapContext().getSpec().body(testData).request(HttpMethod.POST, String.class).asString();
    }

    //@Test
    // Soap Request Examples using a wrapped calls with test data management
    public void serviceWrappedExamples() {
        SoapEndpointWrapper wrapper = new SoapEndpointWrapper();
        //getting default test data
        String defaultTD = wrapper.getDefaultTD("soap_example1");

        //getting static test data
        String staticTD = wrapper.getStaticTD("soap_example2");

        //getting test specific test data
        String specificTD = wrapper.getTestSpecificTD();

        //sending request with default test data and getting response as String
        String response1 = wrapper.getResponse(defaultTD).asString();

        //sending request with static test data and getting response as XmlPath
        XmlPath response2 = wrapper.getResponse(staticTD).asXmlPath();

        //sending request with test specific test data and getting response without transformation
        IResponse<String> response3 = wrapper.getResponse(specificTD);

    }

    public class SoapEndpointWrapper implements SoapTestDataContext {

        @Override
        public String getServiceName() {
            return "serviceName";
        }

        public IResponse<String> getResponse(String testData) {
            return new SoapContext().getSpec().body(testData).request(HttpMethod.POST, String.class);
        }

    }


}
