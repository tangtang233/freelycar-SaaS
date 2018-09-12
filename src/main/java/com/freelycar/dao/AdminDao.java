package com.freelycar.dao;

import com.freelycar.basic.persistence.dao.BaseDao;
import com.freelycar.entity.Admin;

/**
 * @author tangwei
 * @date 2018/9/3
 */
public interface AdminDao extends BaseDao<Admin,Integer> {

    Admin findAdminByAccount(String account);

    void clearRoles();
}
