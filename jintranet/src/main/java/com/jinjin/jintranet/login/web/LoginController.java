package com.jinjin.jintranet.login.web;

import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController implements Constants {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final MemberService memberService;

    @Autowired
    public LoginController(MemberService memberService) {
    	this.memberService = memberService;
    }
    /**
     * 로그인 페이지
     */
    @RequestMapping("/login.do")
    public String login() {
        return "login/index";
    }

    /**
     * 로그인
     */
    @ResponseBody
    @RequestMapping("/loginProcess.do")
    public ResponseEntity<String> loginProcess(
            @RequestBody MemberVO vo) throws Exception {
        LOGGER.info("## LoginController.loginProcess : " + vo.toString());

        return memberService.loginProcess(vo);
    }

	/**
	 * 로그인 후 Spring Security를 타기 위한 페이지
	 */
	@RequestMapping(value = "/loginAdaptor.do")
	public String loginAdaptor(ModelMap map, HttpSession session) {
		MemberVO vo = (MemberVO)session.getAttribute(SESSION_KEY_MEMBER);
		map.put("MemberVO", vo);
		return "login/adaptor";
		
	}
}