package com.freelycar.shiro;

import com.freelycar.basic.wrapper.ResultJO;
import com.freelycar.service.impl.AdminServiceImpl;
import com.freelycar.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author tangwei
 */
@Component
public class InitReadRoles {
	
	private static final Logger log = LogManager.getLogger(InitReadRoles.class);
	
	@Autowired
	private AdminServiceImpl adminService;

	@PostConstruct
	public void readRoles(){
		if(Constants.RELOAD_ROLES){
			log.debug("重新读取角色数据……");
			ResultJO resultJO = this.adminService.readRoles();
			log.debug(resultJO);
			log.debug("角色数据读取成功！");
		}
		else{
			log.error("角色数据读取失败！");
		}
	}
	
}
