package com.jinjin.jintranet.company.service;

import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.CompanyAttachVO;
import com.jinjin.jintranet.common.vo.CompanyVO;
import com.jinjin.jintranet.common.vo.SupplyAttachVO;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.PageUtils.page;

@Service
public class CompanyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    private CompanyMapper companyMapper;

    public CompanyService() {}
    
    @Autowired
    public CompanyService(CompanyMapper companyMapper) {
    	this.companyMapper = companyMapper;
    }
    
    public List<CompanyVO> findCompanyAll() {
        return companyMapper.findCompanyAll(new CompanyVO());
    }

    public ResponseEntity<Map<String, Object>> findCompanyAll(CompanyVO companyVO, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            int totalCnt = companyMapper.getCompanyCnt(companyVO);
            companyVO.setTotalCnt(totalCnt);

            List<CompanyVO> list = companyMapper.findCompanyAll(companyVO);

            map.put("list", list);
            map.put("page", page(companyVO, "company", request));
            map.put("totalCnt", totalCnt);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    public ResponseEntity<CompanyVO> findCompanyById(Integer id) throws Exception {
        try {
            CompanyVO vo = companyMapper.findCompanyById(new CompanyVO(id));

            CompanyAttachVO companyAttachVO = new CompanyAttachVO();
            companyAttachVO.setCompanyId(id);

            List<CompanyAttachVO> attaches = companyMapper.findCompanyAttachById(companyAttachVO);
            List<CompanyAttachVO> bankbooks = new ArrayList<>();

            for (CompanyAttachVO tmp : attaches) {
                String kind = tmp.getAttachKind();
                if ("L".equals(kind)) {
                    vo.setLicense(tmp);
                } else if ("B".equals(kind)) {
                    bankbooks.add(tmp);
                }
            }

            vo.setBankbooks(bankbooks);

            return new ResponseEntity<>(vo, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> write(CompanyVO companyVO) throws Exception {
        try {
            List<CompanyAttachVO> attaches = new ArrayList<>();
            CompanyAttachVO license = companyVO.getLicense();
            List<CompanyAttachVO> bankbooks = companyVO.getBankbooks();

            if (license == null) {
                companyVO.setLicenseYN("N");
            } else {
                companyVO.setLicenseYN("Y");
                license.setAttachKind("L");

                attaches.add(license);
            }

            if (bankbooks.size() <= 0) {
                companyVO.setBankbookYN("N");
            } else {
                companyVO.setBankbookYN("Y");
                for (CompanyAttachVO vo : bankbooks) {
                    vo.setAttachKind("B");
                }

                attaches.addAll(bankbooks);
            }

            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            companyVO.setCrtId(loginMemberId);
            companyVO.setUdtId(loginMemberId);

            companyMapper.write(companyVO);

            companyVO.setAttaches(attaches);

            uploadAttaches(companyVO);

            return new ResponseEntity<>(companyVO.getId().toString(), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("업체 정보 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(CompanyVO companyVO) throws Exception {
        try {
            List<CompanyAttachVO> attaches = new ArrayList<>();
            CompanyAttachVO license = companyVO.getLicense();
            List<CompanyAttachVO> bankbooks = companyVO.getBankbooks();

            CompanyAttachVO companyAttachVO = new CompanyAttachVO();
            companyAttachVO.setCompanyId(companyVO.getId());

            if (license == null) {
                companyAttachVO.setSearchAttachKind("L");
                int size = companyMapper.findCompanyAttachById(companyAttachVO).size();
                companyVO.setLicenseYN(size == 0 ? "N" : "Y");
            } else {
                companyVO.setLicenseYN("Y");
                license.setAttachKind("L");

                attaches.add(license);
            }

            if (bankbooks.size() <= 0) {
                companyAttachVO.setSearchAttachKind("B");
                int size = companyMapper.findCompanyAttachById(companyAttachVO).size();
                companyVO.setBankbookYN(size == 0 ? "N" : "Y");
            } else {
                companyVO.setBankbookYN("Y");
                for (CompanyAttachVO vo : bankbooks) {
                    vo.setAttachKind("B");
                }

                attaches.addAll(bankbooks);
            }

            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            companyVO.setUdtId(loginMemberId);

            companyMapper.edit(companyVO);

            companyVO.setAttaches(attaches);

            uploadAttaches(companyVO);

            return new ResponseEntity<>("업체 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("업체 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(CompanyVO companyVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            companyVO.setDelId(loginMemberId);

            companyMapper.delete(companyVO);

            return new ResponseEntity<>("업체 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("업체 정보 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> deleteAttach(Integer id) throws Exception {
        try {
            companyMapper.deleteAttach(new CompanyAttachVO(id));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("첨부파일 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AttachVO vo = companyMapper.findCompanyAttachByAttachId(new CompanyAttachVO(id));

        if (vo != null) {
            FileUtils.download(vo, request, response);
        }
    }

    private void uploadAttaches(CompanyVO companyVO) {
        Integer companyId = companyVO.getId();
        int loingId = SecurityUtils.getLoginMemberId();
        List<CompanyAttachVO> list = companyVO.getAttaches();
        if (list.size() > 0) {
            for (CompanyAttachVO vo : list) {
                vo.setCrtId(loingId);
                vo.setCompanyId(companyId);
                companyMapper.writeAttach(vo);
            }
        }
    }

    public List<CompanyVO> findCompanyAllForProject() {
        return companyMapper.findCompanyAllForProject(new CompanyVO());
    }
}
