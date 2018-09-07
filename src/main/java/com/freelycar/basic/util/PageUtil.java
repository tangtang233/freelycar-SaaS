package com.freelycar.basic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author tangwei
 */
public class PageUtil {
    private static final Logger logger = LoggerFactory.getLogger(PageUtil.class);
    private static Map<Class<?>, Field> classPKMap = new WeakHashMap();

    public PageUtil() {
    }

    public static int getPageStart(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

    public static int getPageStart(int totalCount, int pageNumber, int pageSize) {
        int start = (pageNumber - 1) * pageSize;
        if (start >= totalCount) {
            start = 0;
        }

        return start;
    }
}
