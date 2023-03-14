package com.jinjin.jintranet.code.service;

import com.jinjin.jintranet.common.vo.CodeVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {

    List<CodeVO> findCodeByMajorCd(CodeVO codeVO);
    List<CodeVO> findCodeByMajorCdAndCodeFg(CodeVO codeVO);
}
