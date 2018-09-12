package com.freelycar.basic.persistence.model;

import com.freelycar.basic.persistence.util.SpringContextUtil;
import com.freelycar.basic.persistence.util.StringUtil;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

public class CurrentDataBase {
    public static final String SQLSERVER_DIALECT = "org.hibernate.dialect.SQLServerDialect";
    public static final String ORACLE_DIALECT = "org.hibernate.dialect.Oracle10gDialect";
    public static final String MYSQL_DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static String currentDialect;

    public CurrentDataBase() {
    }

    public static String getCurrentDialect() {
        return currentDialect;
    }

    static {
        LocalSessionFactoryBean localSessionFactoryBean = (LocalSessionFactoryBean) SpringContextUtil.getBean("&sessionFactory");
        currentDialect = StringUtil.getStr(localSessionFactoryBean.getHibernateProperties().get("hibernate.dialect"));
    }
}
