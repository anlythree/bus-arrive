package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.StationService;
import top.anlythree.bussiness.dto.StationDTO;
import top.anlythree.api.amapimpl.res.station.AMapStationListRes;
import top.anlythree.api.amapimpl.res.station.AMapStationRes;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;
import top.anlythree.utils.exceptions.AException;

@Service
public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Override
    public StationDTO getStation(String cityName,String district, String stationName) {
        String amapUrl = UrlUtils.createAmapUrl(
                new UrlUtils.UrlParam("key", key),
                new UrlUtils.UrlParam("city", cityName),
                new UrlUtils.UrlParam( "batch", "5"),
                new UrlUtils.UrlParam("output", "JSON"),
                new UrlUtils.UrlParam( "address", district+"区"+stationName+"公交站"));
        AMapStationListRes getStationListRes = ResultUtil.getAMapModel(
                RestTemplateUtils.get(amapUrl, AMapStationListRes.class));
        if(null == getStationListRes){
            return null;
        }
        AMapStationRes oneStationRes = getStationListRes.getOneStationRes(stationName);
        if(null == oneStationRes){
            throw new AException("获取不到"+stationName+"站的具体坐标，请尝试自定义关键字来获取站点名称");
        }
        return oneStationRes.castStationDto(stationName);
    }
}
