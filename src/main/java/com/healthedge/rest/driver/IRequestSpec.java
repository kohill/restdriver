package com.healthedge.rest.driver;

import io.restassured.http.ContentType;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface IRequestSpec {

    /**
     * Specify a String request body (such as e.g. JSON or XML) that'll be sent with the request. This works for the
     * POST and PUT methods only. Trying to do this for the other http methods will cause an exception to be thrown.
     * <p>
     * Example of use:
     * <pre>
     * given().body("{ \"message\" : \"hello world\"}").when().post("/json").then().assertThat().body(equalTo("hello world"));
     * </pre>
     * This will POST a request containing JSON to "/json" and expect that the response body equals to "hello world".
     * </p>
     * <p/>
     * <p>
     *
     * @param body The body to send.
     * @return The request specification
     */
    IRequestSpec body(String body);

    /**
     * Specify file content that'll be sent with the request. This only works for the
     * POST, PATCH and PUT http method. Trying to do this for the other http methods will cause an exception to be thrown.
     * <p>
     * Example of use:
     * <pre>
     * InputStream myInputStream = ..
     * given().body(myInputStream).when().post("/json").then().content(equalTo("hello world"));
     * </pre>
     * This will POST a request containing <code>myInputStream</code> to "/json" and expect that the response content equals to "hello world".
     * </p>
     * <p/>
     * <p>
     *
     * @param body The content to send.
     * @return The request specification
     */
    IRequestSpec body(InputStream body);

    /**
     * Specify an Object request content that will automatically be serialized to JSON or XML and sent with the request.
     * If the object is a primitive or <a href="http://download.oracle.com/javase/6/docs/api/java/lang/Number.html">Number</a> the object will
     * be converted to a String and put in the request body. This works for the POST and PUT methods only.
     * Trying to do this for the other http methods will cause an exception to be thrown.
     * <p>
     * Example of use:
     * <pre>
     * Message message = new Message();
     * message.setMessage("My beautiful message");
     *
     * given().
     *         contentType("application/json").
     *         body(message).
     * when().
     *         post("/beautiful-message").
     * then().
     *         body(equalTo("Response to a beautiful message")).
     * </pre>
     * </p>
     * Since the content-type is "application/json" then REST Assured will automatically try to serialize the object using
     * <a href="https://github.com/FasterXML/jackson">Jackson</a> or <a href="https://github.com/google/gson">Gson</a> if they are
     * available in the classpath. If any of these frameworks are not in the classpath then an exception is thrown.
     * <br />
     * If the content-type is "application/xml" then REST Assured will automatically try to serialize the object using <a href="http://jaxb.java.net/">JAXB</a>
     * if it's available in the classpath. Otherwise an exception will be thrown.
     * <br />
     * If no request content-type is specified then REST Assured determine the parser in the following order:
     * <ol>
     * <li>Jackson</li>
     * <li>Gson</li>
     * <li>JAXB</li>
     * </ol>
     * <p>
     *
     * @param body The object to serialize and send with the request
     * @return The request specification
     */
    IRequestSpec body(Object body);

    /**
     * Specify the parameters that'll be sent with the request. This is done by specifying the parameters in name-value pairs, e.g:
     * <pre>
     * given().params("username", "John", "token", "1234").when().get("/parameters").then().assertThat().body(equalTo("username, token"));
     * </pre>
     * <p/>
     * This will send a GET request to "/parameters" with two parameters:
     * <ol>
     * <li>username=John</li>
     * <li>token=1234</li>
     * </ol>
     * and expect that the response body is equal to "username, token".
     *
     * @param firstParameterName      The name of the first parameter
     * @param firstParameterValue     The value of the first parameter
     * @param parameterNameValuePairs Additional parameters in name-value pairs.
     * @return The request specification
     */
    IRequestSpec params(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs);

    /**
     * Specify the parameters that'll be sent with the request as Map e.g:
     * <pre>
     * Map&lt;String, String&gt; parameters = new HashMap&lt;String, String&gt;();
     * parameters.put("username", "John");
     * parameters.put("token", "1234");
     * given().params(parameters).when().get("/cookie").then().assertThat().body(equalTo("username, token"));
     * </pre>
     * <p/>
     * This will send a GET request to "/cookie" with two parameters:
     * <ol>
     * <li>username=John</li>
     * <li>token=1234</li>
     * </ol>
     * and expect that the response body is equal to "username, token".
     *
     * @param parametersMap The Map containing the parameter names and their values to send with the request.
     * @return The request specification
     */
    IRequestSpec params(Map<String, ?> parametersMap);

    /**
     * Specify the query parameters that'll be sent with the request. Note that this method is the same as {@link #params(String, Object, Object...)}
     * for all http methods except for POST where {@link #params(String, Object, Object...)} sets the form parameters and this method sets the
     * query parameters.
     *
     * @param firstParameterName      The name of the first parameter
     * @param firstParameterValue     The value of the first parameter
     * @param parameterNameValuePairs The value of the first parameter followed by additional parameters in name-value pairs.
     * @return The request specification
     */
    IRequestSpec queryParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs);

    /**
     * Specify the query parameters that'll be sent with the request. Note that this method is the same as {@link #params(Map)}
     * for all http methods except for POST where {@link #params(Map)} sets the form parameters and this method sets the
     * query parameters.
     *
     * @param parametersMap The Map containing the parameter names and their values to send with the request.
     * @return The request specification
     */
    IRequestSpec queryParams(Map<String, ?> parametersMap);

    /**
     * Specify the form parameters that'll be sent with the request. Note that this method is the same as {@link #params(String, Object, Object...)}
     * for all http methods except for PUT where {@link #params(String, Object, Object...)} sets the query parameters and this method sets the
     * form parameters.
     *
     * @param firstParameterName      The name of the first parameter
     * @param firstParameterValue     The value of the first parameter
     * @param parameterNameValuePairs The value of the first parameter followed by additional parameters in name-value pairs.
     * @return The request specification
     */
    IRequestSpec formParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs);

    /**
     * Specify the form parameters that'll be sent with the request. Note that this method is the same as {@link #params(Map)}
     * for all http methods except for PUT where {@link #params(Map)} sets the query parameters and this method sets the
     * form parameters.
     *
     * @param parametersMap The Map containing the form parameter names and their values to send with the request.
     * @return The request specification
     * @see #formParams(java.util.Map)
     */
    IRequestSpec formParams(Map<String, ?> parametersMap);

    /**
     * Specify multiple path parameter name-value pairs. Path parameters are used to improve readability of the request path. E.g. instead
     * of writing:
     * <pre>
     * when().
     *        get("/item/"+myItem.getItemNumber()+"/buy/"+2).
     * then().
     *        statusCode(200).
     * </pre>
     * you can write:
     * <pre>
     * given().
     *         pathParams("itemNumber", myItem.getItemNumber(), "amount", 2).
     * when().
     *        get("/item/{itemNumber}/buy/{amount}").
     * then().
     *          statusCode(200);
     * </pre>
     * <p/>
     * which improves readability and allows the path to be reusable in many tests. Another alternative is to use:
     * <pre>
     * when().get("/item/{itemNumber}/buy/{amount}", myItem.getItemNumber(), 2).then().statusCode(200);
     * </pre>
     *
     * @param firstParameterName      The name of the first parameter
     * @param firstParameterValue     The value of the first parameter
     * @param parameterNameValuePairs Additional parameters in name-value pairs.
     * @return The request specification
     */
    IRequestSpec pathParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs);

    /**
     * Specify multiple path parameter name-value pairs. Path parameters are used to improve readability of the request path. E.g. instead
     * of writing:
     * <pre>
     * when().
     *        get("/item/"+myItem.getItemNumber()+"/buy/"+2).
     * then().
     *        statusCode(200);
     * </pre>
     * you can write:
     * <pre>
     * Map&lt;String,Object&gt; pathParams = new HashMap&lt;String,Object&gt;();
     * pathParams.add("itemNumber",myItem.getItemNumber());
     * pathParams.add("amount",2);
     *
     * given().
     *         pathParams(pathParams).
     * when().
     *        get("/item/{itemNumber}/buy/{amount}").
     * then().
     *          statusCode(200);
     * </pre>
     * <p/>
     * which improves readability and allows the path to be reusable in many tests. Another alternative is to use:
     * <pre>
     * when().get("/item/{itemNumber}/buy/{amount}", myItem.getItemNumber(), 2).then().statusCode(200);
     * </pre>
     *
     * @param parameterNameValuePairs A map containing the path parameters.
     * @return The request specification
     */
    IRequestSpec pathParams(Map<String, ?> parameterNameValuePairs);

    /**
     * @return The original Rest Driver Specification
     */
    <T> T getOriginalSpec();

    /**
     * Adding the baseUri Property from the IRequestSpec instead of using static field RestAssured.baseURI.
     *
     * @param baseUri The uri
     * @return the request specification
     */
    IRequestSpec baseUri(String baseUri);

    /**
     * Set the basePath property of the IRequestSpec instead of using static field RestAssured.basePath.
     *
     * @param basePath The base path
     * @return the request specification
     */
    IRequestSpec basePath(String basePath);

    /**
     * Perform a custom HTTP request to the pre-configured path (by default <code>http://localhost:8080</code>).
     *
     * @param method The HTTP method to use
     * @return The response of the request.
     */
    default <T> IResponse request(HttpMethod method) {
        return request(method, Object.class);
    }

    /**
     * Perform a custom HTTP request to the pre-configured path (by default <code>http://localhost:8080</code>).
     *
     * @param method               The HTTP method to use
     * @param expectedResponseType - Response Deserialization expected class type. If unknown Object.class need to be set.
     * @return The response of the request.
     */
    <T> IResponse request(HttpMethod method, Class<T> expectedResponseType);

    /**
     * Perform a custom HTTP request to the pre-configured path (by default <code>http://localhost:8080</code>).
     *
     * @param method The HTTP method to use
     * @return The response of the request.
     */
    default <T> IResponse request(String method) {
        return request(method, Object.class);
    }

    /**
     * Perform a custom HTTP request to the pre-configured path (by default <code>http://localhost:8080</code>).
     *
     * @param method               The HTTP method to use
     * @param expectedResponseType - Response Deserialization expected class type. If unknown Object.class need to be set.
     * @return The response of the request.
     */
    <T> IResponse request(String method, Class<T> expectedResponseType);

    /**
     * Specify the content type of the request.
     *
     * @param contentType The content type of the request
     * @return The request specification
     * @see ContentType
     */
    IRequestSpec contentType(String contentType);


    /**
     * Specify the content type of the request.
     *
     * @param contentType The content type of the request
     * @return The request specification
     * @see ContentType
     */
    IRequestSpec contentType(ContentType contentType);

    /**
     * Specify a multi-part specification. Use this method if you need to specify content-type etc.
     *
     * @param mediaTypes Multipart specification
     * @return The request specification
     */
    IRequestSpec accept(String mediaTypes);

    /**
     * Specify the headers that'll be sent with the request. This is done by specifying the headers in name-value pairs, e.g:
     * <pre>
     * given().headers("headerName1", "headerValue1", "headerName2", "headerValue2").then().expect().body(equalTo("something")).when().get("/headers");
     * </pre>
     * <p/>
     * This will send a GET request to "/headers" with two headers:
     * <ol>
     * <li>headerName1=headerValue1</li>
     * <li>headerName2=headerValue2</li>
     * </ol>
     * and expect that the response body is equal to "something".
     *
     * @param firstParameterName      The name of the first header
     * @param firstParameterValue     The value of the first header
     * @param parameterNameValuePairs Additional headers in name-value pairs.
     * @return The request specification
     */
    IRequestSpec headers(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs);

    /**
     * Specify the headers that'll be sent with the request as Map e.g:
     * <pre>
     * Map&lt;String, String&gt; headers = new HashMap&lt;String, String&gt;();
     * parameters.put("headerName1", "headerValue1");
     * parameters.put("headerName2", "headerValue2");
     * given().headers(headers).then().expect().body(equalTo("something")).when().get("/headers");
     * </pre>
     * <p/>
     * This will send a GET request to "/headers" with two headers:
     * <ol>
     * <li>headerName1=headerValue1</li>
     * <li>headerName2=headerValue2</li>
     * </ol>
     * and expect that the response body is equal to "something".
     *
     * @param parameterNameValuePairs The Map containing the header names and their values to send with the request.
     * @return The request specification
     */
    IRequestSpec headers(Map<String, ?> parameterNameValuePairs);


    /**
     * Specify the Authentication header as: 'Authorization=authTokenValue'. E.g.
     * Authorization=Basic tokenHash
     * Should be used if prefix is not 'Bearer'
     *
     * @param authTokenValue Token value
     * @return The request specification
     */
    IRequestSpec authHeader(String authTokenValue);

    /**
     * Use http basic authentication.
     *
     * @param userName The user name.
     * @param password The password.
     * @return The Request specification
     */
    IRequestSpec authBasic(String userName, String password);


    /**
     * Use http basic authentication like "Basic tokenHash", 'Basic' prefix will be added automatically
     *
     * @param token token
     * @return The Request specification
     */
    IRequestSpec authBasic(String token);


    /**
     * OAuth2 sign the request. Note that this currently does not wait for a WWW-Authenticate challenge before sending the the OAuth header
     * (so currently it's the same as preemptive oauth2 authentication. The reason why it's located here is to be backward compatible).
     * This assumes you've already generated an accessToken for the site you're targeting. The access token will be put in a header.
     *
     * @param accessToken The access token
     * @return The Request specification
     */
    IRequestSpec authOauth2(String accessToken);


    /**
     * Specify the Authentication header as a result of function which will return token depending on function name
     *
     * @param functionName Function name
     * @return The request specification
     */
    IRequestSpec authFunction(String functionName);

    /**
     * Determines if any of the auth types has been already applied to the specification
     *
     * @return 'True' if any auth type is applied to the specification, 'False' otherwise.
     */
    Boolean isAuthSet();

    /**
     * Specify a file to upload to the server using multi-part form data uploading.
     * It will assume that the control name is <tt>file</tt> and the mime-type is <tt>application/octet-stream</tt>.
     * If this is not what you want please use an overloaded method.
     *
     * @param file The file to upload
     * @return The request specification
     */
    IRequestSpec multiPart(File file);

    /**
     * Specify a file to upload to the server using multi-part form data uploading with a specific
     * control name. It will use the mime-type <tt>application/octet-stream</tt>.
     * If this is not what you want please use an overloaded method.
     *
     * @param file        The file to upload
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, File file);

    /**
     * Specify a file to upload to the server using multi-part form data uploading with a specific
     * control name and mime-type.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param file        The file to upload
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, File file, String mimeType);

    /**
     * Specify an object that will be serialized to JSON and uploaded to the server using multi-part form data
     * uploading with a specific control name. It will use mime-type <tt>application/json</tt>.
     * If this is not what you want please use an overloaded method.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param object      The object to serialize to JSON or XML and send to the server
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, Object object);

    /**
     * Specify an object that will be serialized and uploaded to the server using multi-part form data
     * uploading with a specific control name.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param object      The object to serialize to JSON or XML and send to the server
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, Object object, String mimeType);

    /**
     * Specify an object that will be serialized and uploaded to the server using multi-part form data
     * uploading with a specific control name.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param filename    The name of the content you're uploading
     * @param object      The object to serialize to JSON or XML and send to the server
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String filename, Object object, String mimeType);

    /**
     * Specify a byte-array to upload to the server using multi-part form data.
     * It will use the mime-type <tt>application/octet-stream</tt>. If this is not what you want please use an overloaded method.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param fileName    The name of the content you're uploading
     * @param bytes       The bytes you want to send
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String fileName, byte[] bytes);

    /**
     * Specify a byte-array to upload to the server using multi-part form data.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param fileName    The name of the content you're uploading
     * @param bytes       The bytes you want to send
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String fileName, byte[] bytes, String mimeType);

    /**
     * Specify an inputstream to upload to the server using multi-part form data.
     * It will use the mime-type <tt>application/octet-stream</tt>. If this is not what you want please use an overloaded method.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param fileName    The name of the content you're uploading
     * @param stream      The stream you want to send
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String fileName, InputStream stream);

    /**
     * Specify an inputstream to upload to the server using multi-part form data.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param fileName    The name of the content you're uploading
     * @param stream      The stream you want to send
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String fileName, InputStream stream, String mimeType);

    /**
     * Specify a string to send to the server using multi-part form data.
     * It will use the mime-type <tt>text/plain</tt>. If this is not what you want please use an overloaded method.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param contentBody The string to send
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String contentBody);

    /**
     * Specify a string to send to the server using multi-part form data with a specific mime-type.
     *
     * @param controlName Defines the control name of the body part. In HTML this is the attribute name of the input tag.
     * @param contentBody The string to send
     * @param mimeType    The mime-type
     * @return The request specification
     */
    IRequestSpec multiPart(String controlName, String contentBody, String mimeType);
}
