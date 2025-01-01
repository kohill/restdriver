package com.healthedge.rest.data;

import com.healthedge.rest.driver.Header;

import java.util.List;
import java.util.Map;

public interface IDataParser {

    String parse(String value);

    String parseCache(String value, Map<String, String> cachedData);

    String parseHeadersCache(String value, Map<String, List<Header>> cachedData);
}
