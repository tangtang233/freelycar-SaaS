package com.freelycar.shiro;

import com.freelycar.service.IAdminService;
import com.freelycar.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitReadRoles {
	
	private static final Logger log = LogManager.getLogger(InitReadRoles.class);
	
//	@Resource(name = "adminService")
//	private IAdminService adminService;
	
	@PostConstruct
	public void readRoles(){
		if(Constants.RELOAD_ROLES){
			log.debug("重新读取角色数据……");
//			this.adminService.readRoles();
			log.debug("角色数据读取成功！");
		}
		else{
			log.error("角色数据读取失败！");
		}
	}
	
}
