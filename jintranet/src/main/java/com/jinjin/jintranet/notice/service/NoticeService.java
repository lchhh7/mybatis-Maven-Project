package com.jinjin.jintranet.notice.service;

import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.util.StringUtils;
import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.NoticeAttachVO;
import com.jinjin.jintranet.common.vo.NoticeVO;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.PageUtils.page;

@Service
@NoArgsConstructor
public class NoticeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);

    private NoticeMapper noticeMapper;

    @Autowired
    public NoticeService(NoticeMapper noticeMapper) {
    	this.noticeMapper = noticeMapper;
    }

    public List<NoticeVO> findNoticeAll() {
        return noticeMapper.findNoticeAll(new NoticeVO());
    }

    public ResponseEntity<Map<String, Object>> findNoticeAll(String title, String memberName, int pageIndex, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            NoticeVO noticeVO  = new NoticeVO();
            noticeVO.setSearchTitle(title);
            noticeVO.setSearchMemberName(memberName);
            noticeVO.setPageIndex(pageIndex);

            int totalCnt = noticeMapper.getNoticeCnt(noticeVO);
            noticeVO.setTotalCnt(totalCnt);

            List<NoticeVO> list = noticeMapper.findNoticeAll(noticeVO);

            map.put("list", list);
            map.put("page", page(noticeVO, "notices", request));
            map.put("totalCnt", totalCnt);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    public ResponseEntity<String> write(NoticeVO noticeVO) throws Exception {
        try {
            int loingId = SecurityUtils.getLoginMemberId();
            noticeVO.setCrtId(loingId);
            noticeVO.setUdtId(loingId);

            List<String> contents = StringUtils.subStrByByte(noticeVO.getContent());
            noticeVO.setContents(contents);

            noticeMapper.write(noticeVO);

            uploadAttaches(noticeVO);

            return new ResponseEntity<>(noticeVO.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("공지사항 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public NoticeVO findNoticeById(Integer id) {
        NoticeVO vo = noticeMapper.findNoticeById(new NoticeVO(id));

        NoticeAttachVO noticeAttachVO = new NoticeAttachVO();
        noticeAttachVO.setNoticeId(id);

        List<NoticeAttachVO> attaches = noticeMapper.findNoticeAttachById(noticeAttachVO);
        vo.setAttaches(attaches);

        return vo;
    }
    
    public List<NoticeVO> findNoticePopupInfo(NoticeVO noticeVO) {
        return noticeMapper.findNoticePopupInfo(noticeVO);
    }
    
    public ResponseEntity<String> delete(NoticeVO noticeVO) throws Exception {
        try {
            noticeVO.setDelId(SecurityUtils.getLoginMemberId());
            noticeMapper.delete(noticeVO);
            return new ResponseEntity<>("공지사항을 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("공지사항 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(NoticeVO noticeVO) throws Exception {
        try {
            noticeVO.setUdtId(SecurityUtils.getLoginMemberId());

            List<String> contents = StringUtils.subStrByByte(noticeVO.getContent());
            noticeVO.setContents(contents);
            noticeMapper.edit(noticeVO);

            uploadAttaches(noticeVO);

            return new ResponseEntity<>(noticeVO.getId().toString(), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("공지사항 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> deleteAttach(Integer id) throws Exception {
        try {
            noticeMapper.deleteAttach(new NoticeAttachVO(id));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("첨부파일 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AttachVO vo = noticeMapper.findNoticeAttachByAttachId(new NoticeAttachVO(id));

        if (vo != null) {
            FileUtils.download(vo, request, response);
        }
    }

    private void uploadAttaches(NoticeVO noticeVO) {
        Integer noticeId = noticeVO.getId();
        int loingId = SecurityUtils.getLoginMemberId();
        List<NoticeAttachVO> list = noticeVO.getAttaches();
        if (list.size() > 0) {
            for (NoticeAttachVO vo : list) {
                vo.setCrtId(loingId);
                vo.setNoticeId(noticeId);
                noticeMapper.writeAttach(vo);
            }
        }
    }
}
