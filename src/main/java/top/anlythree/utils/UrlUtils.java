package top.anlythree.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.anlythree.cache.ACache;
import top.anlythree.utils.exceptions.AException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public static String createXiaoYuanUrl(UrlParam... params) {
        urlStrBu = new StringBuffer();
        if (params == null || params.length == 0) {
            return StringUtils.EMPTY;
        }
        urlStrBu.append(xiaoyuanUrl).append(wenhao);
        for (int i = 0; i < params.length; i++) {
            urlStrBu.append(params[i].getKey()).append(dengyu).append(params[i].getValue()).append("&");
        }
        return urlStrBu.toString().substring(0, urlStrBu.length() - 1);
    }

    public static String createAmapUrl(UrlParam... params) {
        urlStrBu = new StringBuffer();
        urlStrBu.append(amapUrl);
        if (params == null || params.length == 0) {
            return urlStrBu.toString();
        }
        urlStrBu.append(wenhao);
        // 生成sig
        StringBuffer createSigBuffer = new StringBuffer();
        // 根据key来排序
        Arrays.stream(params)
                .sorted(Comparator.comparing(UrlParam::getKey))
                .forEach(urlParam -> {
                    urlStrBu.append(urlParam.getKey()).append(dengyu).append(urlParam.getValue()).append("&");
                    createSigBuffer.append(urlParam.getKey()).append(dengyu).append(urlParam.getValue()).append("&");
                });
        String createSig = createSigBuffer.toString().substring(0, createSigBuffer.length() - 1);
        createSig += sig;
        return urlStrBu.toString().substring(0, urlStrBu.length() - 1) + "&sig=" + MD5Utils.getMd5(createSig);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UrlParam {

        private String key;

        private String value;
    }

}
