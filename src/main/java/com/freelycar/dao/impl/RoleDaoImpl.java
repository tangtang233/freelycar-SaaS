package com.freelycar.dao.impl;

import com.freelycar.basic.persistence.dao.MainBaseDaoImpl;
import com.freelycar.dao.RoleDao;
import com.freelycar.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * @author tangwei
 */
@Repository("roleDao")
public class RoleDaoImpl extends MainBaseDaoImpl<Role, Integer> implements RoleDao {

}
