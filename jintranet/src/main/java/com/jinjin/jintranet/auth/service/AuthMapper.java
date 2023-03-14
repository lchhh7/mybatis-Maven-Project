package com.jinjin.jintranet.auth.service;

import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {

    List<AuthVO> findAuthAll();

    List<AuthVO> findAuthByMemberId(AuthVO authVO);

    List<MemberVO> findAuthMemberByAuthId(AuthVO authVO);
    
    List<MemberVO> findAuthMemberByMemberId(AuthVO authVO);

    void deleteAuthMemberByMemberId(AuthVO authVO);

    void writeAuthMember(AuthVO authVO);
}
