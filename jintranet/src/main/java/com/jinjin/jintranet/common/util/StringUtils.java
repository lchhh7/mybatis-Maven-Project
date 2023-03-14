package com.jinjin.jintranet.common.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String under10(int i) {
        return i < 10 ? "0" + i : Integer.toString(i);
    }

    public static List<String> subStrByByte(String str) {
        List<String> result = new ArrayList<>();
        final int CUT_LENGTH = 3998;

        if (!str.isEmpty()) {
            str = str.trim();
            if (str.getBytes().length <= CUT_LENGTH) {
                result.add(str);
            } else {

                StringBuffer sb = new StringBuffer(CUT_LENGTH);
                int cnt = 0;
                int length = str.toCharArray().length;
                int total = 0;
                for (char ch : str.toCharArray()) {
                    cnt += String.valueOf(ch).getBytes().length;
                    total++;
                    sb.append(ch);
                    if (cnt > CUT_LENGTH) {
                        result.add(sb.toString());
                        sb.setLength(0);
                        cnt = 0;
                        continue;
                    }

                    if (total == length) {
                        result.add(sb.toString());
                        break;
                    }
                }
            }
        }

        return result;
    }
}
