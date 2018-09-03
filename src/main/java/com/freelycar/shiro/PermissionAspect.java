package com.freelycar.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {
	@Pointcut(value="execution(String *..controller*..*(..)) && @annotation(permissionRequire)", argNames="permissionRequire")
	public void controllerAspect(PermissionRequire permissionRequire) {
	}

	@Around("controllerAspect(permissionRequire)")
	public String permissioCheck(ProceedingJoinPoint pjp, PermissionRequire permissionRequire){
		String permission = permissionRequire.value();
		if(permission != null && !permission.isEmpty()){
			Subject curUser = SecurityUtils.getSubject();
			if(!curUser.isPermitted(permission)){
				//return JsonResFactory.buildOrg(RESCODE.PERMISSION_ERROR).toString();
			}
		}
		Object res = null;
		try {
			res = pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return (String) res;
	}
}
