package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.dto.StationDTO;
import top.anlythree.api.amapimpl.res.station.AMapStationListRes;
import top.anlythree.api.amapimpl.res.station.AMapStationRes;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;

@Service
public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Override
    public StationDTO getStation(String cityName,String routeName, String stationName) {
        String amapUrl = UrlUtils.createAmapUrl("key", key, "city", cityName, "batch", "5",
                "output", "JSON", "address", routeName+"路"+stationName+"(公交站)");
        AMapStationListRes getStationListRes = ResultUtil.getAMapModel(
                RestTemplateUtils.get(amapUrl, AMapStationListRes.class));
        if(null == getStationListRes){
            return null;
        }
        AMapStationRes oneStationRes = getStationListRes.getOneStationRes(stationName);
        return oneStationRes.castStationDto(stationName);
    }
}
