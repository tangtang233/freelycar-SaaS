package com.freelycar.service.impl;

import com.freelycar.basic.persistence.dao.IDao;
import com.freelycar.basic.persistence.service.BaseService;
import com.freelycar.dao.IRoleDao;
import com.freelycar.entity.Role;
import com.freelycar.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService extends BaseService<Role, Integer> implements IRoleService {
    private static Logger logger = LoggerFactory.getLogger(RoleService.class);

    private IRoleDao roleDao;

    @Autowired
    @Override
    public void setBaseDao(IDao<Role, Integer> baseDao) {
        this.roleDao = (IRoleDao) baseDao;
        this.baseDao = baseDao;
    }
}
