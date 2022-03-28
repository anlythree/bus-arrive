package top.anlythree.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author anlythree
 * @description: 字符串工具类
 * @time 2022/3/2815:35
 */
public class AStrUtil {

    /**
     * 比较两个字符串是否相似
     * @param sourceStr
     * @param targetStr
     * @return
     */
    public static Boolean isSame(String sourceStr,String targetStr){
        //  比较下原字符串是否一半以上的字符是不同的，只要少于一半不同的字符就认为这两个字符串相似
        return sourceStr.length() >> 1 > StringUtils.getLevenshteinDistance(sourceStr, targetStr);
    }
}
