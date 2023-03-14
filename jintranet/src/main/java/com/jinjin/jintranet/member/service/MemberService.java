package com.jinjin.jintranet.member.service;


import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.CodeVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.security.SecurityUtils;
@Service
public class MemberService implements Constants {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private MemberMapper memberMapper;

    public MemberService() {}
    
    @Autowired
    public MemberService(MemberMapper memberMapper) {
    	this.memberMapper = memberMapper;
    }
    /**
     * 로그인
     */
    public ResponseEntity<String> loginProcess(MemberVO vo) throws Exception {
        try {
            if (isEmpty(vo.getMemberId()) || isEmpty(vo.getPassword())) {
                return new ResponseEntity<>("아이디, 비밀번호를 모두 입력해주세요.", HttpStatus.NOT_FOUND);
            }

            MemberVO member = memberMapper.findMemberByMemberId(vo);

            if (member == null) {
                return new ResponseEntity<>("입력하신 정보와 일치하는 자료가 없습니다.", HttpStatus.NOT_FOUND);
            }

            String encPassword = DigestUtils.sha256Hex(vo.getPassword());
            if (!member.getPassword().equals(encPassword)) {
                return new ResponseEntity<>("입력하신 정보와 일치하는 자료가 없습니다.", HttpStatus.NOT_FOUND);
            }

            SecurityUtils.setLoginMember(member);

            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("사용자정보를 확인 중 오류가 발생하였습니다. 관리자에게 문의하세요", HttpStatus.CONFLICT);
        }
    }

    public MemberVO findMemberByMemberId(MemberVO vo) {
        vo.setMemberId(SecurityUtils.getLoginMember().getMemberId());
        return memberMapper.findMemberByMemberId(vo);
    }

    public MemberVO findMemberById(MemberVO vo) {
        return memberMapper.findMemberById(vo);
    }
    /**
     * 비밀번호 수정
     */
    public ResponseEntity<String> editPassword(MemberVO memberVO) throws Exception {
        try {
            String password = memberVO.getPassword();
            String newPassword = memberVO.getNewPassword();
            String newPassword2 = memberVO.getNewPassword2();

            if (isBlank(password) || isBlank(newPassword) || isBlank(newPassword2)) {
                return new ResponseEntity<>("모든 항목을 입력해주세요.", HttpStatus.NOT_FOUND);
            }

            MemberVO member = memberMapper.findMemberByMemberId(new MemberVO(SecurityUtils.getLoginMember().getMemberId()));
            String encPassword = DigestUtils.sha256Hex(password);

            if (!encPassword.equals(member.getPassword())) {
                return new ResponseEntity<>("현재 비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND);
            }

            if (!newPassword.equals(newPassword2)) {
                return new ResponseEntity<>("새 비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND);
            }

            encPassword = DigestUtils.sha256Hex(newPassword);

            member.setUdtId(SecurityUtils.getLoginMemberId());
            member.setPassword(encPassword);
            memberMapper.editPassword(member);

            return new ResponseEntity<>("비밀번호를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("비밀번호 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 계정 변경
     */
    public ResponseEntity<String> edit(MemberVO memberVO) throws Exception {
        try {
            // 1. 직급, 부서 확인
            // 2. 내선번호 확인
            // 3. 전화번호 확인
            // 4. color 확인


            Integer loginId = SecurityUtils.getLoginMemberId();
            memberVO.setId(loginId);
            memberVO.setUdtId(loginId);
            memberMapper.edit(memberVO);

            return new ResponseEntity<>("정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 계정 생성
     */
    public ResponseEntity<String> createMember(MemberVO memberVO) throws Exception {
        try {
            String loginedUserName = SecurityUtils.getLoginMemberName();
            return null;

        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return null;
        }
    }

    /**
     *
     */
  
    public List<MemberVO> findAll(MemberVO memberVO) throws Exception {
        try {
            return memberMapper.findAllMember(memberVO);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
    *
    */
 
   public List<MemberVO> findAllPassenger(MemberVO memberVO) throws Exception {
       try {
    	   memberVO.setId(SecurityUtils.getLoginMemberId());
           return memberMapper.findAllPassenger(memberVO);
       } catch (Exception e) {
           return null;
       }
   }

}