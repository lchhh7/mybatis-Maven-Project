package com.jinjin.jintranet.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class ProjectSegmentVO extends ProjectDefaultVO {
    private Integer segId;
    private Integer projectId;

    @NotBlank(message = "세그먼트명을 입력해주세요.")
    private String title;
    @NotBlank(message = "세그먼트 코드를 선택해주세요.")
    private String kind;
    private String kindName;
    @Pattern(regexp = "^{0}|[0-9]{2}$", message = "정렬순서는 두 자리 숫자로 입력해주세요.")
    private String ord;

    public ProjectSegmentVO(Integer id) {
        super(id);
    }

}
