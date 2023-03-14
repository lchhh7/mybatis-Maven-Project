package com.jinjin.jintranet.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class ProjectScheduleVO extends ProjectDefaultVO {
    private String searchKind1;
    private String searchKind2;
    private String searchKind3;
    private String searchYear;

    private Integer scheduleId;
    private Integer projectId;
    @NotBlank(message = "일정명을 입력해주세요.")
    private String title;
    @NotBlank(message = "일정구분을 선택해주세요.")
    private String kind;
    private String kindName;
    private String place;
    private String attendee;
    private String remark;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 시작일을 선택해주세요. (yyyy-MM-dd)")
    private String scheduleStartDt;
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 시작시간을 입력해주세요. (HH:mm)")
    private String scheduleStartTm;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 종료일을 선택해주세요. (yyyy-MM-dd)")
    private String scheduleEndDt;
    @NotBlank(message = "일정 종료시간을 입력해주세요.")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 종료시간을 입력해주세요. (HH:mm)")
    private String scheduleEndTm;

    public ProjectScheduleVO(Integer id) {
        super(id);
    }

}
