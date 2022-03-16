package top.anlythree.api.amapimpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.api.amapimpl.res.AMapStationListRes;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;
import top.anlythree.utils.exceptions.AException;

@Service
public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Override
    public StationDTO getStation(String cityName, String district, String stationName) {
        AMapStationListRes locationByName = getLocationByName(cityName, district + "区" + stationName + "公交站");
        AMapStationListRes.AMapStationRes oneStationRes = locationByName.getOneStationRes(stationName);
        if (null == oneStationRes) {
            throw new AException("获取不到" + stationName + "站的具体坐标，请尝试自定义关键字来获取站点名称");
        }
        return oneStationRes.castStationDto(stationName);
    }

    @Override
    public AMapStationListRes getLocationByName(String cityName, String keyWord) {
        String amapUrl = UrlUtils.createAmapUrl(
                "getStation",
                new UrlUtils.UrlParam("key", key),
                new UrlUtils.UrlParam("city", cityName),
                new UrlUtils.UrlParam("batch", "2"),
                new UrlUtils.UrlParam("output", "JSON"),
                new UrlUtils.UrlParam("address", keyWord));
        return ResultUtil.getAMapModel(
                RestTemplateUtils.get(amapUrl, AMapStationListRes.class));
    }
}
