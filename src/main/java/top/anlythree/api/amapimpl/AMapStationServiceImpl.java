package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.enums.UrlTypeEnum;
import top.anlythree.bussiness.dto.LocationDTO;
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
        StationDTO locationByKeyWord = ACache.getLocationByKeyWord(cityName + "市" + district + "区" + stationName + "公交站");
        if(null != locationByKeyWord){
            return locationByKeyWord;
        }
        AMapStationListRes locationByName = getLocationByNameFromAMap(cityName, district + "区" + stationName + "公交站");
        AMapStationListRes.AMapStationRes oneStationRes = locationByName.getOneStationRes(stationName);
        if (null == oneStationRes) {
            throw new AException("获取不到" + stationName + "站的具体坐标，请尝试自定义关键字来获取站点名称");
        }
        // 添加缓存
        ACache.addLocation(oneStationRes.castStationDto(stationName));
        return oneStationRes.castStationDto(stationName);
    }

    @Override
    public LocationDTO getLocationByName(String cityName, String keyWord) {
        StationDTO locationByKeyWord = ACache.getLocationByKeyWord(keyWord);
        if(null != locationByKeyWord){
            return new LocationDTO(locationByKeyWord);
        }
        AMapStationListRes.AMapStationRes oneLocationRes = getLocationByNameFromAMap(cityName, keyWord).getOneLocationRes(keyWord);
        Assert.notNull(oneLocationRes,"未找到该位置,keyWord:"+keyWord);
        return new LocationDTO(oneLocationRes.castStationDto(keyWord));
    }

    @Override
    public AMapStationListRes getLocationByNameFromAMap(String cityName, String keyWord) {
        String amapUrl = UrlUtil.createAmapUrl(
                UrlTypeEnum.LOCATION,
                new UrlUtil.UrlParam("key", key),
                new UrlUtil.UrlParam("city", cityName),
                new UrlUtil.UrlParam("batch", "2"),
                new UrlUtil.UrlParam("output", "JSON"),
                new UrlUtil.UrlParam("address", keyWord));
        return ResultUtil.getAMapModel(
                RestTemplateUtil.get(amapUrl, AMapStationListRes.class));
    }
}
