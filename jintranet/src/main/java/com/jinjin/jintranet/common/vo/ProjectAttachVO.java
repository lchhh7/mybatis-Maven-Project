package com.jinjin.jintranet.common.vo;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProjectAttachVO extends AttachVO {
    private Integer projectId;

    public ProjectAttachVO(Integer id) {
        super(id);
    }
}
