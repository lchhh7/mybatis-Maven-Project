package com.jinjin.jintranet.supply.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.SupplyVO;
import com.jinjin.jintranet.supply.service.SupplyService;

@Controller
public class SupplyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupplyController.class);

    private final SupplyService supplyService;
    
    @Autowired
    public SupplyController(SupplyService supplyService) {
    	this.supplyService = supplyService;
    }
    /**
     * 비품관리 > 목록 페이지로 이동
     */
    @RequestMapping(value = "/supply.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {

            map.putAll(supplyService.supplyItems());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "supply/supply";
    }

    /**
     * 비품관리 > 목록 조회
     */
    @RequestMapping(value = "/supply/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findSupplyAll(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "s1", required = false, defaultValue = "") String statusR,
            @RequestParam(value = "s2", required = false, defaultValue = "") String statusY,
            @RequestParam(value = "s3", required = false, defaultValue = "") String statusN,
            @RequestParam(value = "n", required = false, defaultValue = "") String name,
            @RequestParam(value = "m", required = false, defaultValue = "") String memberId,
            @RequestParam(value = "a", required = false, defaultValue = "") String approveId,
            HttpServletRequest request) throws Exception {

        SupplyVO supplyVO = new SupplyVO();
        supplyVO.setPageIndex(page);
        supplyVO.setSearchStatus1(statusR);
        supplyVO.setSearchStatus2(statusY);
        supplyVO.setSearchStatus3(statusN);
        supplyVO.setSearchName(name);
        supplyVO.setSearchMemberId(memberId);
        supplyVO.setSearchApproveId(approveId);

        loggingCurrentMethod(LOGGER, supplyVO);

        return supplyService.findSupplyAll(supplyVO, request);
    }
    /**
     * 비품관리 > 등록페이지로 이동
     */
    @RequestMapping(value = "/supply/write.do", method = RequestMethod.GET)
    public String write(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(supplyService.supplyItems());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "supply/supply-write";
    }

    /**
     * 비품관리 > 등록
     */
    @RequestMapping(value = "/supply/write.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody SupplyVO supplyVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, supplyVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return supplyService.write(supplyVO);
    }

    /**
     * 비품관리 > 상세정보페이지로 이동
     */
    @RequestMapping(value = "/supply/view.do", method = RequestMethod.GET)
    public String view(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
            map.put("supply", supplyService.findSupplyById(id));
            map.putAll(supplyService.supplyItems());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "supply/supply-view";
    }

    /**
     * 비품관리 > 수정페이지로 이동
     */
    @RequestMapping(value = "/supply/edit.do", method = RequestMethod.GET)
    public String edit(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
            SupplyVO supplyVO = supplyService.findSupplyById(id);
            map.put("supply", supplyVO);

            map.putAll(supplyService.supplyItems());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "supply/supply-edit";
    }

    /**
     * 비품관리 > 수정
     */
    @RequestMapping(value = "/supply/edit.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@Validated @RequestBody SupplyVO supplyVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, supplyVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return supplyService.edit(supplyVO);
    }

    /**
     * 비품관리 > 삭제
     */
    @RequestMapping(value = "/supply/edit.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody SupplyVO supplyVO) throws Exception {
        loggingCurrentMethod(LOGGER, supplyVO);
        return supplyService.delete(supplyVO);
    }

    /**
     * 비품관리 > 승인/비승인
     */
    @RequestMapping(value = "/supply/edit/status.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> editStatus(@RequestBody SupplyVO supplyVO) throws Exception {
        loggingCurrentMethod(LOGGER, supplyVO);
        return supplyService.editStatus(supplyVO);
    }

    /**
     * 비품관리 > 승인 후 결제 방법 및 영수증 발행 여부 업데이트
     */
    @RequestMapping(value = "/supply/edit/pay.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> editPayment(@RequestBody SupplyVO supplyVO) throws Exception {
        loggingCurrentMethod(LOGGER, supplyVO);

        if (isBlank(supplyVO.getPayment()) || isBlank(supplyVO.getBillYN())) {
            return new ResponseEntity<>("결제방법과 계산서발행 여부 모두 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        return supplyService.editPayment(supplyVO);
    }

    /**
     * 비품관리 > 첨부파일 업로드
     */
    @RequestMapping(value = "/supply/upload", method = RequestMethod.POST)
    public ResponseEntity<List<AttachVO>> upload(MultipartHttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            return new ResponseEntity<>(FileUtils.upload(request, "supply_attach"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 비품관리 > 첨부파일 삭제
     */
    @RequestMapping(value = "/supply/attach", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAttach(@RequestParam("id") Integer id) throws Exception {
        return supplyService.deleteAttach(id);
    }

    /**
     * 비품관리 > 첨부파일 다운로드
     */
    @RequestMapping(value = "/supply/download", method = RequestMethod.POST)
    public void download(@RequestParam int id,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        supplyService.download(id, request, response);
    }
}

