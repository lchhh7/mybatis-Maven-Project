package com.jinjin.jintranet.document.service;

import static com.jinjin.jintranet.common.util.LoggingUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.vo.DocumentVO;
import com.jinjin.jintranet.member.service.MemberMapper;
import com.jinjin.jintranet.project.service.ProjectService;
import com.jinjin.jintranet.security.SecurityUtils;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.PageUtils.page;
import static com.jinjin.jintranet.common.util.StringUtils.under10;

@Service
public class DocumentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);
	
    private DocumentMapper documentMapper;

    public DocumentService() {}
    
    @Autowired
    DocumentService(DocumentMapper documentMapper) {
    	this.documentMapper = documentMapper;
    }
    
    public Map<String, Object> main(DocumentVO documentVO) {
        Map<String, Object> map = new HashMap<>();


        List<DocumentVO> documents = documentMapper.findAll(documentVO);
        int size = documents.size();

        map.put("documents", documents);
        map.put("documentNo", createDocumentNo(new LocalDate(documentVO.getSearchDocumentDt()), size));
        map.put("documentCnt", size);

        return map;
    }

    public List<DocumentVO> findAll(DocumentVO documentVO) {
        return documentMapper.findAll(documentVO);
    }


    public String createDocumentNo(LocalDate ld, int no) {
        return new StringBuffer()
                .append("제 ")
                .append(ld.getYear())
                .append("-")
                .append(under10(ld.getMonthOfYear()))
                .append(under10(ld.getDayOfMonth()))
                .append(under10(++no))
                .append("호").toString();
    }

    public String write(DocumentVO documentVO) {
        int loginId = SecurityUtils.getLoginMemberId();
        documentVO.setCrtId(loginId);
        documentVO.setUdtId(loginId);

        documentVO.setSearchDocumentDt(documentVO.getDocumentDt());
        List<DocumentVO> documents = documentMapper.findAll(documentVO);
        int size = documents.size();

        String documentNo = createDocumentNo(new LocalDate(documentVO.getSearchDocumentDt()), size);
        documentVO.setDocumentNo(documentNo);

        documentMapper.write(documentVO);

        return documentNo;
    }

    public void edit(DocumentVO documentVO) {
        int loginId = SecurityUtils.getLoginMemberId();
        documentVO.setUdtId(loginId);

        documentMapper.edit(documentVO);
    }
    
    public void delete(DocumentVO documentVO) {
        int loginId = SecurityUtils.getLoginMemberId();
        documentVO.setDelId(loginId);

        documentMapper.delete(documentVO);
    }
    
    public ResponseEntity<Map<String, Object>> searching(DocumentVO documentVO ,HttpServletRequest request) {
    	try {
    	Map<String, Object> map = new HashMap<>();
    	
    	int totalCnt = documentMapper.getDocumentCnt(documentVO);
    	documentVO.setTotalCnt(totalCnt);
    	List<DocumentVO> list =  documentMapper.searching(documentVO);
    	
    	 map.put("list", list);
         map.put("page", page(documentVO, "documents", request));
         map.put("totalCnt", totalCnt);

         return new ResponseEntity<>(map, HttpStatus.OK);
     } catch (Exception e) {
         loggingStackTrace(e, LOGGER);
         return new ResponseEntity<>(HttpStatus.CONFLICT);
     }
    }
    
    public DocumentVO findDocumentById(DocumentVO documentVO) {
    	return documentMapper.findDocumentById(documentVO);
    }
}
