package com.freelycar.service.impl;

import com.freelycar.basic.persistence.dao.BaseDao;
import com.freelycar.basic.persistence.service.BaseServiceImpl;
import com.freelycar.dao.impl.PermissionDaoImpl;
import com.freelycar.entity.Permission;
import com.freelycar.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangwei
 */
@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl<Permission,Integer> implements PermissionService {
    private static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private PermissionDaoImpl permissionDao;

    @Autowired
    @Override
    public void setBaseDao(BaseDao<Permission, Integer> baseDao) {
        this.permissionDao = (PermissionDaoImpl) baseDao;
        this.baseDao = baseDao;
    }
}
