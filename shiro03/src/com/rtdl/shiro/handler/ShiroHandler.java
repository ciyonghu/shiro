package com.rtdl.shiro.handler;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shiro")
public class ShiroHandler {
	public static Logger log = LoggerFactory.getLogger(ShiroHandler.class);
	
	
	@RequestMapping("/login")
	public String login(String username,String password){
		System.out.println(username + " : " + password);
		
		Subject subject = SecurityUtils.getSubject();
		
		if(!subject.isAuthenticated()){
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			token.setRememberMe(true);
			try {
				subject.login(token);
			} catch (UnknownAccountException e) {
				log.info("没有找到对应的账户");
			} catch (IncorrectCredentialsException e) {
				log.info("不正确的密码凭证");
			} catch (AuthenticationException e) {
				log.info("登录失败");
			}
		}
		
		
		return "redirect:/list.jsp";
	}
}
