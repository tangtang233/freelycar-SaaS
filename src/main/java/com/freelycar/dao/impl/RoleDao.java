package com.freelycar.dao.impl;

import com.freelycar.basic.persistence.dao.MainBaseDao;
import com.freelycar.dao.IRoleDao;
import com.freelycar.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * @author tangwei
 */
@Repository("roleDao")
public class RoleDao extends MainBaseDao<Role, Integer> implements IRoleDao {

}
