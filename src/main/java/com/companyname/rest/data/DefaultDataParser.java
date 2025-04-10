package com.companyname.rest.data;

import com.companyname.rest.driver.Header;
import com.mifmif.common.regex.Generex;
import io.restassured.path.json.JsonPath;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link IDataParser} interface.
 * Supported markup (values in parentheses are optional):
 * <ul>
 * <li>today(delta)(:format) - today's date + specified delta (m/H/d/M/y for minutes/hours/days/months/years) in specified format.
 * 		Examples: $&lt;today&gt;, $&lt;today+1M&gt;, $&lt;today-1y+2M-3d+4H&gt;, $&lt;today+4d:MM/dd/yyyy H:mm a&gt;.
 *      @see DateTimeFormatter#format(java.time.temporal.TemporalAccessor) for supported formats.
 * </li>
 * <li>BOM(delta):(format) - beginning of current month + delta in specified format.
 *      Examples: $&lt;BOM&gt;, $&lt;BOM+1M&gt;, $&lt;BOM-1y+2M-3d+4H&gt;, $&lt;BOM+4d:MM/dd/yyyy H:mm a&gt;.
 * </li>
 * <li>BONM(delta):(format) - beginning of next month + delta in specified format.
 *      Examples: $&lt;BONM&gt;, $&lt;BONM+1M&gt;, $&lt;BONM-1y+2M-3d+4H&gt;, $&lt;BONM+4d:MM/dd/yyyy H:mm a&gt;.
 * </li>
 * <li>BOY(delta):(format) - beginning of current year + delta in specified format.
 *      Examples: $&lt;BOY&gt;, $&lt;BOY+1M&gt;, $&lt;BOY-1y+2M-3d+4H&gt;, $&lt;BOY+4d:MM/dd/yyyy H:mm a&gt;.
 * </li>
 * <li>BONY(delta):(format) - beginning of next year + delta in specified format.
 *      Examples: $&lt;BONY&gt;, $&lt;BONY+1M&gt;, $&lt;BONY-1y+2M-3d+4H&gt;, $&lt;BONY+4d:MM/dd/yyyy H:mm a&gt;.
 * </li>
 * <li>rx:(regular expression) - random string matching specified regular expression.
 *      Examples: $&lt;rx:Harry-[A-Z]{5}&gt;, Jacob$&lt;rx:[a-zA-Z]{3}&gt;, $&lt;rx:\d{10}&gt;.
 *  </li>
 *  <li>cache:(step_name):(GPath) - gets value or json block from cached response from previous steps.
 *  <a href="http://docs.groovy-lang.org/latest/html/documentation/#_gpath">GPath documentation</a>
 *      Examples: $&lt;cache:step1.CreateQuote_QuoteType_Quick:quote.quoteNumber&gt;.
 *  </li>
 *  <li>cache_headers:(step_name):(GPath) - gets value or json block from cached response Headers from previous steps.
 *  <a href="http://docs.groovy-lang.org/latest/html/documentation/#_gpath">GPath documentation</a>
 *  Examples: $&lt;cache_headers:step1.CreateQuote_QuoteType_Quick:quote.quoteNumber&gt;.
 *  </li>
 *  <li>testdata:(file_path):(GPath) - gets json value or block from another file.
 *  <a href="http://docs.groovy-lang.org/latest/html/documentation/#_gpath">GPath documentation</a>
 *  Examples: $&lt;testdata:default/restServiceName/rest_example1:TestData1&gt;.
 *  </li>
 * </ul>
 */
public class DefaultDataParser implements IDataParser {
    protected static Pattern expressionPattern;

    protected static String expressionRegexp = "(\"?)\\$<(\\w+)([^>]*)>(\"?)";
    protected static String datePattern = "MM/dd/yyyy";

    static {
        expressionPattern = Pattern.compile(expressionRegexp);
    }

    protected Map<String, Function<String, String>> processors = new HashMap<>();

    public DefaultDataParser() {
        registerProcessor("today", args -> processDateTime(LocalDateTime.now(), args));
        registerProcessor("BOM", args -> processDateTime(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()), args));
        registerProcessor("BOY", args -> processDateTime(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()), args));
        registerProcessor("BONM", args -> processDateTime(LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextMonth()), args));
        registerProcessor("BONY", args -> processDateTime(LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextYear()), args));
        registerProcessor("rx", s -> new Generex(s.replaceFirst("^:", "")).random());
        registerProcessor("testdata", s -> processTestData(s.replaceFirst("^:", "")));
    }

    /**
     * Register markup processors
     *
     * @param exprType expression type (e.g. "today" in "$&lt;today&gt" markup string)
     * @param proc     expression processor
     */
    public void registerProcessor(String exprType, Function<String, String> proc) {
        processors.put(exprType, proc);
    }

    @Override
    public String parse(String value) {
        return parseText(value, processors);
    }

    @Override
    public String parseCache(String value, Map<String, String> cachedData) {
        Map<String, Function<String, String>> processors = new HashMap<>();
        processors.put("cache", args -> processCachedData(args.replaceFirst("^:", ""), cachedData));
        return parseText(value, processors);

    }

    @Override
    public String parseHeadersCache(String value, Map<String, List<Header>> cachedData) {
        processors.put("cache_headers", args -> processHeadersCache(args.replaceFirst("^:", ""), cachedData));
        return parseText(value, processors);
    }


    protected String parseText(String value, Map<String, Function<String, String>> processors) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = expressionPattern.matcher(value);
        while (matcher.find()) {
            String expressionType = matcher.group(2).trim();
            String expressionArgs = matcher.group(3).replace("\\\\", "\\").trim();
            String defaultValue = matcher.group().replace("\\\\", "\\").trim();
            if (processors.containsKey(expressionType)) {
                String processedValue = processors.get(expressionType).apply(expressionArgs);
                if ((expressionType.equals("cache") || expressionType.equals("testdata"))
                        && (processedValue.startsWith("{") || processedValue.startsWith("["))) {
                    matcher.appendReplacement(sb, processedValue);
                } else {
                    matcher.appendReplacement(sb, "");
                    sb.append(matcher.group(1)).append(processedValue).append(matcher.group(4));
                }
            } else {
                matcher.appendReplacement(sb, "");
                sb.append(defaultValue);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected String processDateTime(LocalDateTime dateTime, String argString) {
        String[] splitArgs = argString.split(":", 2);
        Pattern dp = Pattern.compile("([+-])(\\d+)(\\w)");
        Matcher m = dp.matcher(splitArgs[0]);
        while (m.find()) {
            int signum = m.group(1).equals("+") ? 1 : -1;
            long val = Long.parseLong(m.group(2));
            TemporalUnit unit;
            switch (m.group(3)) {
                case "m":
                    unit = ChronoUnit.MINUTES;
                    break;
                case "H":
                    unit = ChronoUnit.HOURS;
                    break;
                case "d":
                    unit = ChronoUnit.DAYS;
                    break;
                case "M":
                    unit = ChronoUnit.MONTHS;
                    break;
                case "y":
                    unit = ChronoUnit.YEARS;
                    break;
                default:
                    throw new IllegalArgumentException("Cannot parse " + m.group(3) + " in " + argString + " as temporal unit");
            }
            dateTime = dateTime.plus(val * signum, unit);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern((splitArgs.length == 2) ? splitArgs[1] : datePattern);
        return dateTime.format(fmt);
    }

    protected String processCachedData(String argString, Map<String, String> cachedData) {
        List<String> splitArgs = Arrays.asList(argString.split(":", 2));
        String pathKey = splitArgs.get(0).trim();
        if (cachedData.isEmpty()) {
            throw new RuntimeException("Cached data empty, check if cashed data variable is not used in the first step or response successfully received in previous step: " + pathKey);
        }
        if (cachedData.containsKey(pathKey) && !cachedData.get(pathKey).isEmpty()) {
            return readJson(cachedData.get(pathKey), splitArgs.get(1).trim());
        } else {
            throw new RuntimeException("No such data cashed or path is not built properly: " + pathKey);
        }
    }

    protected String processHeadersCache(String argString, Map<String, List<Header>> cachedData) {
        List<String> splitArgs = Arrays.asList(argString.split(":", 2));
        String pathKey = splitArgs.get(0).trim();
        String name = splitArgs.get(1).trim();
        if (cachedData.isEmpty()) {
            throw new RuntimeException("Cached data empty, check if cashed data variable is not used in the first step or response successfully received in previous step: " + pathKey);
        }
        if (!cachedData.containsKey(pathKey) || cachedData.get(pathKey).stream().noneMatch(header -> header.getName().equals(name))) {
            throw new RuntimeException("No such data cashed or path is not built properly: " + pathKey);
        }

        return cachedData.get(pathKey).stream().filter(header -> header.getName().equals(name)).findFirst().orElse(null).getValue();
    }


    protected String processTestData(String argString) {
        List<String> splitArgs = Arrays.asList(argString.split(":", 2));

        if (splitArgs.size() == 2) {
            String td = new RestTestData().fromFile(splitArgs.get(0).trim()).asString();
            return readJson(td, splitArgs.get(1).trim());
        }
        throw new RuntimeException("Wrong reference " + argString
                + ". Only 2 level reference level is allowed. Example: 'key': '$<testdata:path_to_file:path_to_jsonElement>'");
    }

    protected String readJson(String json, String path) {
        JsonPath jsonPath = new JsonPath(json);
        String result = RestTestData.gson.toJson(jsonPath.get(path), Object.class);
        if (!result.isEmpty() && result.startsWith("\"")) {
            result = result.substring(1, result.length() - 1);
        }
        return result;
    }
}
