package com.jinjin.jintranet.common.vo;

import lombok.Getter;

import static com.jinjin.jintranet.common.constant.Constants.*;

@Getter
public class OmissionScheduleCodeVO extends DefaultVO {
    private static final String scheduleTypeCd  = CODE_SCHEDULE_TYPE;
    private static final String addVacationCd   = CODE_SCHEDULE_TYPE_ADD_VACATION;
    private static final String omissionCd      = CODE_SCHEDULE_TYPE_OMISSION;
    
    
    private static final String statusCd        = CODE_STATUS;
    private static final String statusReadyCd   = CODE_STATUS_READY;
    private static final String statusYesCd     = CODE_STATUS_YES;
    private static final String statusNoCd      = CODE_STATUS_NO;
    private static final String statusCancelCd  = CODE_STATUS_CANCEL_REQUEST;
    private static final String statusDeleteCd  = CODE_STATUS_DELETE;
}
