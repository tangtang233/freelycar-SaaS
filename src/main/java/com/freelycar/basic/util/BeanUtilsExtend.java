package com.freelycar.basic.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author tangwei
 */
public class BeanUtilsExtend extends BeanUtils {

    public BeanUtilsExtend() {
    }

    public static void copyNotNullProperties(Object source, Object target) {
        copyNotNullProperties(source, target, (Class)null);
    }

    private static void copyNotNullProperties(Object source, Object target, Class<?> editable) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }

            actualEditable = editable;
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        PropertyDescriptor[] var5 = targetPds;
        int var6 = targetPds.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            PropertyDescriptor targetPd = var5[var7];
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }

                        Object value = readMethod.invoke(source);
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }

                        if (value != null) {
                            writeMethod.invoke(target, value);
                        }
                    } catch (Throwable var13) {
                        throw new FatalBeanException("Could not copy properties from source to target", var13);
                    }
                }
            }
        }

    }

    public static void copyPropertiesByParameterMap(Object source, Object target, Map parameterMap, String prefix) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        PropertyDescriptor[] var6 = targetPds;
        int var7 = targetPds.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            PropertyDescriptor targetPd = var6[var8];
            String propertyname = targetPd.getName();
            if (parameterMap.containsKey(prefix + propertyname) && targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }

                        Object value = readMethod.invoke(source);
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }

                        writeMethod.invoke(target, value);
                        if (value == null) {
                            System.err.println("警告=========================>>>>" + targetPd.getName() + "字段写入了null值，如果该字段在数据库中设置了不允许为null，这将导致未知的错误");
                        }
                    } catch (Throwable var15) {
                        throw new FatalBeanException("Could not copy properties from source to target", var15);
                    }
                }
            }
        }

    }

    public static <T> T copyMapToProperties(Map<String, Object> map, Object obj) {
        String setterName = null;
        String fieldName = null;
        String tempFieldName = null;
        Class<? extends Object> clazz = obj.getClass();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator var7 = set.iterator();

        while(var7.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var7.next();
            fieldName = (String)entry.getKey();
            tempFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            setterName = "set" + tempFieldName;

            try {
                Field[] fields = clazz.getDeclaredFields();
                Field[] var10 = fields;
                int var11 = fields.length;

                for(int var12 = 0; var12 < var11; ++var12) {
                    Field f = var10[var12];
                    if (fieldName.toLowerCase().equals(f.getName().toLowerCase())) {
                        Method setterMethod = clazz.getMethod(setterName, f.getType());
                        Object value = entry.getValue();
                        String str = value == null ? null : String.valueOf(value);
                        if (str != null) {
                            if (f.getType().equals(String.class)) {
                                value = str;
                            } else if (f.getType().equals(Integer.class)) {
                                value = Integer.valueOf(str);
                            } else if (f.getType().equals(Long.class)) {
                                value = Long.valueOf(str);
                            } else if (f.getType().equals(Date.class)) {
                                value = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(str);
                            } else {
                                if (!f.getType().equals(BigDecimal.class)) {
                                    System.out.println(f.getType().getName() + " 为暂不考虑的赋值类型，已经忽略=========================");
                                    break;
                                }

                                value = new BigDecimal(str);
                            }
                        }

                        setterMethod.invoke(obj, value);
                        break;
                    }
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }
        }

        return (T) obj;
    }

    public static <T> T copyMapToProperties(Map<String, Object> map, Object obj, Map<String, String> dateFormatMap) {
        String setterName = null;
        String fieldName = null;
        String tempFieldName = null;
        Class<? extends Object> clazz = obj.getClass();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator var8 = set.iterator();

        while(var8.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var8.next();
            fieldName = (String)entry.getKey();
            tempFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            setterName = "set" + tempFieldName;

            try {
                Field[] fields = clazz.getDeclaredFields();
                Field[] var11 = fields;
                int var12 = fields.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    Field f = var11[var13];
                    if (fieldName.toLowerCase().equals(f.getName().toLowerCase())) {
                        Method setterMethod = clazz.getMethod(setterName, f.getType());
                        Object value = entry.getValue();
                        String str = value == null ? null : String.valueOf(value);
                        if (str != null) {
                            if (f.getType().equals(String.class)) {
                                value = str;
                            } else if (f.getType().equals(Integer.class)) {
                                value = Integer.valueOf(str);
                            } else if (f.getType().equals(Long.class)) {
                                value = Long.valueOf(str);
                            } else if (f.getType().equals(Date.class)) {
                                String formate = (String)dateFormatMap.get(clazz.getSimpleName().toLowerCase() + "." + fieldName.toLowerCase());
                                value = (new SimpleDateFormat(formate)).parse(str);
                            } else {
                                if (!f.getType().equals(BigDecimal.class)) {
                                    System.out.println(f.getType().getName() + " 为暂不考虑的赋值类型，已经忽略=========================");
                                    break;
                                }

                                value = new BigDecimal(str);
                            }
                        }

                        setterMethod.invoke(obj, value);
                        break;
                    }
                }
            } catch (Exception var19) {
                var19.printStackTrace();
            }
        }

        return (T) obj;
    }
}
