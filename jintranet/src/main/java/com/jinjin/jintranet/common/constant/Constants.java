package com.jinjin.jintranet.common.constant;

import com.jinjin.jintranet.common.vo.CodeVO;

public interface Constants {
	String SESSION_KEY_MEMBER = "loginUser";

	String CODE_POSITION            = "001";
	String CODE_DEPARTMENT          = "002";
	String CODE_PROJECT_ACTION      = "003";
    String CODE_PROJECT_SEGMENT     = "004";
    String CODE_PROJECT_TASK        = "005";
    String CODE_PROJECT_SCHEDULE    = "006";
    String CODE_PROJECT_DOCUMENT    = "007";
    String CODE_STATUS              = "008";
    String CODE_SUPPLY_PAYMENT      = "009";
    String CODE_COMPANY_KIND        = "010";
    String CODE_COMMUTING_TYPE      = "011";
    String CODE_SCHEDULE_TYPE       = "012";
    String CODE_COMMUTING_REQUEST_TYPE       = "013";

    int CODE_AUTH_NOTICE = 1;
    int CODE_AUTH_MEMBER = 2;
    int CODE_AUTH_SCHEDULE = 3;
    int CODE_AUTH_SUPPLY = 4;
    int CODE_AUTH_COMPANY = 5;


    String CODE_SCHEDULE_TYPE_SCHEDULE          = "SC";
    String CODE_SCHEDULE_TYPE_VACATION          = "VA";
    String CODE_SCHEDULE_TYPE_FULL_VACATION     = "FV";
    String CODE_SCHEDULE_TYPE_HALF_VACATION     = "HV";
    String CODE_SCHEDULE_TYPE_ADD_VACATION      = "AV";
    String CODE_SCHEDULE_TYPE_OUTSIDE_WORK      = "OW";
    String CODE_SCHEDULE_TYPE_BUSINESS_TRIP     = "BT";
    String CODE_SCHEDULE_TYPE_OVERTIME_WORK     = "OT";

    String CODE_COMMUTING_REQUEST_TYPE_ON   = "Y";
    String CODE_COMMUTING_REQUEST_TYPE_OFF  = "N";
    String CODE_COMMUTING_REQUEST_TYPE_OVER = "O";
    String CODE_COMMUTING_REQUEST_TYPE_ADD  = "A";

    String CODE_STATUS_READY            = "R";
    String CODE_STATUS_YES              = "Y";
    String CODE_STATUS_NO               = "N";
    String CODE_STATUS_CANCEL_REQUEST   = "C";
    String CODE_STATUS_DELETE           = "D";
    
    
    //OMISSION 용
    String CODE_SCHEDULE_TYPE_OMISSION      = "6";
}
