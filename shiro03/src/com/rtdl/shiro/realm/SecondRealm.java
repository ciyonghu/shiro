package com.rtdl.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 授权+认证
 * @author 刘鹏
 *
 */
public class SecondRealm extends AuthorizingRealm {

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		
		//将AuthenticationToken转换成UsernamePasswordToken
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		
		//从UsernamePasswordToken中获取username
		String username = upToken.getUsername();
		
		//调用数据库中的方法,从数据库中查询username对应的记录
		System.out.println("从数据库中获取username "+username);
		
		//若用户不存在，则亦可以做出抛出UnknownAccountException异常
		if("unknown".equals(username)){
			throw new UnknownAccountException("账号不存在");
		}
		
		//根据用户的情况，来构建AuthenticationInfo对象并返回,通常使用SimpleAuthenticationInfo
		//principal:认证实体信息，可以使username，也可以是数据表对应的用户的实体类对象,
		Object principal = username;
		//credentials:密码
		Object credentials = "123456";
		//realmName:当前realm对象的name，调用父类的getName()方法即可
		String realmName = getName();
		
		ByteSource credentialsSalt = ByteSource.Util.bytes(username); 
		
//		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
		
		//盐值加密
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
		
		return info;
	}

	
	public static void main(String[] args) {
		String algorithmName = "SHA1";
		String source = "123456";
		Object salt = null;
		int hashIterations = 1024;
		Object result = new SimpleHash(algorithmName, source, salt, hashIterations);
		System.out.println(result);
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//从principalCollection中获取登录用户的信息
		Object principal = principals.getPrimaryPrincipal();
		
		//利用登录的用户信息来获取当前用户的角色或权限
		Set<String> roles = new HashSet<String>();
		roles.add("user");
		if("admin".equals(principal)){
			roles.add("admin");
		}
		
		//创建simpleAuthorizationInfo,并设置其roles属性
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		
		//返回simpleAuthorizationInfo对象
		return info;
	}
}
