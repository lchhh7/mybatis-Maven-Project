package com.jinjin.jintranet.supply.service;

import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.SupplyAttachVO;
import com.jinjin.jintranet.common.vo.SupplyVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface SupplyMapper {
    List<SupplyVO> findSupplyAllForMain(SupplyVO supplyVO);

    List<SupplyVO> findSupplyAll(SupplyVO supplyVO);

    SupplyVO findSupplyById(SupplyVO supplyVO);

    List<SupplyAttachVO> findSupplyAttachById(SupplyAttachVO supplyAttachVO);

    void write(SupplyVO supplyVO);

    void edit(SupplyVO supplyVO);

    void delete(SupplyVO supplyVO);

    void editPayment(SupplyVO supplyVO);

    void editStatus(SupplyVO supplyVO);

    int getSupplyCnt(SupplyVO supplyVO);

    void deleteAttach(SupplyAttachVO supplyAttachVO);

    AttachVO findSupplyAttachByAttachId(SupplyAttachVO supplyAttachVO);

    void writeAttach(SupplyAttachVO vo);
}
