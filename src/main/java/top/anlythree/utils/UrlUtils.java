package top.anlythree.utils;

import org.apache.commons.lang3.StringUtils;
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:07 上午
 */
public class UrlUtils {

    private final static String maohao = ":";

    private final static String wenhao = "?";

    private static StringBuffer urlStrBu = new StringBuffer();

    public static String createXiaoYuan(String ... params){
        if(params == null || params.length == 0){
            return StringUtils.EMPTY;
        }
        if((params.length & 1) == 1){
            throw new AException("语法错误，参数数量必须是成对存在的");
        }
        urlStrBu.append(ACache.getXiaoyuanUrl()).append(wenhao);
        for (int i = 0; i < params.length - 2; i+=2) {
            urlStrBu.append(params[i]).append(maohao).append(params[i+1]);
        }
        return urlStrBu.toString();
    }
}
