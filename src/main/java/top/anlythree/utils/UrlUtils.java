package top.anlythree.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:07 上午
 */
public class UrlUtils {

    private final static String dengyu = "=";

    private final static String wenhao = "?";

    private static StringBuffer urlStrBu = new StringBuffer();

    @Value("${xiaoyuan.url}")
    private static String xiaoyuanUrl;

    public static String createXiaoYuan(String ... params){
        urlStrBu = new StringBuffer();
        if(params == null || params.length == 0){
            return StringUtils.EMPTY;
        }
        if((params.length & 1) == 1){
            throw new AException("语法错误，参数数量必须是成对存在的");
        }
        urlStrBu.append(xiaoyuanUrl).append(wenhao);
        for (int i = 0; i < params.length; i+=2) {
            urlStrBu.append(params[i]).append(dengyu).append(params[i+1]).append("&");
        }
        return urlStrBu.toString().substring(0,urlStrBu.length()-1);
    }
}
