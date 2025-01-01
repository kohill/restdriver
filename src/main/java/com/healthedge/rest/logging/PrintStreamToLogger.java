package com.healthedge.rest.logging;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A wrapper class which takes a logger as constructor argument and offers a PrintStream whose flush
 * method writes the written content to the supplied logger (debug level).
 *
 * @author Anton Perapecha
 */
public class PrintStreamToLogger {
    /**
     * Logger for this class
     */
    private final Logger logger;
    private PrintStream printStream;

    /**
     * Constructor
     *
     * @param logger
     */
    public PrintStreamToLogger(Logger logger) {
        this.logger = logger;


    }

    /**
     * @return printStream
     */
    public PrintStream getPrintStream() {
        if (printStream == null) {
            OutputStream output = new OutputStream() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);

                @Override
                public void write(int b) {
                    baos.write(b);
                }

                @Override
                public void flush() {
                    if (StringUtils.isNotBlank(baos.toString())) {
                        logger.info("\n" + this.baos.toString());
                    }
                    baos = new ByteArrayOutputStream(8192);
                }
            };
            printStream = new PrintStream(output, true);
        }
        return printStream;
    }
}
