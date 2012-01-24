/*
 *  Copyright 2012 Mathieu ANCELIN
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.restmvc.utils;

import java.io.PrintStream;

/**
 * Utility class for messages printing in printstream.
 * 
 * @author Mathieu ANCELIN
 */
public final class SimpleLogger {

    private static final int NORMAL = 0;
    private static final int BRIGHT = 1;
    private static final int FOREGROUND_RED = 31;
    private static final int FOREGROUND_YELLOW = 33;
    private static final int FOREGROUND_BLUE = 34;
    private static final int FOREGROUND_CYAN = 36;
    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;
    private static final String FATAL_COLOUR = PREFIX + BRIGHT + SEPARATOR + FOREGROUND_RED + SUFFIX;
    private static final String ERROR_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_RED + SUFFIX;
    private static final String WARN_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_YELLOW + SUFFIX;
    private static final String INFO_COLOUR = PREFIX + SUFFIX;
    private static final String DEBUG_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_BLUE + SUFFIX;
    private static final String TRACE_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_CYAN + SUFFIX;
    private static final String TRACE_PREFIX = "[TRACE] ";

    private static boolean trace = false;
    private static boolean colors = false;

    private static PrintStream errorOut = System.err;
    private static PrintStream infoOut = System.out;
    private static PrintStream traceOut = System.out;

    private SimpleLogger() {}

    public static void enableTrace(boolean trace) {
        SimpleLogger.trace = trace;
    }

    public static void enableColors(boolean colors) {
        SimpleLogger.colors = colors;
    }

    public static void setErrorOut(PrintStream errorOut) {
        SimpleLogger.errorOut = errorOut;
    }

    public static void setInfoOut(PrintStream infoOut) {
        SimpleLogger.infoOut = infoOut;
    }

    public static void setTraceOut(PrintStream traceOut) {
        SimpleLogger.traceOut = traceOut;
    }

    public static void error(String message, Object... printable) {
        print(ERROR_COLOUR, errorOut, message, printable);
    }

    public static void info(String message, Object... printable) {
        print(INFO_COLOUR, infoOut, message, printable);
    }

    public static void trace(String message, Object... printable) {
        if (trace) {
            print(TRACE_COLOUR, traceOut, TRACE_PREFIX + message, printable);
        }
    }

    private static void print(String color, PrintStream out, String message, Object... printable) {
        StringBuilder print = new StringBuilder();
        if (colors) {
            print.append(color);
        }
        if (message.contains("{}") && printable != null && printable.length > 0) {
            String[] parts = message.split("\\{\\}");
            int i = 0;
            for (String part : parts) {
                print.append(part);
                if (i < printable.length) {
                    print.append(printable[i]);
                    i++;
                }
            }
            if (i < printable.length) {
                for (int j = i; j < printable.length; j++) {
                    print.append(printable[j]);
                    print.append("\n");
                }
            }
        } else {
            print.append(message);
            if (printable != null && printable.length > 0) {
                print.append("\n");
            }
            for (Object o : printable) {
                print.append(o.toString());
                print.append("\n");
            }
        }
        if (colors) {
            print.append(END_COLOUR);
        }
        out.println(print.toString());
    }

    public static class Duration {

        private final long start;
        private final long stop;

        public Duration(long start, long stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            double time =  (stop - start);
            return String.format("%.1f", time);
        }
    }
}
