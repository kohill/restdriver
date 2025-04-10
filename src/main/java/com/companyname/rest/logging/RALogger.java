package com.companyname.rest.logging;

import com.companyname.config.props.PropertyReader;
import com.companyname.rest.config.RestConstants;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class RALogger {
    private static final Logger LOG = LogManager.getLogger(RALogger.class);
    private static final String MESSAGE_SEPARATOR = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
    private static final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private RALogger() {
    }

    public static void addToLog(String logMessage) {
        try {
            byteArrayOutputStream.write((logMessage + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logOutput() {
        StringBuilder log = new StringBuilder(byteArrayOutputStream.toString());

        if (log.length() != 0) {
            for (String line : log.toString().split("\n")) {
                LOG.info(line.replace("\r", ""));
            }
        }

        byteArrayOutputStream.reset();
    }

    public static class LogFilter implements Filter {

        @Override
        public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {

            Response response = null;

            try {

                // send the request
                response = ctx.next(requestSpec, responseSpec);

            } catch (Exception e) {
                addToLog("Something is wrong with the Response/Could not connect to the environment");
                addToLog(e.getMessage());
                //throw new RuntimeException("Something is wrong with the Response/Could not connect to the environment");
            } finally {
                // print the request
                RequestPrinter.print(requestSpec, requestSpec.getMethod(), requestSpec.getURI(), LogDetail.ALL, requestSpec.getConfig().getLogConfig().blacklistedHeaders(),
                        new PrintStream(byteArrayOutputStream), PropertyReader.getProperty(RestConstants.Properties.LOG_PRETTY_JSON, true));
                // add an empty line
                addToLog("\n");
                if (response != null) {
                    // print the response
                    ResponsePrinter.print(response, response, new PrintStream(byteArrayOutputStream), LogDetail.ALL, PropertyReader.getProperty(RestConstants.Properties.LOG_PRETTY_JSON, true),
                            requestSpec.getConfig().getLogConfig().blacklistedHeaders());
                }
                // add the message separator
                addToLog(MESSAGE_SEPARATOR);

                // print the log
                logOutput();
            }

            return response;
        }
    }
}
