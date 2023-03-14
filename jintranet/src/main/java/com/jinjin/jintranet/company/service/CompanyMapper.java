package com.jinjin.jintranet.company.service;

import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.CompanyAttachVO;
import com.jinjin.jintranet.common.vo.CompanyVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {
    List<CompanyVO> findCompanyAllForProject(CompanyVO companyVO);

    List<CompanyVO> findCompanyAll(CompanyVO companyVO);

    CompanyVO findCompanyById(CompanyVO companyVO);

    void write(CompanyVO companyVO);

    void edit(CompanyVO companyVO);

    void delete(CompanyVO companyVO);

    int getCompanyCnt(CompanyVO companyVO);

    AttachVO findCompanyAttachByAttachId(CompanyAttachVO companyAttachVO);

    void writeAttach(CompanyAttachVO vo);

    List<CompanyAttachVO> findCompanyAttachById(CompanyAttachVO companyAttachVO);

    void deleteAttach(CompanyAttachVO companyAttachVO);
}
