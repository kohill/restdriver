package examples;

public class TestDataParserExamples {

    //BOM(delta):(format) - beginning of current month + delta in specified format.
    //Examples: $<BOM>, $<BOM+1M>, $<BOM-1y+2M-3d+4H>, $<BOM+4d:MM/dd/yyyy H:mm a>.

    //BONM(delta):(format) - beginning of next month + delta in specified format.
    // Examples: $<BONM>, $<BONM+1M>, $<BONM-1y+2M-3d+4H>, $<BONM+4d:MM/dd/yyyy H:mm a>.

    //BOY(delta):(format) - beginning of current year + delta in specified format.
    //Examples: $<BOY>, $<BOY+1M>, $<BOY-1y+2M-3d+4H>, $<BOY+4d:MM/dd/yyyy H:mm a>.

    //BONY(delta):(format) - beginning of next year + delta in specified format.
    //Examples: $<BONY>, $<BONY+1M>, $<BONY-1y+2M-3d+4H>, $<BONY+4d:MM/dd/yyyy H:mm a>.

    //rx:(regular expression) - random string matching specified regular expression.
    //Examples: $<rx:Harry-[A-Z]{5}>, Jacob$<rx:[a-zA-Z]{3}>, $<rx:\d{10}>.

    //GPath documentation: http://docs.groovy-lang.org/latest/html/documentation/#_gpath
    //cache:(step_name):(GPath) - gets value or json block from cached response of previous steps.
    // Examples: $<cache:step1.CreateQuote_QuoteType_Quick:quote.quoteNumber>.

    //testdata:(file_path):(GPath) - gets json value or block from another file.
    // Examples: $<testdata:default/restServiceName/rest_example1:TestData1>.
}
