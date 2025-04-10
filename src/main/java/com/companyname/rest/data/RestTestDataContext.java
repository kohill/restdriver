package com.companyname.rest.data;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public interface RestTestDataContext {

    /**
     * Sets test data folder name related to Service name
     **/
    String getServiceName();

    /**
     * Gets default TestData from src/test/resources/testdata/{restType}/default/{serviceName}/{fileName}.json file
     **/
    default RestTestData getDefaultTD(String fileName) {
        return new RestTestData().fromFile(Paths.get("default", getServiceName(), checkFileExtention(fileName)).toString());
    }

    /**
     * Gets Test Specific TestData;
     * File path is build according to test package {package}/{TestClassName}.json
     **/
    default RestTestData getTestSpecificTD() {
        StackTraceElement lastNonReflectionThread = null;
        for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
            if (se.getClassName().contains("jdk.internal.reflect.NativeMethodAccessorImpl")) {
                break;
            }
            lastNonReflectionThread = se;
        }
        if (lastNonReflectionThread == null) {
            throw new RuntimeException("Not possible to get TestData, stacktrace element with test was not found.");
        }
        List<String> path = Arrays.asList(lastNonReflectionThread.getClassName().split("\\."));
        return new RestTestData().fromFile(Paths.get(StringUtils.join(path, File.separator), checkFileExtention(lastNonReflectionThread.getMethodName())).toString());
    }

    /**
     * Gets Custom TestData from static folder
     * src/test/resources/testdata/{restType}/static/{serviceName}/{fileName}.json
     * Can be used in cases when the same test data should be used for multiple tests in order not to create test specific TestData for each test.
     **/
    default RestTestData getStaticTD(String fileName) {
        return new RestTestData().fromFile(Paths.get("static", getServiceName(), checkFileExtention(fileName)).toString());
    }

    default String checkFileExtention(String fileName) {
        String result = fileName;
        if (!result.endsWith(".json")) {
            result = result.concat(".json");
        }
        return result;
    }
}
