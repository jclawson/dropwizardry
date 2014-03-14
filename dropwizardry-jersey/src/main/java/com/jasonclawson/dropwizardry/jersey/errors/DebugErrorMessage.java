package com.jasonclawson.dropwizardry.jersey.errors;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import com.google.common.base.Preconditions;

import lombok.Getter;

@Getter
public class DebugErrorMessage  {
    private final long errorId;
    private final ExceptionBean exception;
    
    public DebugErrorMessage(long errorId, Throwable t) {
        Preconditions.checkNotNull(t);
        this.errorId = errorId;
        exception = new ExceptionBean(t);
    }
    
    @Override
    public String toString() {
        StringWriter str = new StringWriter();
        try {
            exception.printStackTrace(str);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write StackTrace for errorId '"+errorId+"'", e);
        }
        return LoggingDebugExceptionMapper.formatErrorMessage(errorId)+" "+str.toString();
    }
    
//    private String formatErrorMessage(long id) {
//        return String.format("There was an error processing your request. It has been logged (ID %016x).", id);
//    }
    
    @Getter
    protected static class ExceptionBean {
        private final String clazzName;
        private final String message;
        private final ExceptionBean cause;
        private final StackTraceElement[] stack;
        
        public ExceptionBean(Throwable t) {
            clazzName = t.getClass().getSimpleName();
            message = t.getMessage();
            Throwable cause = t.getCause();
            if(cause != null) {
                this.cause = new ExceptionBean(cause);
            } else {
                this.cause = null;
            }
            stack = t.getStackTrace();
        }
        
        private static final String CAUSE_CAPTION = "Caused by: ";
        
        @Override
        public String toString() {
            String s = clazzName;
            return (message != null) ? (s + ": " + message) : s;

        }
        
        protected void printStackTrace(Writer s) throws IOException {
            // Guard against malicious overrides of Throwable.equals by
            // using a Set with identity equality semantics.
            Set<ExceptionBean> dejaVu =
                Collections.newSetFromMap(new IdentityHashMap<ExceptionBean, Boolean>());
            dejaVu.add(this);

                // Print our stack trace
                s.write(this.toString());
                s.write("\n");
                StackTraceElement[] trace = getStack();
                for (StackTraceElement traceElement : trace) {
                    s.write("\tat " + traceElement);
                    s.write("\n");
                }

                // Print cause, if any
                ExceptionBean ourCause = getCause();
                if (ourCause != null)
                    ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, "", dejaVu);
        }
        
        /**
         * Print our stack trace as an enclosed exception for the specified
         * stack trace.
         * @throws IOException 
         */
        private void printEnclosedStackTrace(Writer s,
                                             StackTraceElement[] enclosingTrace,
                                             String caption,
                                             String prefix,
                                             Set<ExceptionBean> dejaVu) throws IOException {
            
            if (dejaVu.contains(this)) {
                s.write("\t[CIRCULAR REFERENCE:" + this + "]");
                s.write("\n");
            } else {
                dejaVu.add(this);
                // Compute number of frames in common between this and enclosing trace
                StackTraceElement[] trace = getStack();
                int m = trace.length - 1;
                int n = enclosingTrace.length - 1;
                while (m >= 0 && n >=0 && trace[m].equals(enclosingTrace[n])) {
                    m--; n--;
                }
                int framesInCommon = trace.length - 1 - m;

                // Print our stack trace
                s.write(prefix + caption + this);
                s.write("\n");
                for (int i = 0; i <= m; i++) {
                    s.write(prefix + "\tat " + trace[i]);
                    s.write("\n");
                }
                
                if (framesInCommon != 0) {
                    s.write(prefix + "\t... " + framesInCommon + " more");
                    s.write("\n");
                }

                // Print cause, if any
                ExceptionBean ourCause = getCause();
                if (ourCause != null)
                    ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, prefix, dejaVu);
            }
        }
    }
}
