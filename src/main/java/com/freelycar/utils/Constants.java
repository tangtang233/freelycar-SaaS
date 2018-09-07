package com.freelycar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 常量类，存储项目中需要用的常量
 * @author haizhe
 * modify by tangwei 2018-09-07
 */
public class Constants {

	/**
	 * 返回对象里的code的key名称
	 */
	public static final String RESPONSE_CODE_KEY = "code";
	/**
	 * 返回对象里的msg的key名称
	 */
	public static final String RESPONSE_MSG_KEY = "msg";
	/**
	 * 返回对象里的data的key名称
	 */
	public static final String RESPONSE_DATA_KEY = "data";
	/**
	 * 返回对象里的size的key名称
	 */
	public static final String RESPONSE_SIZE_KEY = "size";
	public static final String RESPONSE_CLIENT_KEY = "client";
	public static final String RESPONSE_REAL_SIZE_KEY = "realSize";
	public static final String RESPONSE_AMOUNT_KEY = "amount";
	
	private static final String RELOAD_ROLES_KEY = "reload_roles";
	public static boolean RELOAD_ROLES;
	
	public static final int PROJECT_WITH_CARD = 0;
	public static final int PROJECT_WITH_CASH = 1;
	public static final int PROJECT_WITH_FAVOUR = 2;

	public static final String STAFF_NAME_SPLIT = ";";
	
	private static Properties p = null;
	
	static {
		p = new Properties();
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl == null) {
				cl = Constants.class.getClassLoader();
			}
			InputStream in = cl.getResourceAsStream("config.properties");
			
			p.load(in);
			RELOAD_ROLES = Boolean.valueOf(p.getProperty(RELOAD_ROLES_KEY));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
		
}
