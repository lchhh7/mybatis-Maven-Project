package com.jinjin.jintranet.member.web;


import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.security.SecurityUtils;

@Controller
public class MemberController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

	private final MemberService memberService;
	
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@RequestMapping(value = "/member/edit.do", method = RequestMethod.GET)
	public String edit(ModelMap map, HttpServletRequest request) throws Exception {
		loggingCurrentMethod(LOGGER);
		try {
			map.putAll(getDefaultMenu(request));
		} catch (Exception e) {
			loggingStackTrace(e, LOGGER);
		}
		
		return "member/member-edit";
	}

	@RequestMapping(value = "/member/edit.do", method = RequestMethod.PATCH)
	public ResponseEntity<String> edit(@Validated @RequestBody MemberVO memberVO,BindingResult bindingResult) throws Exception {
		loggingCurrentMethod(LOGGER, memberVO);

		memberVO.setMemberId(SecurityUtils.getLoginMember().getMemberId());

		if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

		return memberService.edit(memberVO);
	}

	@RequestMapping(value = "/member/edit/p.do", method = RequestMethod.PATCH)
	public ResponseEntity<String> editPassword(@RequestBody MemberVO memberVO) throws Exception {
		loggingCurrentMethod(LOGGER, memberVO);
		return memberService.editPassword(memberVO);
	}
}
