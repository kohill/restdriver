package com.healthedge.rest.data;

import com.healthedge.config.props.ConfigLoader;
import com.healthedge.config.props.PropertyReader;
import com.healthedge.rest.config.RestConstants;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class RestTestData implements IRestTestData {

    public static final String ROOT_TESTDATA_PATH = PropertyReader.getProperty(RestConstants.Properties.REST_TESTDATA_ROOT_FOLDER, "src/test/resources/testdata/rest");
    public static final GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    public static final Gson gson = gsonBuilder.create();
    private static final IDataParser PARSER = ConfigLoader.getClassInstance(IDataParser.class, DefaultDataParser.class, RestConstants.Properties.REST_PARSER_CLASS);
    private JsonElement jsonElement;

    public RestTestData fromFile(String pathToFile) {
        Path path = Paths.get(pathToFile).startsWith(Paths.get(ROOT_TESTDATA_PATH)) ? Paths.get(pathToFile) : Paths.get(ROOT_TESTDATA_PATH, pathToFile);
        try {
            jsonElement = parseJsonValues(JsonParser.parseReader(Files.newBufferedReader(path)));
        } catch (IOException e) {
            throw new RuntimeException("Wrong path to File or File doesn't exist", e);
        }
        return this;
    }

    public RestTestData fromFile(String pathToFolder, String filename) {
        Path path = Paths.get(pathToFolder).startsWith(Paths.get(ROOT_TESTDATA_PATH)) ? Paths.get(pathToFolder, filename) : Paths.get(ROOT_TESTDATA_PATH, pathToFolder, filename);

        try {
            jsonElement = parseJsonValues(JsonParser.parseReader(Files.newBufferedReader(path)));
        } catch (IOException e) {
            throw new RuntimeException("Wrong path to File or File doesn't exist", e);
        }
        return this;
    }

    public RestTestData fromObject(Object json) {
        jsonElement = parseJsonValues(gson.toJsonTree(json));
        return this;
    }

    public RestTestData fromString(String json) {
        jsonElement = parseJsonValues(JsonParser.parseString(json));
        return this;
    }

    public RestTestData fromJsonElement(JsonElement jsonElement) {
        this.jsonElement = parseJsonValues(jsonElement);
        return this;
    }

    @Override
    public <T> T asModel(Class<T> objectType, String testDataName, String state) {
        return gson.fromJson(getJsonElement(testDataName, state), objectType);
    }

    @Override
    public <T> T asModel(Class<T> objectType, String testDataName) {
        return gson.fromJson(getJsonElement(testDataName), objectType);
    }

    @Override
    public <T> T asModel(Class<T> objectType) {
        return gson.fromJson(asJsonElement(), objectType);
    }

    @Override
    public JsonElement asJsonElement() {
        return this.jsonElement.deepCopy();
    }

    @Override
    public JsonElement asJsonElement(String testDataName) {
        return getJsonElement(testDataName);
    }

    public JsonElement asJsonElement(String testDataName, String state) {
        return getJsonElement(testDataName, state);
    }

    @Override
    public Map asMap() {
        return asModel(Map.class);
    }

    @Override
    public Map asMap(String testDataName) {
        return asModel(Map.class, testDataName);
    }

    @Override
    public Map asMap(String testDataName, String state) {
        return asModel(Map.class, testDataName, state);
    }


    @Override
    public String asString() {
        return asJsonElement().toString();
    }

    @Override
    public String asString(String testDataName) {
        return getJsonElement(testDataName).toString();
    }

    @Override
    public String asString(String testDataName, String state) {
        return getJsonElement(testDataName, state).toString();
    }

    private JsonElement getJsonElement(String testDataName) {
        return getJsonElement(testDataName, null);
    }

    private JsonElement getJsonElement(String testDataName, String state) {
        JsonElement element = asJsonElement();
        String adjustedName = state != null ? testDataName.concat("_").concat(state) : testDataName;
        String name = element.getAsJsonObject().has(adjustedName) ? adjustedName : testDataName;
        if (!element.getAsJsonObject().has(name)) {
            throw new RuntimeException("Node is not present in json structure");
        }
        return element.getAsJsonObject().get(name);
    }

    private JsonElement parseJsonValues(JsonElement element) {
        return JsonParser.parseString(PARSER.parse(element.toString()));
    }
}
