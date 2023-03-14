package com.jinjin.jintranet.supply.service;

import com.jinjin.jintranet.auth.service.AuthMapper;
import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.*;
import com.jinjin.jintranet.member.service.MemberMapper;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.PageUtils.page;

@Service
public class SupplyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyService.class);

    @Autowired
    private SupplyMapper supplyMapper;
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private MemberMapper memberMapper;
    
    public SupplyService() {}
    
    @Autowired
    public SupplyService(SupplyMapper supplyMapper,AuthMapper authMapper,CodeMapper codeMapper,MemberMapper memberMapper) {
    	this.supplyMapper = supplyMapper;
    	this.authMapper = authMapper;
    	this.codeMapper = codeMapper;
    	this.memberMapper = memberMapper;
    }
    
    
    public List<SupplyVO> findSupplyAllForMain() {
        return supplyMapper.findSupplyAllForMain(new SupplyVO());
    }

    public ResponseEntity<Map<String,Object>> findSupplyAll(SupplyVO supplyVO, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            int totalCnt = supplyMapper.getSupplyCnt(supplyVO);
            supplyVO.setTotalCnt(totalCnt);

            List<SupplyVO> list = supplyMapper.findSupplyAll(supplyVO);

            map.put("list", list);
            map.put("page", page(supplyVO, "supplies", request));
            map.put("totalCnt", totalCnt);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public SupplyVO findSupplyById(Integer id) {
        SupplyVO vo = supplyMapper.findSupplyById(new SupplyVO(id));

        SupplyAttachVO supplyAttachVO = new SupplyAttachVO();
        supplyAttachVO.setSupplyId(id);

        List<SupplyAttachVO> attaches = supplyMapper.findSupplyAttachById(supplyAttachVO);
        vo.setAttaches(attaches);

        return vo;
    }

    public ResponseEntity<String> write(SupplyVO supplyVO) throws Exception {
        try {
            // validation 체크 추가

            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            supplyVO.setCrtId(loginMemberId);
            supplyVO.setUdtId(loginMemberId);
        	supplyVO.setMemberId(loginMemberId);
            supplyMapper.write(supplyVO);

            uploadAttaches(supplyVO);

            return new ResponseEntity<>(supplyVO.getId().toString(), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("비품 신청 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(SupplyVO supplyVO) throws Exception {
        try {
            // validation 체크 추가

            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            supplyVO.setUdtId(loginMemberId);
            supplyMapper.edit(supplyVO);

            uploadAttaches(supplyVO);

            return new ResponseEntity<>("비품 신청내역을 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("비품 신청내역 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(SupplyVO supplyVO) {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            supplyVO.setDelId(loginMemberId);
            supplyMapper.delete(supplyVO);

            return new ResponseEntity<>("비품 신청내역을 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("비품 신청내역 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> editPayment(SupplyVO supplyVO) throws Exception {
        try {
            SupplyVO vo = supplyMapper.findSupplyById(supplyVO);
            if (vo == null) return new ResponseEntity<>("비정상적인 접근입니다.", HttpStatus.NOT_FOUND);

            if (!"Y".equals(vo.getStatus()))
                return new ResponseEntity<>("승인상태가 아닌경우 저장할 수 없습니다.", HttpStatus.NOT_FOUND);

            if ("".equals(vo.getPayment()) || "".equals(vo.getBillYN()))
                return new ResponseEntity<>("결제방법과 계산서발행 여부 모두 입력해주세요.", HttpStatus.NOT_FOUND);

            supplyMapper.editPayment(supplyVO);

            return new ResponseEntity<>("정상적으로 저장했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("결제방법 저장 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> editStatus(SupplyVO supplyVO) {
        String statusName = null;
        try {
            String status = supplyVO.getStatus();

            if (!("Y".equals(status) || "N".equals(status) || "R".equals(status))) {
                return new ResponseEntity<>("비정상적인 접근입니다.", HttpStatus.NOT_FOUND);
            }

            statusName = "Y".equals(status) ? "승인" : "N".equals(status) ? "비승인" : "승인 대기";

            //Integer loginMemberId = SecurityUtils.getLoginMemberId();
            supplyMapper.editStatus(supplyVO);

            return new ResponseEntity<>("신청내역을 정상적으로 " + statusName +  " 처리했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("신청내역 " + statusName +" 처리 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public Map<String, Object> supplyItems() {
        Map<String, Object> map = new HashMap<>();
        map.put("payment", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_SUPPLY_PAYMENT)));
        map.put("status", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_STATUS)));
        map.put("approves", authMapper.findAuthMemberByAuthId(new AuthVO(Constants.CODE_AUTH_SUPPLY)));
        map.put("members", memberMapper.findAll(new MemberVO()));


        return map;
    }


    public ResponseEntity<String> deleteAttach(Integer id) throws Exception {
        try {
            supplyMapper.deleteAttach(new SupplyAttachVO(id));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("첨부파일 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AttachVO vo = supplyMapper.findSupplyAttachByAttachId(new SupplyAttachVO(id));

        if (vo != null) {
            FileUtils.download(vo, request, response);
        }
    }

    private void uploadAttaches(SupplyVO supplyVO) {
        Integer supplyId = supplyVO.getId();
        int loingId = SecurityUtils.getLoginMemberId();
        List<SupplyAttachVO> list = supplyVO.getAttaches();
        if (list.size() > 0) {
            for (SupplyAttachVO vo : list) {
                vo.setCrtId(loingId);
                vo.setSupplyId(supplyId);
                supplyMapper.writeAttach(vo);
            }
        }
    }


}
