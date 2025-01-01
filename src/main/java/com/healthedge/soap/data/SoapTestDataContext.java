package com.healthedge.soap.data;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public interface SoapTestDataContext {

    /**
     * Sets test data folder name related to Service name
     **/
    String getServiceName();

    /**
     * Gets TestData from the full parsed path file
     **/
    default String getPathTD(String path) {
        return new SoapTestData().fromFullPath(Paths.get(path).toString());
    }

    /**
     * Gets default TestData from src/test/resources/testdata/default/soap/{serviceName}/{fileName}.xml file
     **/
    default String getDefaultTD(String fileName) {
        return new SoapTestData().fromFile(Paths.get("default", getServiceName(), fileName + ".xml").toString());
    }

    /**
     * Gets default TestData from src/test/resources/testdata/default/soap/{serviceName}/{state}/{fileName}.xml file
     **/
    default String getDefaultTD(String fileName, String state) {
        return new SoapTestData().fromFile(Paths.get("default", getServiceName(), state, fileName + ".xml").toString());
    }

    /**
     * Gets Custom TestData from static folder
     * src/test/resources/testdata/static/soap/{serviceName}/{fileName}.xml
     **/
    default String getStaticTD(String fileName) {
        return new SoapTestData().fromFile(Paths.get("static", getServiceName(), fileName + ".xml").toString());
    }

    /**
     * Gets Custom TestData from static folder
     * src/test/resources/testdata/static/soap/{serviceName}/{state}/{fileName}.xml
     **/
    default String getStaticTD(String fileName, String state) {
        return new SoapTestData().fromFile(Paths.get("static", getServiceName(), state, fileName + ".xml").toString());
    }

    /**
     * Gets Test Specific TestData;
     * File path is build according to test package {package}/{TestClassName}/{TestMethodName}.xml
     **/
    default String getTestSpecificTD() {
        StackTraceElement lastNonReflectionThread = null;
        for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
            if (se.getClassName().contains("sun.reflect")) {
                break;
            }
            lastNonReflectionThread = se;
        }
        if (lastNonReflectionThread == null) {
            throw new RuntimeException("Not possible to get TestData, stacktrace element with test was not found.");
        }
        List<String> path = Arrays.asList(lastNonReflectionThread.getClassName().split("\\."));
        return new SoapTestData().fromFile(Paths.get(StringUtils.join(path, File.separator), lastNonReflectionThread.getMethodName() + ".xml").toString());
    }
}
