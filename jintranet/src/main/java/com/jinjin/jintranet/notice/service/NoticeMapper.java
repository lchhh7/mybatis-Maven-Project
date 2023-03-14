package com.jinjin.jintranet.notice.service;

import com.jinjin.jintranet.common.vo.NoticeAttachVO;
import com.jinjin.jintranet.common.vo.NoticeVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {
    List<NoticeVO> findNoticeAll(NoticeVO noticeVO);

    void write(NoticeVO noticeVO);

    NoticeVO findNoticeById(NoticeVO noticeVO);
    
    List<NoticeVO> findNoticePopupInfo(NoticeVO noticeVO);

    List<NoticeAttachVO> findNoticeAttachById(NoticeAttachVO noticeAttachVO);

    void delete(NoticeVO noticeVO);

    void edit(NoticeVO noticeVO);

    void writeAttach(NoticeAttachVO vo);

    NoticeAttachVO findNoticeAttachByAttachId(NoticeAttachVO noticeAttachVO);

    void deleteAttach(NoticeAttachVO noticeAttachVO);

    int getNoticeCnt(NoticeVO noticeVO);
    
}
