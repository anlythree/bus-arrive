package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.dto.AMapStationDTO;
import top.anlythree.api.amapimpl.res.station.GetStationListRes;
import top.anlythree.api.amapimpl.res.station.StationRes;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;

public class AMapStationServiceImpl implements StationService {

    @Value("${amap.key}")
    private final String key = null;

    @Value("${amap.sign}")
    private final String sign = null;

    @Override
    public AMapStationDTO getStation(String cityName, String stationName) {
        String amapUrl = UrlUtils.createAmapUrl("key", key, "sig", sign, "city", cityName, "batch", "5",
                "output", "JSON", "address", "stationName");
        GetStationListRes getStationListRes = ResultUtil.getAMapModel(
                RestTemplateUtils.get(amapUrl, GetStationListRes.class));
        // 从5个搜索结果中找到最符合的那个位置
        for (StationRes getStationRes : getStationListRes.getGeocodes()) {

        }
        return null;
    }
}
