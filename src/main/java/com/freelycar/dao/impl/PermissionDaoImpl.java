package com.freelycar.dao.impl;

import com.freelycar.basic.persistence.dao.MainBaseDaoImpl;
import com.freelycar.dao.PermissionDao;
import com.freelycar.entity.Permission;
import org.springframework.stereotype.Repository;

/**
 * @author tangwei
 */
@Repository("permissionDao")
public class PermissionDaoImpl extends MainBaseDaoImpl<Permission,Integer> implements PermissionDao {

}
