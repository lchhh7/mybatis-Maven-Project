package com.jinjin.jintranet.common.util;

import org.slf4j.Logger;

public class LoggingUtils {
    public static void loggingStackTrace(Exception e, Logger logger) {
        StackTraceElement[] ste = e.getStackTrace();
        String className  = ste[0].getClassName();
        StringBuffer sb = new StringBuffer(ste[0].getMethodName() + "()<line : " + ste[0].getLineNumber() + ">");
        String fileName = ste[0].getFileName();

        logger.error(">>> ERROR ON {} <<<", fileName);
        logger.error("{} >> {}", className, sb);
        logger.error("MSG : {}", e.getMessage());
        logger.error("CUZ : {}", e.getCause());
    }

    public static void loggingCurrentMethod(Logger logger, Object...objects) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();

        StringBuffer sb = new StringBuffer();
        for (Object o : objects) {
            sb.append(o.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        logger.info(className + "." + methodName + " : " + sb);
    }

    public static void loggingCurrentMethod(Logger logger) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();

        logger.info(className + "." + methodName);
    }
}