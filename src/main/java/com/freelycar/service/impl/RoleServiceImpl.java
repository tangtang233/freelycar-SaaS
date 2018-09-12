package com.freelycar.service.impl;

import com.freelycar.basic.persistence.dao.BaseDao;
import com.freelycar.basic.persistence.service.BaseServiceImpl;
import com.freelycar.dao.RoleDao;
import com.freelycar.entity.Role;
import com.freelycar.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements RoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleDao roleDao;

    @Autowired
    @Override
    public void setBaseDao(BaseDao<Role, Integer> baseDao) {
        this.roleDao = (RoleDao) baseDao;
        this.baseDao = baseDao;
    }
}
