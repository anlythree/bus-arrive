package top.anlythree.api.xiaoyuanimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.xiaoyuanimpl.res.route.XiaoYuanRouteListRes;
import top.anlythree.api.xiaoyuanimpl.res.route.XiaoYuanRouteRes;
import top.anlythree.cache.ACache;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;
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
    public List<Route> getRouteListByNameAndCityId(String routeName, String cityId) {
        City cityById = cityService.getCityById(cityId);
        if(null == cityById){
            throw new AException("cityId错误，查不到指定的cityId，cityId："+cityId);
        }
        String keySecret = MD5Utils.getMd5(uname + key + "luxian");
        XiaoYuanRouteListRes xiaoYuanRouteListRes = ResultUtil.getXiaoYuanModel(
                RestTemplateUtils.get(UrlUtils.createXiaoYuan(
                        "optype","luxian",
                                "uname",uname,
                                "cityid",cityId,
                                "keywords",routeName,
                                "keySecret",keySecret),
                        XiaoYuanRouteListRes.class));
        List<XiaoYuanRouteRes> xiaoYuanRouteResList = xiaoYuanRouteListRes.getReturlList();
        if(CollectionUtils.isEmpty(xiaoYuanRouteResList)){
            return null;
        }
        return xiaoYuanRouteResList.stream().map(f-> f.castRoute(cityId)).collect(Collectors.toList());
    }

    @Override
    public Route getRoutByNameAndCityIdAndStartStation(String routeName, String cityName, String startStation) {
        City cityByName = cityService.getCityByName(cityName);
        List<Route> routeCacheList = ACache.getRouteCacheList();
        for (Route route : routeCacheList) {
            if(Objects.equals(routeName,route.getRouteName()) &&
            Objects.equals(cityByName.getId(),route.getCityId()) &&
            Objects.equals(startStation,route.getStartStation())){
                return route;
            }
        }
        // 缓存中找不到，找到并加载到缓存
        // 重新在缓存中查找
        cacheRouteByNameAndCityName(cityName,routeName);
        List<Route> newRouteCacheList = ACache.getRouteCacheList();
        for (Route route : newRouteCacheList) {
            if(Objects.equals(routeName,route.getRouteName()) &&
                    Objects.equals(cityByName.getId(),route.getCityId()) &&
                    Objects.equals(startStation,route.getStartStation())){
                return route;
            }
        }
        return null;
    }

    @Override
    public void cacheRouteByNameAndCityName(String cityName, String routeName) {

        City cityByName = cityService.getCityByName(cityName);
        List<Route> allRouteList = getRouteListByNameAndCityId(routeName, cityByName.getId());
        if (CollectionUtils.isEmpty(allRouteList)) {
            log.error("根据位置路线名查询不到相关线路，缓存该线路失败：城市名称：" + cityName+"公交路线名称："+routeName );
            return;
        }
        for (Route route : allRouteList) {
            if(Objects.equals(route.getRouteName(), routeName)){
                //  这里手动剔除掉api查出来的模糊数据，只取路线名称完全一样的公交路线
                ACache.addRoute(route);
            }
        }
    }
}
