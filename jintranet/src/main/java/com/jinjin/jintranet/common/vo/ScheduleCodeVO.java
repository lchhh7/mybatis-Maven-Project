package com.jinjin.jintranet.common.vo;

import lombok.Getter;

import static com.jinjin.jintranet.common.constant.Constants.*;

@Getter
public class ScheduleCodeVO extends DefaultVO {
    private static final String scheduleTypeCd  = CODE_SCHEDULE_TYPE;
    private static final String scheduleCd      = CODE_SCHEDULE_TYPE_SCHEDULE;
    private static final String fullVacationCd  = CODE_SCHEDULE_TYPE_FULL_VACATION;
    private static final String halfVacationCd  = CODE_SCHEDULE_TYPE_HALF_VACATION;
    private static final String addVacationCd   = CODE_SCHEDULE_TYPE_ADD_VACATION;
    private static final String outsideWorkCd   = CODE_SCHEDULE_TYPE_OUTSIDE_WORK;
    private static final String businessTripCd   = CODE_SCHEDULE_TYPE_BUSINESS_TRIP;
    private static final String overtimeWorkCd  = CODE_SCHEDULE_TYPE_OVERTIME_WORK;


    private static final String statusCd        = CODE_STATUS;
    private static final String statusReadyCd   = CODE_STATUS_READY;
    private static final String statusYesCd     = CODE_STATUS_YES;
    private static final String statusNoCd      = CODE_STATUS_NO;
    private static final String statusCancelCd  = CODE_STATUS_CANCEL_REQUEST;
    private static final String statusDeleteCd  = CODE_STATUS_DELETE;
}
