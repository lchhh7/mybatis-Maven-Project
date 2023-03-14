package com.jinjin.jintranet.security;

import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.MemberVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Security Util 클래스
 */
public class SecurityUtils {

	public static Authentication getAuthObject() {
		return createAuthObject();
	}

	/**
	 * 로그인 된 계정 아이디를 돌려주는 함수
	 *
	 * @return
	 */
	public static String getLoginMemberName() {
		return createAuthObject().getName();
	}

	public static Integer getLoginMemberId() {
		MemberVO memberVO = (MemberVO)getSessionObject().getAttribute(Constants.SESSION_KEY_MEMBER);

		if (memberVO == null) {
			throw new IllegalArgumentException("로그인 되지 않은 상태거나 세션이 만료되었음");
		}

		return memberVO.getId();
	}

	private static Authentication createAuthObject() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 현재 로그인 된 계정 정보를 돌려주는 함수
	 *
	 * @return 로그인 계정 정보
	 */
	public static MemberVO getLoginMember() {
		MemberVO memberVO = (MemberVO)getSessionObject().getAttribute(Constants.SESSION_KEY_MEMBER);

		if (memberVO == null) {
			throw new IllegalArgumentException("로그인 되지 않은 상태거나 세션이 만료되었음");
		}

		return memberVO;
	}

	/**
	 * 현재 로그인 된 계정 정보를 받아서 세션에 저장하는 함수
	 *
	 * @param vo
	 */
	public static void setLoginMember(MemberVO vo) {
		vo.setPassword("");
		getSessionObject().setAttribute(Constants.SESSION_KEY_MEMBER, vo);
	}

	/**
	 * 세션을 가져오는 함수
	 *
	 * @return 세션
	 */
	private static HttpSession getSessionObject() {
		ServletRequestAttributes attr =
				(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

		return attr.getRequest().getSession(true);
	}

}
