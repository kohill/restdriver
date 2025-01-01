package com.healthedge.rest.data;

import com.google.gson.JsonElement;

import java.util.LinkedHashMap;
import java.util.Map;

public interface IRestTestData {

    /**
     * @param state        - US state name for state specific TD
     * @param testDataName - name of json object graphs.
     * @param objectType   - Java object type.
     * @return Test Data as Java object
     */
    <T> T asModel(Class<T> objectType, String testDataName, String state);

    /**
     * @param testDataName - name of json object graphs.
     * @param objectType   - Java object type.
     * @return Test Data as Java object
     */
    <T> T asModel(Class<T> objectType, String testDataName);

    /**
     * @param objectType - Java object type
     * @return Test Data as Java object
     */
    <T> T asModel(Class<T> objectType);

    /**
     * @return Test Data as {@link JsonElement}
     */
    JsonElement asJsonElement();

    /**
     * @param testDataName - name of json object graphs.
     * @return Test Data as {@link JsonElement}
     */
    JsonElement asJsonElement(String testDataName);

    /**
     * @param state        - US state name for state specific TD
     * @param testDataName - name of json object graphs.
     * @return Test Data as {@link JsonElement}
     */
    JsonElement asJsonElement(String testDataName, String state);

    /**
     * @return Test Data as {@link LinkedHashMap}
     */
    Map asMap();

    /**
     * @param testDataName - name of json object graphs.
     * @return Test Data as {@link LinkedHashMap}
     */
    Map asMap(String testDataName);

    /**
     * @param state        - US state name for state specific TD
     * @param testDataName - name of json object graphs.
     * @return Test Data as {@link LinkedHashMap}
     */
    Map asMap(String testDataName, String state);

    /**
     * @return Test Data as {@link String}
     */
    String asString();

    /**
     * @return Test Data as {@link String}
     */
    String asString(String testDataName);

    /**
     * @param state - US state name for state specific TD
     * @return Test Data as {@link String}
     */
    String asString(String testDataName, String state);
}
