package com.jinjin.jintranet.common.vo;

import com.jinjin.jintranet.common.vo.DefaultVO;

import static com.jinjin.jintranet.common.constant.Constants.*;
import static com.jinjin.jintranet.common.constant.Constants.CODE_STATUS_DELETE;

public class CommutingCodeVO extends DefaultVO {
    private static final String commutingRequestTypeCd  = CODE_COMMUTING_REQUEST_TYPE;
    private static final String onWorkCd                = CODE_COMMUTING_REQUEST_TYPE_ON;
    private static final String offWorkCd               = CODE_COMMUTING_REQUEST_TYPE_OFF;
    private static final String overtimeWorkCd          = CODE_COMMUTING_REQUEST_TYPE_OVER;
    private static final String addRequestCd            = CODE_COMMUTING_REQUEST_TYPE_ADD;

    private static final String statusCd        = CODE_STATUS;
    private static final String statusReadyCd   = CODE_STATUS_READY;
    private static final String statusYesCd     = CODE_STATUS_YES;
    private static final String statusNoCd      = CODE_STATUS_NO;
    private static final String statusCancelCd  = CODE_STATUS_CANCEL_REQUEST;
    private static final String statusDeleteCd  = CODE_STATUS_DELETE;
}
