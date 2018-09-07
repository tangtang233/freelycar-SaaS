package com.freelycar.dao;

import com.freelycar.basic.persistence.dao.IDao;
import com.freelycar.entity.Admin;

/**
 * @author tangwei
 * @date 2018/9/3
 */
public interface IAdminDao extends IDao<Admin,Integer> {

    Admin findAdminByAccount(String account);

    void clearRoles();
}
