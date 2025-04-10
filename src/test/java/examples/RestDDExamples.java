package examples;

import com.companyname.rest.RestDDContext;
import com.companyname.rest.data.RestDDAdapter;
import com.companyname.rest.driver.IResponse;
import com.companyname.rest.driver.model.RestDDModel;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.util.LinkedList;

public class RestDDExamples {

    //  @Test
    public void exampleOutOfTheBox() {
        RestDDAdapter adapter = new RestDDAdapter();
        LinkedList<RestDDModel> tests = adapter.fromSingleFile("example_rest_dd.json").getScenarioList();
        RestDDContext context = new RestDDContext(tests.get(0));

        tests.get(0).getSteps().forEach((s, step) -> {
            IResponse response = context.send(step);
            String expectedResponse = context.getExpectedResponse(step);
//            Commented out so dependency is not needed
//            assertThat(response).as("Null response from " + tests.get(0).getTestDescription()).isNotNull();
        });
    }


    // @Test(dataProvider = "restTestData")
    public void exampleUsingdataProvider(RestDDModel testData) {
        RestDDContext context = new RestDDContext(testData);

        testData.getSteps().forEach((s, step) -> {
            IResponse response = context.send(step);
            String expectedResponse = context.getExpectedResponse(step);
//            Commented out so dependency is not needed
//            assertThat(response).as("Null response from " + testData.getTestDescription()).isNotNull();
        });
    }

    @DataProvider(name = "restTestData")
    public Object[] getData(ITestContext testContext) {
        RestDDAdapter adapter = new RestDDAdapter();
        LinkedList<RestDDModel> tests = adapter.fromPropertyFiles().getScenarioList();
        RestDDModel[] modelArray = new RestDDModel[tests.size()];
        modelArray = tests.toArray(modelArray);
        return modelArray;
    }
}
