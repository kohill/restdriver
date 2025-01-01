package examples;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.function.Function;

public class AuthFunctionExample implements Function<String, String> {

    @Override
    public String apply(String o) {
        switch (o.toUpperCase()) {
            case "OKTA": // These options will be accecible by proving name in brackets in json file or in code
                return getBearerToken("OKTA");// method which makes a call to real service, impelemnted on project level
            case "FAKE":
                return "Bearer:" + RandomStringUtils.random(50, true, true);
            case "EMPTY":
                return "";
            default:
                throw new RuntimeException("Authentication function name is not provided or not implemented.");
        }
    }


    protected String getBearerToken(String param) {
        //implement call to real service and return token value
        return "real token value from service call";
    }
}