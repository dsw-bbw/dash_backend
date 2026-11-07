package ch.dsa.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FindCollect {
    public static Map<String, Object> collect(Object target) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Method m : findCollects(target.getClass())) {
            String name = m.getAnnotation(Collect.class).value();
            try {
                m.setAccessible(true);
                map.put(name, m.invoke(target));
            } catch (Exception e) {
                map.put(name, null);
                System.err.println(name + ":" + e.getCause());
            }
        }
        return map;
    }

    private static List<Method> findCollects(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Collect.class))
                .toList();
    }
}
