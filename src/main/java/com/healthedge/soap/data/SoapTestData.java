package com.healthedge.soap.data;

import com.healthedge.config.props.ConfigLoader;
import com.healthedge.config.props.PropertyReader;
import com.healthedge.rest.config.RestConstants;
import com.healthedge.rest.data.DefaultDataParser;
import com.healthedge.rest.data.IDataParser;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class SoapTestData {

    public static final String ROOT_PATH = PropertyReader.getProperty(RestConstants.Properties.SOAP_TESTDATA_ROOT_FOLDER, "src/test/resources/testdata/soap");
    private static final IDataParser PARSER = ConfigLoader.getClassInstance(IDataParser.class, DefaultDataParser.class, RestConstants.Properties.REST_PARSER_CLASS);

    public String fromFile(String pathToFile) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(ROOT_PATH + "/" + pathToFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found:" + ROOT_PATH + "/" + pathToFile);
        }
        String td = toString(fileInputStream);
        return td;
    }

    public String fromFullPath(String fullPath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found:" + fullPath);
        }
        String td = toString(fileInputStream);
        return td;
    }

    public String fromPath(Path path) {
        String td = parseValues(path.toString());
        return td;
    }

    private String toString(FileInputStream fileInputStream) {
        String td = null;
        try {
            td = parseValues(IOUtils.toString(fileInputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Error during reading data from:" + fileInputStream);
        }
        return td;
    }

    private String parseValues(String data) {
        return PARSER.parse(data);
    }
}
