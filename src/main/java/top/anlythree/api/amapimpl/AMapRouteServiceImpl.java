package top.anlythree.api.amapimpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.RouteService;
import top.anlythree.api.StationService;
import top.anlythree.api.amapimpl.enums.UrlTypeEnum;
import top.anlythree.api.amapimpl.res.AMapBusRouteRes;
import top.anlythree.api.amapimpl.res.AMapCityRes;
import top.anlythree.api.amapimpl.res.AMapWalkRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.RestTemplateUtil;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.TimeUtil;
import top.anlythree.utils.UrlUtil;
import top.anlythree.utils.exceptions.AException;

import java.time.LocalDateTime;
import java.util.Comparator;
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

    @Autowired
    @Qualifier(value = "AMapStationServiceImpl")
    private StationService stationService;

    @Override
    public List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityAndEndStation(String cityName, String routeName, String endStationName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteFromCache(String cityId, String routeName, String endStationName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public void cacheRouteByNameAndCityName(String cityName, String routeName) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String cityName, String routeName, String rideStart, String rideEnd) {
        throw new AException("no suport impl, use begin with XiaoYuan……class to impl");
    }

    @Override
    public AMapBusRouteRes getBusRouteByLocation(String cityName, String startLocationLal, String endLocationLal, LocalDateTime dateTime) {
        String time = null;
        String date = null;
        if (null != dateTime) {
            String[] dateAndTimeByDateTimeStr = TimeUtil.getDateAndTimeByTime(dateTime);
            date = dateAndTimeByDateTimeStr[0];
            time = dateAndTimeByDateTimeStr[1];
        }
        String amapUrl = UrlUtil.createAmapUrl(UrlTypeEnum.BUS_ROUTE,
                new UrlUtil.UrlParam("key", key),
                new UrlUtil.UrlParam("city", cityName),
                new UrlUtil.UrlParam("origin", startLocationLal),
                new UrlUtil.UrlParam("date", date),
                new UrlUtil.UrlParam("time", time),
                new UrlUtil.UrlParam("destination", endLocationLal),
                new UrlUtil.UrlParam("strategy","2"));
        return ResultUtil.getAMapModel(RestTemplateUtil.get(amapUrl, AMapBusRouteRes.class));
    }

    @Override
    public AMapBusRouteRes.AMapBusRouteInfo.TransitsInfo getBusTransitsByLocation(String cityName, String routeName, String startLocationLal, String endLocationLal, LocalDateTime dateTime) {
        AMapBusRouteRes busRouteTimeByLocation = getBusRouteByLocation(cityName, startLocationLal, endLocationLal, dateTime);
        if (null == busRouteTimeByLocation ||
                null == busRouteTimeByLocation.getRoute() ||
                CollectionUtils.isEmpty(busRouteTimeByLocation.getRoute().getTransits())) {
            throw new AException("查询不到直达的" + routeName + "公交方案，来自高德api的信息：" + busRouteTimeByLocation);
        }
        return busRouteTimeByLocation.getTransitsByRouteName(routeName);
    }

    @Override
    public AMapWalkRouteTimeRes.Route.Path getWalkSecondsByLocation(String cityName, String startLocationLal, String endLocationLal, LocalDateTime dateTime) {
        String date = null;
        String time = null;
        if (null != dateTime) {
            String[] dateAndTimeByDateTimeStr = TimeUtil.getDateAndTimeByTime(dateTime);
            date = dateAndTimeByDateTimeStr[0];
            time = dateAndTimeByDateTimeStr[1];
        }
        String amapUrl = UrlUtil.createAmapUrl(UrlTypeEnum.WALK_ROUTE,
                new UrlUtil.UrlParam("key", key),
                new UrlUtil.UrlParam("city", cityName),
                new UrlUtil.UrlParam("origin", startLocationLal),
                new UrlUtil.UrlParam("destination", endLocationLal),
                new UrlUtil.UrlParam("date", date),
                new UrlUtil.UrlParam("time", time));
        AMapWalkRouteTimeRes aMapModel = ResultUtil.getAMapModel(RestTemplateUtil.get(amapUrl, AMapWalkRouteTimeRes.class));
        if(null == aMapModel.getRoute() || CollectionUtils.isEmpty(aMapModel.getRoute().getPaths())) {
            throw new AException("起点：" + startLocationLal + "终点：" + endLocationLal + "查询不到步行方案");
        }
        return aMapModel.getRoute().getPaths().stream().min(Comparator.comparing(AMapWalkRouteTimeRes.Route.Path::getDuration)).get();
    }

    @Override
    public AMapWalkRouteTimeRes.Route.Path getWalkSecondsByLocationName(String cityName, String startLocationName, String endLocationName, LocalDateTime dateTime) {
        LocationDTO startLocationByName = stationService.getLocationByName(cityName, startLocationName);
        LocationDTO endLocationByName = stationService.getLocationByName(cityName, endLocationName);
        return this.getWalkSecondsByLocation(cityName,startLocationByName.getLongitudeAndLatitude(),endLocationByName.getLongitudeAndLatitude(),dateTime);
    }

    @Override
    public String getCityCodeByName(String cityName) {
        String cityCodeFromCache = ACache.aMapCityList.get(cityName);
        if(StringUtils.isNotEmpty(cityCodeFromCache)){
            return cityCodeFromCache;
        }
        String cityCodeNo = getCityCodeNoCacheByName(cityName);
        ACache.aMapCityList.put(cityName,cityCodeNo);
        return cityCodeNo;
    }

    @Override
    public String getCityCodeNoCacheByName(String cityName) {
        AMapCityRes aMapModel = ResultUtil.getAMapModel(RestTemplateUtil.get(
                UrlUtil.createAmapUrl(UrlTypeEnum.DISTRICT,
                        new UrlUtil.UrlParam("key", key),
                        new UrlUtil.UrlParam("keywords", cityName))
                , AMapCityRes.class));
        return aMapModel.getCityCode();
    }
}
