package com.jinjin.jintranet.admin.member.service;

import com.jinjin.jintranet.common.vo.MemberVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface AdminMemberMapper {
    int getMemberCnt(MemberVO memberVO);

    List<MemberVO> findAll(MemberVO memberVO);
    
    List<MemberVO> findMemberList(MemberVO memberVO);
    
    MemberVO findById(MemberVO memberVO);

    void write(MemberVO memberVO);

    void edit(MemberVO memberVO);

    void delete(MemberVO memberVO);
}
