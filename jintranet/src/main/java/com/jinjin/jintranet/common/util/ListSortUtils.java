package com.jinjin.jintranet.common.util;

import java.util.Comparator;

import com.jinjin.jintranet.common.vo.CommutingVO;

public class ListSortUtils implements Comparator<CommutingVO>{

	public int compare(CommutingVO commutingVO1,CommutingVO commutingVO2) {
		return (commutingVO1.getId() > commutingVO2.getId()) ?  1 :
			 (commutingVO1.getId() < commutingVO2.getId()) ?  -1 : 0;
	}
}
