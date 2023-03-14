package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.File;
import java.util.Date;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeVO extends DefaultVO {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String tableName = "NOTICE";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String subTableName = "NOTICE_ATTACH";

    private String searchTitle;
    private String searchMemberName;

    private Integer id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private List<String> contents;
    private String memberName;
    private String captcha;
    private String attachYN;

    private Integer crtId;
    private Integer udtId;
    private Integer delId;
    private String crtDt;
    private String udtDt;
    private String delDt;

    private String postStrDt;
    private String postEndDt;
    
    private List<NoticeAttachVO> attaches;
    
    public NoticeVO(Integer id) {
    	this.id = id;
    }
}
