package com.jinjin.jintranet.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
public class AttachVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String path;
    private String originalFileName;
    private String storedFileName;
    private Long fileSize;

    private String crtDt;
    private String delDt;
    private Integer crtId;
    private Integer delId;

    public AttachVO(Integer id) {
        this.id = id;
    }
}
