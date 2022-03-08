package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.dto.StationDTO;
import top.anlythree.api.amapimpl.res.station.AMapStationListRes;
import top.anlythree.api.amapimpl.res.station.AMapStationRes;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;

public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Value("${amap.sign}")
    private final String sign = null;

    @Override
    public StationDTO getStation(String cityName, String stationName) {
        String amapUrl = UrlUtils.createAmapUrl("key", key, "sig", sign, "city", cityName, "batch", "5",
                "output", "JSON", "address", "stationName");
        AMapStationListRes getStationListRes = ResultUtil.getAMapModel(
                RestTemplateUtils.get(amapUrl, AMapStationListRes.class));
        if(null == getStationListRes){
            return null;
        }
        AMapStationRes oneStationRes = getStationListRes.getOneStationRes(stationName);
        return oneStationRes.castStationDto(stationName);
    }
}
