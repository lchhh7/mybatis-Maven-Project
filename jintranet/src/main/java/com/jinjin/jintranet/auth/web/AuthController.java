package com.jinjin.jintranet.auth.web;

import com.jinjin.jintranet.auth.service.AuthService;
import com.jinjin.jintranet.common.vo.AuthVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;

@Controller
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
    	this.authService = authService;
    }
    
    @RequestMapping("/auths")
    public String main(ModelMap map,
                       HttpSession session) throws Exception {

        List<AuthVO> authList = null;

        try {
            authList = authService.findAuthAll();

            map.put("authList", authList);
            //map.putAll(getUserMenuList(SecurityUtils.getLoginedUserName()));

        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "auth/index";
    }

}
