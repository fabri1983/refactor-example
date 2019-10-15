package org.fabri1983.refactorexample.joblogger.enhanced.handler;

import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Custom version of a java.util.logging.ConsoleHandler which accepts injection of any output stream. 
 * System.out, System.err, any instance of OutputStream.
 */
public class CustomConsoleHandler extends StreamHandler {

	/**
     * Create a {@code CustomConsoleHandler} for an {@code OutputStream}.
     * <p>
     * The {@code CustomConsoleHandler} is configured based on
     * {@code LogManager} properties (or their default values).
     *
     */
    public CustomConsoleHandler(final OutputStream out) {
        // configure with specific defaults for ConsoleHandler.
    	// Defaults to Level.INFO.
        super(out, new SimpleFormatter());
    }

    /**
     * Publish a {@code LogRecord}.
     * <p>
     * The logging request was made initially to a {@code Logger} object,
     * which initialized the {@code LogRecord} and forwarded it here.
     *
     * @param  record  description of the log event. A null record is
     *                 silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    /**
     * Override {@code StreamHandler.close} to do a flush but not
     * to close the output stream.  That is, we do <b>not</b>
     * close {@code System.err}.
     */
    @Override
    public void close() {
        flush();
    }
    
}
