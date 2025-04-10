package com.companyname.rest.data;

import com.companyname.rest.driver.Header;

import java.util.List;
import java.util.Map;

public interface IDataParser {

    String parse(String value);

    String parseCache(String value, Map<String, String> cachedData);

    String parseHeadersCache(String value, Map<String, List<Header>> cachedData);
}
