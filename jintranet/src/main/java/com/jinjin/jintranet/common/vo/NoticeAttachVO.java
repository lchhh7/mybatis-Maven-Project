package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class NoticeAttachVO extends AttachVO {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String tableName = "NOTICE_ATTACH";
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String mainTableName = "NOTICE";

    private Integer noticeId;

    public NoticeAttachVO (Integer id) {
        super(id);
    }
}
