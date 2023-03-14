package com.jinjin.jintranet.member.service;

import java.util.List;

import com.jinjin.jintranet.common.vo.MemberVO;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface MemberMapper {
	MemberVO findMemberByMemberId(MemberVO memberVO);
	
	MemberVO findMemberById(MemberVO memberVO);
	
	void update(MemberVO memberVO);

	MemberVO select(MemberVO memberVO);

    void editPassword(MemberVO memberVO);

	void edit(MemberVO memberVO);
	
	List<MemberVO> findAll(MemberVO memberVO);
	
	List<MemberVO> findAllMember(MemberVO memberVO);

	List<MemberVO> findAllPassenger(MemberVO memberVO);
}