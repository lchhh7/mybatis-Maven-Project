package com.jinjin.jintranet.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jinjin.jintranet.auth.service.AuthMapper;
import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.member.service.MemberMapper;

public class SecurityService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    private final MemberMapper userMapper;
    
    public final AuthMapper authMapper;
    
    @Autowired
    public SecurityService(MemberMapper userMapper,AuthMapper authMapper) {
        this.userMapper = userMapper;
        this.authMapper = authMapper;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO memberVO = userMapper.findMemberByMemberId(new MemberVO(username));

        if (memberVO == null) {
            LOGGER.info("## SecurityService.loadUserByUsername() : User info is NULL");
            return null;
        }

        /* 나중에 롤 역할 픽스되면 값을 나눠서 설정해줄 것 / 아마 메뉴별이 되지 않을까? */
        Collection<SimpleGrantedAuthority> roles = generateDefaultUserRoles(memberVO);
        //UserDetails user = new User(memberVO.getMemberId(), memberVO.getPassword(), roles);
        UserDetailsVO userVO = new UserDetailsVO(memberVO,roles);
        SecurityUtils.setLoginMember(memberVO);
        return userVO;
    }

    /**
     * 유저정보에 설정된 기본 역할을 리턴한다.
     */
    private Collection<SimpleGrantedAuthority> generateDefaultUserRoles(MemberVO memberVO) {
        Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
        	AuthVO authVO = new AuthVO();
        	authVO.setMemberId(memberVO.getId());
	        List<MemberVO> authorityList = authMapper.findAuthMemberByMemberId(authVO);
	        if(authorityList.size()==0) {
	        	System.out.println("USER");
	        	roles.add(new SimpleGrantedAuthority("ROLE_USER"));
	        } else {
		        for(int i=0; i<authorityList.size();i++) {
		        	System.out.println("ADMIN " + i);
		        	roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"+authorityList.get(i).getAuthId()));
		        }
	        }
        return roles;
    }

}
