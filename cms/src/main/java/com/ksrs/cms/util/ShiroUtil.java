package com.ksrs.cms.util;

import com.ksrs.cms.config.shiro.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;

public class ShiroUtil {

    public static void clearAuth(){
        RealmSecurityManager rem = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        AuthRealm realm = (AuthRealm) rem.getRealms().iterator().next();
        realm.clearAuthz();
    }
}
