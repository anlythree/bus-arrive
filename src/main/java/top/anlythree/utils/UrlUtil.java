package top.anlythree.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:07 上午
 */
@Component
public class UrlUtil {

    private final static String dengyu = "=";

    private final static String wenhao = "?";

    private static StringBuffer urlStrBu = new StringBuffer();

    private static String xiaoyuanUrl = null;

    private static String amapGetStationUrl = null;

    private static String amapGetBusRouteTime = null;

    private static String sig = null;

    @Value("${xiaoyuan.url}")
    public void setXiaoyuanUrl(String xiaoyuanUrl) {
        UrlUtil.xiaoyuanUrl = xiaoyuanUrl;
    }

    @Value("${amap.stationUrl}")
    public void setAmapGetStationUrl(String amapUrl) {
        UrlUtil.amapGetStationUrl = amapUrl;
    }

    @Value("${amap.busRouteTimeUrl}")
    public void setAmapGetBusRouteTime(String amapUrl) {
        UrlUtil.amapGetBusRouteTime = amapUrl;
    }

    @Value("${amap.sign}")
    public void setSig(String sig) {
        UrlUtil.sig = sig;
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

    /**
     * todo-anlythree 抽取枚举类
     * @param type
     * @param params
     * @return
     */
    public static String createAmapUrl(String type, UrlParam... params) {
        urlStrBu = new StringBuffer();
        if(StringUtils.equals("getStation",type)){
            urlStrBu.append(amapGetStationUrl);
        }else if(StringUtils.equals("getBusRouteTime",type)){
            urlStrBu.append(amapGetBusRouteTime);
        }
        if (params == null || params.length == 0) {
            return urlStrBu.toString();
        }
        urlStrBu.append(wenhao);
        // 生成sig
        StringBuffer createSigBuffer = new StringBuffer();
        // 根据key来排序
        Arrays.stream(params)
                .sorted(Comparator.comparing(UrlParam::getKey))
                .filter(urlParam -> urlParam.getValue() != null && urlParam.getKey() != null)
                .forEach(urlParam -> {
                    urlStrBu.append(urlParam.getKey()).append(dengyu).append(urlParam.getValue()).append("&");
                    createSigBuffer.append(urlParam.getKey()).append(dengyu).append(urlParam.getValue()).append("&");
                });
        if(StringUtils.isEmpty(createSigBuffer)){
            // 如果没有传任何参数，那么createSigBuffer会是一个空字符串，直接返回，或者不需要拼接sig也直接返回
            return urlStrBu.toString().substring(0, urlStrBu.length() - 1);
        }
        String createSig = createSigBuffer.toString().substring(0, createSigBuffer.length() - 1);
        createSig += sig;
        return urlStrBu.toString().substring(0, urlStrBu.length() - 1) + "&sig=" + MD5Util.getMd5(createSig);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UrlParam {

        private String key;

        private String value;
    }

}
