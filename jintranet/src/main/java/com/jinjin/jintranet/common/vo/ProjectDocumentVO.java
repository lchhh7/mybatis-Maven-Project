package com.jinjin.jintranet.common.vo;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter
@NoArgsConstructor
public class ProjectDocumentVO extends ProjectDefaultVO {
    private Integer projectId;
    @NotBlank(message = "세그먼트를 선택해주세요.")
    private String segment;
    private String segmentName;
    private String task;
    private String taskName;
    @NotBlank(message = "문서명을 입력해주세요.")
    private String title;
    @Pattern(regexp = "^[F|L]$", message = "문서형식을 선택해주세요.")
    private String kind;
    private String kindName;
    private Integer version;
    private String path;
    private String originalFileName;
    private String storedFileName;
    private Long fileSize;

    private String action;
    private String changeDt;
    private String memberName;

    private Integer historyId;


    public ProjectDocumentVO(Integer id) {
        super(id);
    }
}
