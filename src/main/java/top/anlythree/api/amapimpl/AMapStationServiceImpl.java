package top.anlythree.api.amapimpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.api.amapimpl.res.AMapStationListRes;
import top.anlythree.cache.ACache;
import top.anlythree.utils.RestTemplateUtil;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtil;
import top.anlythree.utils.exceptions.AException;

@Service
public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Override
    public StationDTO getStation(String cityName, String district, String stationName) {
        for (StationDTO stationDTO : ACache.getStationCacheList()) {
            if(StringUtils.isNotEmpty(stationDTO.getStationFullName()) &&
                    stationDTO.getStationFullName().contains(stationName) &&
                    stationDTO.getStationFullName().contains(district) &&
                    stationDTO.getStationFullName().contains(cityName)){
                return stationDTO;
            }
        }
        AMapStationListRes locationByName = getLocationByName(cityName, district + "区" + stationName + "公交站");
        AMapStationListRes.AMapStationRes oneStationRes = locationByName.getOneStationRes(stationName);
        if (null == oneStationRes) {
            throw new AException("获取不到" + stationName + "站的具体坐标，请尝试自定义关键字来获取站点名称");
        }
        // 添加缓存
        ACache.addStation(oneStationRes.castStationDto(stationName));
        return oneStationRes.castStationDto(stationName);
    }

    @Override
    public AMapStationListRes getLocationByName(String cityName, String keyWord) {
        String amapUrl = UrlUtil.createAmapUrl(
                "getStation",
                new UrlUtil.UrlParam("key", key),
                new UrlUtil.UrlParam("city", cityName),
                new UrlUtil.UrlParam("batch", "2"),
                new UrlUtil.UrlParam("output", "JSON"),
                new UrlUtil.UrlParam("address", keyWord));
        return ResultUtil.getAMapModel(
                RestTemplateUtil.get(amapUrl, AMapStationListRes.class));
    }
}
