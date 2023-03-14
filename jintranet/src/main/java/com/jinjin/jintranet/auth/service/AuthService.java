package com.jinjin.jintranet.auth.service;

import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.MemberVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private AuthMapper authMapper;
    
    public AuthService() {}
    
    @Autowired
    public AuthService(AuthMapper authMapper) {
    	this.authMapper = authMapper;
    }
    
    public List<AuthVO> findAuthAll() {
        return authMapper.findAuthAll();
    }
    
    public List<MemberVO> findAuthMemberByAuthId(AuthVO vo){
    	return authMapper.findAuthMemberByAuthId(vo);
    }
    
    public List<MemberVO> findAuthMemberByMemberId(AuthVO vo){
    	return authMapper.findAuthMemberByMemberId(vo);
    }
}
