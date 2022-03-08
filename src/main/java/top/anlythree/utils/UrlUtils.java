package top.anlythree.utils;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.Objects;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:07 上午
 */
@Component
public class UrlUtils {

    private final static String dengyu = "=";

    private final static String wenhao = "?";

    private static StringBuffer urlStrBu = new StringBuffer();

    private static String xiaoyuanUrl = null;

    private static String amapUrl = null;

    private static String sig = null;

    @Value("${xiaoyuan.url}")
    public void setXiaoyuanUrl(String xiaoyuanUrl) {
        UrlUtils.xiaoyuanUrl = xiaoyuanUrl;
    }

    @Value("${amap.url}")
    public void setAmapUrl(String amapUrl) {
        UrlUtils.amapUrl = amapUrl;
    }

    @Value("${amap.sign}")
    public void setSig(String sig) {
        UrlUtils.sig = sig;
    }


    public static String createXiaoYuanUrl(String... params) {
        urlStrBu = new StringBuffer();
        if (params == null || params.length == 0) {
            return StringUtils.EMPTY;
        }
        if ((params.length & 1) == 1) {
            throw new AException("语法错误，参数数量必须是成对存在的");
        }
        urlStrBu.append(xiaoyuanUrl).append(wenhao);
        for (int i = 0; i < params.length; i += 2) {
            urlStrBu.append(params[i]).append(dengyu).append(params[i + 1]).append("&");
        }
        return urlStrBu.toString().substring(0, urlStrBu.length() - 1);
    }

    public static String createAmapUrl(String... params) {
        urlStrBu = new StringBuffer();
        urlStrBu.append(amapUrl);
        if (params == null || params.length == 0) {
            return urlStrBu.toString();
        }
        if ((params.length & 1) == 1) {
            throw new AException("语法错误，参数数量必须是成对存在的");
        }
        urlStrBu.append(wenhao);
        // 生成sig
        StringBuffer createSigBuffer = new StringBuffer();
        for (int i = 0; i < params.length; i += 2) {
            urlStrBu.append(params[i]).append(dengyu).append(params[i + 1]).append("&");
//            if(Objects.equals(params[i],"key")){
//                continue;
//            }
            createSigBuffer.append(params[i]).append(dengyu).append(params[i + 1]).append("&");
        }
        String createSig = createSigBuffer.toString().substring(0, createSigBuffer.length() - 1);
        createSig += sig;
        return urlStrBu.toString().substring(0, urlStrBu.length() - 1)+"&sig="+MD5Utils.getMd5(createSig);
    }

}
