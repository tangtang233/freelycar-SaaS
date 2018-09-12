package com.freelycar.service.impl;

import com.freelycar.basic.persistence.dao.BaseDao;
import com.freelycar.basic.persistence.service.BaseServiceImpl;
import com.freelycar.basic.wrapper.ResultJO;
import com.freelycar.dao.impl.AdminDaoImpl;
import com.freelycar.entity.Admin;
import com.freelycar.entity.Permission;
import com.freelycar.entity.Role;
import com.freelycar.service.AdminService;
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
public class AdminServiceImpl extends BaseServiceImpl<Admin, Integer> implements AdminService {

    private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private AdminDaoImpl adminDao;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @Autowired
    @Override
    public void setBaseDao(BaseDao<Admin, Integer> baseDao) {
        this.adminDao = (AdminDaoImpl) baseDao;
        this.baseDao = baseDao;
    }

    @Override
    public Admin findAdminByAccount(String account) {
        return adminDao.findAdminByAccount(account);
    }

    public ResultJO readRoles() {
        this.adminDao.clearRoles();
        Set<Role> roles = PermissionsList.getRoles();
        logger.info("从配置文件中读取到角色数据共：" + roles.size() + "条");
        for (Role role : roles) {
            Set<Permission> permissionsList = role.getPermissions();
            logger.info("角色名称:" + role.getRoleName() + ", 角色描述:" + role.getDescription() + ", 权限:" + permissionsList);
            roleService.save(role);
            for (Permission permission : permissionsList) {
                permission.setRoleId(role.getId());
                permissionService.save(permission);
            }
        }
        return ResultJO.getDefaultResult(null);
    }
}
