package com.freelycar.service.impl;

import com.freelycar.basic.persistence.dao.IDao;
import com.freelycar.basic.persistence.service.BaseService;
import com.freelycar.basic.wrapper.ResultJO;
import com.freelycar.dao.IAdminDao;
import com.freelycar.entity.Admin;
import com.freelycar.entity.Role;
import com.freelycar.service.IAdminService;
import com.freelycar.service.IRoleService;
import com.freelycar.utils.PermissionsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author tangwei
 */
@Service("adminService")
public class AdminService extends BaseService<Admin, Integer> implements IAdminService {

    private static Logger logger = LoggerFactory.getLogger(AdminService.class);

    private IAdminDao adminDao;

    @Autowired
    @Override
    public void setBaseDao(IDao<Admin, Integer> baseDao) {
        this.adminDao = (IAdminDao) baseDao;
        this.baseDao = baseDao;
    }

    @Autowired
    private IRoleService roleService;

    @Override
    public Admin findAdminByAccount(String account) {
        return adminDao.findAdminByAccount(account);
    }

    public ResultJO readRoles() {
        this.adminDao.clearRoles();
        Set<Role> roles = PermissionsList.getRoles();
        logger.debug("从配置文件中读取到角色数据共：" + roles.size() + "条");
        for(Role role : roles){
            logger.debug("角色名称:" + role.getRoleName() + ", 角色描述:" + role.getDescription() + ", 权限:" + role.getPermissions());
            roleService.save(role);
        }
        return ResultJO.getDefaultResult(null);
    }
}
