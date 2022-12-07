package com.qaq.demo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ObjectUtils {

    /**
     * 获取对象指定字段的值
     * 
     * @param o
     * @param field
     * @return
     */
    public static Object getFieldValue(Object o, Field field) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);

        try {
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean setFieldValue(Object o, Field field, Object value) {
        String fieldName = field.getName();
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String setter = "set" + firstLetter + fieldName.substring(1);

        try {
            Method method = o.getClass().getMethod(setter, field.getType());
            method.invoke(o, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> Integer getChangedCount(T oldObject, T newObject) {
        Integer changedCount = 0;
        Field[] fields = newObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object newValue = getFieldValue(newObject, field);
            Object oldValue = getFieldValue(oldObject, field);
            if (!Objects.deepEquals(newValue, oldValue)) {
                changedCount++;
            }
        }
        return changedCount;
    }
}
