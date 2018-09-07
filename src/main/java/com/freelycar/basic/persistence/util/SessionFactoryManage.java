package com.freelycar.basic.persistence.util;

import org.hibernate.SessionFactory;

import java.util.Map;

/**
 * @author tangwei
 */
public class SessionFactoryManage {
    private Map<String, SessionFactory> sessionFactoryMap;

    public SessionFactoryManage() {
    }

    public Map<String, SessionFactory> getSessionFactoryMap() {
        return this.sessionFactoryMap;
    }

    public void setSessionFactoryMap(Map<String, SessionFactory> sessionFactoryMap) {
        this.sessionFactoryMap = sessionFactoryMap;
    }
}
