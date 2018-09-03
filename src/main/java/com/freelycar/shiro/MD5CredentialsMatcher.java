
package com.freelycar.shiro;

import com.freelycar.utils.MD5;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class MD5CredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
	    Object accountCredentials = getCredentials(info);
	    String psw = MD5.compute(String.valueOf(authcToken.getPassword()));
	    System.out.println("computed password:" + psw);
	    return equals(psw, accountCredentials);
	}

}

