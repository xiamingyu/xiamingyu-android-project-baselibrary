package com.ihomefnt.baselibrary.baseutil;

import java.util.List;

/**
 * Created by XiaMingYu on 2016/3/14.
 */
public class ListUtil {
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static String list2String(List<String> list, int spaceSize) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            if (s.equals(list.get(list.size() - 1))) {
                sb.append(s);
            } else if (spaceSize == 2) {
                sb.append(s + "  ");
            } else {
                sb.append(s + " ");
            }
        }
        return sb.toString();
    }
}
