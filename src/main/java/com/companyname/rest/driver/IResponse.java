package com.companyname.rest.driver;

import com.companyname.rest.driver.model.RestError;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IResponse<T> {

    /**
     * Get the body and map it to a Java object. For JSON responses this requires that you have either
     *
     * @param cls Response Deserialization class type.
     * @return Class type Java object if response success, otherwise {@link RestError}
     */
    <R> R asModel(Class<T> cls);

    /**
     * Get the body and map it to a Java object. For JSON responses this requires that you have either
     *
     * @return Class type Java object if response success, otherwise {@link RestError}
     */
    <R> R asModel();

    /**
     * Get the body and map it to a Java object.
     *
     * @param cls Response Deserialization class type.
     * @return Class type Java object if response success, otherwise Exception
     */
    <T> T asSuccess(Class<T> cls);


    /**
     * Get the body and map it to a Java object.
     *
     * @return Class type Java object if response success, otherwise Exception
     */
    <T> T asSuccess();

    /**
     * Get the body and map it to a Java object.
     *
     * @return {@link RestError} if response contains errors, otherwise Exception
     */
    RestError asError();

    /**
     * Get the content type of the response
     *
     * @return The content type value or <code>null</code> if not found.
     */
    String getContentType();

    /**
     * The response cookies as simple name/value pair. It assumes that no cookies have the same name. If two cookies should never the less
     * have the same name <i>the first cookie value</i> is used.
     *
     * @return The response cookies.
     */
    Map<String, String> getCookies();

    /**
     * Get a single header value associated with the given name. If the header is a multi-value header then you need to use
     * {@link Headers#getList(String)} in order to get all values..
     *
     * @return The header value or <code>null</code> if value was not found.
     */
    String getHeader(String name);

    /**
     * The response headers as List. If there are several response headers with the same name a list of
     * the response header values are returned.
     */
    List<Header> getHeadersAsList();

    /**
     * The response headers as String. If there are several response headers with the same name a list of
     * the response header values are returned.
     */
    String getHeadersAsString();

    /**
     * Checks whether response is successful or not.
     *
     * @return true if response successful, otherwise false
     */
    Boolean isSuccessful();

    /**
     * @return The original Rest Driver Response
     */
    <T> T getOriginalResponse();

    /**
     * Get the status code of the response.
     *
     * @return The status code of the response.
     */
    int getStatusCode();

    /**
     * Get response body as a string.
     *
     * @return Response body as a string.
     */
    String asString();

    /**
     * Get the body as a {@link XmlPath}.
     *
     * @return The body as a {@link XmlPath}.
     */
    XmlPath asXmlPath();


    /**
     * Only for SOAP
     * Get the body as a {@link JsonPath}.
     *
     * @return The body as a {@link JsonPath}.
     */
    JsonPath asJsonPath();

    /**
     * Get the body as a pretty formatted string.
     *
     * @return The body as a string.
     */
    String asPrettyString();

    /**
     * Get the body as a byte array.
     *
     * @return The body as a array.
     */
    byte[] asByteArray();

    /**
     * Get the body as an input stream.
     *
     * @return The body as an input stream.
     */
    InputStream asInputStream();

}
