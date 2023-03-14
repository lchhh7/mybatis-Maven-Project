package com.jinjin.jintranet.document.service;

import java.util.List;

import com.jinjin.jintranet.common.vo.DocumentVO;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface DocumentMapper {
    List<DocumentVO> findAll(DocumentVO documentVO);

    DocumentVO findById(DocumentVO documentVO);

    void write(DocumentVO documentVO);

    void edit(DocumentVO documentVO);

    void delete(DocumentVO documentVO);
    
    List<DocumentVO> searching(DocumentVO documentVO);
    
    int getDocumentCnt(DocumentVO documentVO);
    
    DocumentVO findDocumentById(DocumentVO documentVO);
}
