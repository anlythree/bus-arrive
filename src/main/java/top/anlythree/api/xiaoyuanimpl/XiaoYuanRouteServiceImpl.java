package top.anlythree.api.xiaoyuanimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanRouteListRes;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.MD5Utils;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;
import top.anlythree.utils.exceptions.AException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XiaoYuanRouteServiceImpl implements RouteService {

    @Value("${xiaoyuan.username}")
    private String uname;

    @Value("${xiaoyuan.key}")
    private String key;

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;


    @Override
    public List<XiaoYuanRouteDTO> getRouteListByNameAndCityName(String routeName, String cityName) {
        XiaoYuanCityDTO cityById = cityService.getCityByName(cityName);
        if(null == cityById){
            throw new AException("cityId错误，查不到指定的cityId，cityName："+cityName);
        }
        String keySecret = MD5Utils.getMd5(uname + key + "luxian");
        XiaoYuanRouteListRes xiaoYuanRouteListRes = ResultUtil.getXiaoYuanModel(
                RestTemplateUtils.get(UrlUtils.createXiaoYuanUrl(
                        new UrlUtils.UrlParam("optype","luxian"),
                        new UrlUtils.UrlParam("uname",uname),
                        new UrlUtils.UrlParam("cityid",cityById.getId()),
                        new UrlUtils.UrlParam("keywords",routeName),
                        new UrlUtils.UrlParam("keySecret",keySecret)),
                        XiaoYuanRouteListRes.class));
        List<XiaoYuanRouteListRes.XiaoYuanRouteRes> xiaoYuanRouteResList = xiaoYuanRouteListRes.getReturlList();
        if(CollectionUtils.isEmpty(xiaoYuanRouteResList)){
            return null;
        }
        return xiaoYuanRouteResList.stream()
                .filter(f -> Objects.equals(f.getBusStaname(), routeName))
                .map(f-> f.castRoute(cityById.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityIdAndStartStation(String routeName, String cityName, String endStation) {
        XiaoYuanCityDTO cityByName = cityService.getCityByName(cityName);
        XiaoYuanRouteDTO routeFromCache = getRouteFromCache(cityByName.getId(), routeName, endStation);
        if(null != routeFromCache){
            return routeFromCache;
        }
        // 缓存中找不到，找到并加载到缓存
        cacheRouteByNameAndCityName(cityName,routeName);
        // 重新在缓存中查找
        XiaoYuanRouteDTO routeFromCacheDTO = getRouteFromCache(cityByName.getId(), routeName, endStation);
        if(null == routeFromCacheDTO){
            throw new AException("查找不到路线信息："+cityName+"，"+routeName+"，"+endStation);
        }
        return routeFromCacheDTO;
    }

    @Override
    public XiaoYuanRouteDTO getRouteFromCache(String cityId,String routeName,String endStation){
        List<XiaoYuanRouteDTO> routeCacheList = ACache.getRouteCacheList();
        for (XiaoYuanRouteDTO route : routeCacheList) {
            if(Objects.equals(routeName,route.getRouteName()) &&
                    Objects.equals(cityId,route.getCityId()) &&
                    Objects.equals(endStation,route.getEndStation())){
                return route;
            }
        }
        return null;
    }

    @Override
    public void cacheRouteByNameAndCityName(String cityName, String routeName) {
        List<XiaoYuanRouteDTO> allRouteList = getRouteListByNameAndCityName(routeName, cityName);
        if (CollectionUtils.isEmpty(allRouteList)) {
            log.error("根据位置路线名查询不到相关线路，缓存该线路失败：城市名称：" + cityName+"公交路线名称："+routeName );
            return;
        }
        allRouteList.forEach(ACache::addRoute);
    }

    @Override
    public XiaoYuanRouteDTO getRouteByNameAndCityAndRideStartAndRideEnd(String routeName, String cityName, String rideStart, String rideEnd) {
        List<XiaoYuanRouteDTO> allRouteList = getRouteListByNameAndCityName(routeName, cityName);
        int rideStartIndex = 0;
        int rideEndIndex = 0;
        for (XiaoYuanRouteDTO route : allRouteList) {

        }
        return null;
    }

    @Override
    public AMapBusRouteTimeRes getBusRouteTimeByCityNameAndRouteNameAndEndStation(String cityName, String routeName, String endStation, String startLocation, String endLocation) {
        throw new AException("no suport impl, use begin with AMap……class to impl");
    }
}
