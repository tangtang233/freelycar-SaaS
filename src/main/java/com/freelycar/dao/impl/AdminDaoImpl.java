package com.freelycar.dao.impl;

import com.freelycar.basic.persistence.dao.MainBaseDaoImpl;
import com.freelycar.dao.AdminDao;
import com.freelycar.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangwei
 * @date 2018/9/3
 */
@Repository("adminDao")
public class AdminDaoImpl extends MainBaseDaoImpl<Admin,Integer> implements AdminDao {

    @Override
    public Admin findAdminByAccount(String account) {
        String hql = "from Admin where delStatus=0 account = ?";
        List paramList = new ArrayList<>();
        return unique(hql, paramList);
    }

    @Override
    public void clearRoles() {
        String hql4ClearRoles = "delete from Role";
        String hql4ClearPermissions = "delete from Permission";
        List<String> sqlList = new ArrayList<>();
        sqlList.add(hql4ClearRoles);
        sqlList.add(hql4ClearPermissions);
        this.executeBatchNative(sqlList);
//        execteBulk(hql4ClearRoles, null);
//        execteBulk(hql4ClearPermissions, null);
    }
}
