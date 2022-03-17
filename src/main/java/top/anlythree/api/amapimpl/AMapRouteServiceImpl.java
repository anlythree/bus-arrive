package top.anlythree.api.amapimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.RouteService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.TimeUtils;
import top.anlythree.utils.UrlUtils;
import top.anlythree.utils.exceptions.AException;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/1610:31 上午
 */
@Service
public class AMapRouteServiceImpl implements RouteService {

    @Value("${amap.key}")
    private final String key = null;

    @Override
    public List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteFromCache(String cityId, String routeName, String startStation) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public void cacheRouteByNameAndCityName(String cityName, String routeName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String routeName, String cityName, String rideStart, String rideEnd) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public AMapBusRouteTimeRes getBusRouteTimeByLocation(String cityName,String startLocation, String endLocation,String dateTime) {
        String time = "";
        String date = "";
        if(null != dateTime){
            String[] dateAndTimeByDateTimeStr = TimeUtils.getDateAndTimeByDateTimeStr(dateTime);
            date = dateAndTimeByDateTimeStr[0];
            time = dateAndTimeByDateTimeStr[1];
        }
        String amapUrl = UrlUtils.createAmapUrl("getBusRouteTime",
                new UrlUtils.UrlParam("key", key),
                new UrlUtils.UrlParam("city",cityName),
                new UrlUtils.UrlParam("origin", startLocation),
                new UrlUtils.UrlParam("date",date),
                new UrlUtils.UrlParam("time",time),
                new UrlUtils.UrlParam("destination", endLocation));
        return ResultUtil.getAMapModel(RestTemplateUtils.get(amapUrl, AMapBusRouteTimeRes.class));
    }

    @Override
    public Integer getSecondsByBus(String cityName, String startLocation, String endLocation, String busName, String dateTime) {
        getBusRouteTimeByLocation(cityName,startLocation,endLocation,dateTime);
        return null;
    }
}
