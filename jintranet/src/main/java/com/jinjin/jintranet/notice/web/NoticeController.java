package com.jinjin.jintranet.notice.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

import java.net.URLDecoder;
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

import com.jinjin.jintranet.auth.service.AuthService;
import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.NoticeVO;
import com.jinjin.jintranet.notice.service.NoticeService;
import com.jinjin.jintranet.security.SecurityUtils;

@Controller
public class NoticeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

    private final NoticeService noticeService;
    
    private final AuthService authService;
    
    @Autowired
    public NoticeController(NoticeService noticeService,AuthService authService) {
    	this.noticeService = noticeService;
    	this.authService = authService;
    }
    
    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        try {
        	int writeAuthority = 0;
        	List<MemberVO> authList = authService.findAuthMemberByAuthId(new AuthVO(1));
        	for(int i=0; i<authList.size();i++) {
        		if(authList.get(i).getId().equals(SecurityUtils.getLoginMemberId())) {
        			writeAuthority++;
        		}
        	}
        	map.put("writeAuthority", writeAuthority);
            map.put("noticeList", noticeService.findNoticeAll());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            // TODO: 메인으로 돌리기 모든 컨트롤러 동일
            loggingStackTrace(e, LOGGER);
        }

        return "notice/notice";
    }

    @RequestMapping(value = "/notice/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> main(
            @RequestParam(value = "t", required = false, defaultValue = "") String searchTitle,
            @RequestParam(value = "m", required = false, defaultValue = "") String searchCrtid,
            @RequestParam(value = "p", required = false, defaultValue = "1") int pageIndex,
            HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, searchTitle, searchCrtid, pageIndex);
        return noticeService.findNoticeAll(searchTitle, searchCrtid, pageIndex, request);
    }


    @RequestMapping(value = "/notice/write", method = RequestMethod.GET)
    public String write(ModelMap map, HttpServletRequest request) throws Exception {
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "notice/notice-write";
    }

    @RequestMapping(value = "/notice/write", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody NoticeVO noticeVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, noticeVO);
        
        if (bindingResult.hasErrors()) {
        	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        

        if ("<p>&nbsp;</p>".equals(noticeVO.getContent())) {
            return new ResponseEntity<>("내용을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        
        noticeVO.setContent(URLDecoder.decode(noticeVO.getContent(), "UTF-8"));
        noticeVO.setTitle(URLDecoder.decode(noticeVO.getTitle(), "UTF-8"));
        noticeVO.setPostStrDt(noticeVO.getPostStrDt());
        noticeVO.setPostEndDt(noticeVO.getPostEndDt());
        return noticeService.write(noticeVO);
    }

    @RequestMapping(value = "/notice/view", method = RequestMethod.GET)
    public String view(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        try {
            map.put("notice", noticeService.findNoticeById(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "notice/notice-view";
    }

    @RequestMapping(value = "/notice", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody NoticeVO noticeVO) throws Exception {
        return noticeService.delete(noticeVO);
    }

    @RequestMapping(value = "/notice/edit", method = RequestMethod.GET)
    public String edit(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        try {
            map.put("notice", noticeService.findNoticeById(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "notice/notice-edit";
    }

    @RequestMapping(value = "/notice/edit", method = RequestMethod.POST)
    public ResponseEntity<String> edit(@Validated @RequestBody NoticeVO noticeVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, noticeVO);

        if (bindingResult.hasErrors()) {
        	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        if ("<p>&nbsp;</p>".equals(noticeVO.getContent())) {
            return new ResponseEntity<>("내용을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        return noticeService.edit(noticeVO);
    }

    @RequestMapping(value = "/notice/upload", method = RequestMethod.POST)
    public ResponseEntity<List<AttachVO>> upload(MultipartHttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            return new ResponseEntity<>(FileUtils.upload(request, "notice_attach"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/notice/attach", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAttach(@RequestParam("id") Integer id) throws Exception {
        return noticeService.deleteAttach(id);
    }

    @RequestMapping(value = "/notice/download", method = RequestMethod.POST)
    public void download(@RequestParam int id,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        noticeService.download(id, request, response);
    }
}
