package utils.logging;

import utils.WriterOutputStream;

import java.io.PrintStream;
import java.io.StringWriter;
public class LogFactory {
    private static final ThreadLocal<StringWriter> LOG_WRITER = new ThreadLocal<>();
    private static final ThreadLocal<PrintStream> LOG_STREAM = new ThreadLocal<>();

    public static void init() {
        if (LOG_WRITER.get() == null || LOG_STREAM.get() == null) {
            StringWriter writer = new StringWriter();
            PrintStream stream = new PrintStream(new WriterOutputStream(writer), true);
            LOG_WRITER.set(writer);
            LOG_STREAM.set(stream);
        }
    }

    public static StringWriter getWriter() {
        return LOG_WRITER.get();
    }

    public static PrintStream getStream() {
        return LOG_STREAM.get();
    }

    public static void clear() {
        LOG_WRITER.remove();
        LOG_STREAM.remove();
    }
}
