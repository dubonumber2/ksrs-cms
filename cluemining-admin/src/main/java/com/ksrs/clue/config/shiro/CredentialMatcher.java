package com.ksrs.clue.config.shiro;

import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.util.PasswordHelper;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class CredentialMatcher extends SimpleCredentialsMatcher {

    @Autowired
    ClueminingUserService clueminingUserService;
    //登录的校验规则
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String password = new String(usernamePasswordToken.getPassword());
        String md5Passowrd=null;
        String dbPassword = (String) info.getCredentials();
        PasswordHelper helper = new PasswordHelper();
        md5Passowrd = helper.encryptPassword(((UsernamePasswordToken) token).getUsername(),password);
        if(dbPassword.equals("110")){
            dbPassword = clueminingUserService.findOldUserPassword(((UsernamePasswordToken) token).getUsername());
            md5Passowrd = DigestUtils.md5DigestAsHex(password.getBytes());
        }
        return this.equals(md5Passowrd,dbPassword);
    }


}
