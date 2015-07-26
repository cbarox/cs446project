package ca.uwaterloo.mapapp.server;

import java.lang.reflect.Field;

/**
 * Created by cjbarrac
 * 7/26/15
 */
public class MagicLogger {
    private static final int MAGIC_THREAD_DEPTH = 3;

    public static void printFields(Object o) {
        String className = o.getClass().getName();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                System.out.printf("%s.%s: %s%n", className, field.getName(), field.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getCallerInfo() {
        StackTraceElement stackTraceElements = Thread.currentThread().getStackTrace()[MAGIC_THREAD_DEPTH];
        return "[" + stackTraceElements.getClassName() + "][" + stackTraceElements.getMethodName() + "] ";
    }

    public static void log(String format, Object... objects) {
        final String logFormat = getCallerInfo() + format + "%n";
        System.out.printf(logFormat, objects);
    }

}
