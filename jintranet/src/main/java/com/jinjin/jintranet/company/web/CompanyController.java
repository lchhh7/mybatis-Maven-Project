package com.jinjin.jintranet.company.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

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
import com.jinjin.jintranet.common.vo.CompanyVO;
import com.jinjin.jintranet.company.service.CompanyService;

@Controller
@RequestMapping("admin")
public class CompanyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;
    
    @Autowired
    public CompanyController(CompanyService companyService) {
    	this.companyService = companyService;
    }
    @RequestMapping(value = "/company.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "company/company";
    }

    @RequestMapping(value = "/company/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> main(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "n", required = false, defaultValue = "") String name,
            @RequestParam(value = "k1", required = false, defaultValue = "") String kind1,
            @RequestParam(value = "k2", required = false, defaultValue = "") String kind2,
            HttpServletRequest request) throws Exception {

        CompanyVO companyVO = new CompanyVO();
        companyVO.setPageIndex(page);
        companyVO.setSearchName(name);
        companyVO.setSearchKind1(kind1);
        companyVO.setSearchKind2(kind2);

        loggingCurrentMethod(LOGGER, companyVO);
        return companyService.findCompanyAll(companyVO, request);
    }

    @RequestMapping(value = "/company/write.do", method = RequestMethod.GET)
    public String write(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "company/company-write";
    }

    @RequestMapping(value = "/company/write.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody CompanyVO companyVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, companyVO);

        if(bindingResult.hasErrors()) {
        	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(),HttpStatus.BAD_REQUEST);
        }

        return companyService.write(companyVO);
    }

    @RequestMapping(value = "/company/edit.do", method = RequestMethod.GET)
    public String view(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
            ResponseEntity<CompanyVO> r = companyService.findCompanyById(id);
            map.put("company", r.getBody());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "company/company-edit";
    }

    @RequestMapping(value = "/company/edit.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@Validated @RequestBody CompanyVO companyVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, companyVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return companyService.edit(companyVO);
    }

    @RequestMapping(value = "/company/edit.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody CompanyVO companyVO) throws Exception {
        loggingCurrentMethod(LOGGER, companyVO);
        return companyService.delete(companyVO);
    }

    /**
     * 업체관리 > 첨부파일 업로드
     */
    @RequestMapping(value = "/company/upload", method = RequestMethod.POST)
    public ResponseEntity<List<AttachVO>> upload(MultipartHttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            return new ResponseEntity<>(FileUtils.upload(request, "company_attach"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 업체관리 > 첨부파일 삭제
     */
    @RequestMapping(value = "/company/attach", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAttach(@RequestParam("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return companyService.deleteAttach(id);
    }

    /**
     * 업체관리 > 첨부파일 다운로드
     */
    @RequestMapping(value = "/company/download", method = RequestMethod.POST)
    public void download(@RequestParam int id,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        companyService.download(id, request, response);
    }
}
