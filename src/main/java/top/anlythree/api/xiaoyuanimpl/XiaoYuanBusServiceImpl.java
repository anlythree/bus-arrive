package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.BusService;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.MD5Utils;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;
import top.anlythree.utils.exceptions.AException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/95:18 下午
 */
@Service
public class XiaoYuanBusServiceImpl implements BusService {

    @Value("${xiaoyuan.username}")
    private String uname;

    @Value("${xiaoyuan.key}")
    private String key;

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;

    @Autowired
    @Qualifier(value = "xiaoYuanRouteServiceImpl")
    private RouteService routeService;

    @Override
    public List<BusDTO> getBusLocationList(String cityName, String routeName, String endStation) {
        XiaoYuanCityDTO cityByName = cityService.getCityByName(cityName);
        XiaoYuanRouteDTO route = routeService.getRouteByNameAndCityIdAndStartStation(routeName, cityName, endStation);
        String getBusLocationUrl = UrlUtils.createXiaoYuanUrl(
                new UrlUtils.UrlParam("optype", "rtbus"),
                new UrlUtils.UrlParam("uname", uname),
                new UrlUtils.UrlParam("cityid", cityByName.getId()),
                new UrlUtils.UrlParam("bus_linestrid", route.getRouteId()),
                new UrlUtils.UrlParam("bus_linenum", route.getRouteCode()),
                new UrlUtils.UrlParam("bus_staname", route.getRouteName()),
                new UrlUtils.UrlParam("keySecret", MD5Utils.getMd5(uname + key + "rtbus"))
        );
        XiaoYuanBusRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(RestTemplateUtils.get(getBusLocationUrl, XiaoYuanBusRes.class));
        if(null == xiaoYuanModel || null == xiaoYuanModel.getReturlList()){
            return null;
        }
        // 把新的路线信息填充到缓存中
        XiaoYuanRouteDTO routeByNameAndCityIdAndStartStation = routeService.getRouteByNameAndCityIdAndStartStation(routeName, cityName, endStation);
        XiaoYuanBusRes.LineInfoRes lineinfo = xiaoYuanModel.getReturlList().getLineinfo();
        routeByNameAndCityIdAndStartStation.setFirstTime(lineinfo.getFirTime());
        routeByNameAndCityIdAndStartStation.setEndTime(lineinfo.getEndTime());
        routeByNameAndCityIdAndStartStation.setMoneyQty(new BigDecimal(lineinfo.getBusMoney()));
        ACache.addRoute(routeByNameAndCityIdAndStartStation);
        // 返回公交列表
        if(CollectionUtils.isEmpty(xiaoYuanModel.getReturlList().getBuses())){
            return null;
        }
        return xiaoYuanModel.getReturlList().getBuses().stream().map(XiaoYuanBusRes.BusInfoRes::castBusDTO).collect(Collectors.toList());
    }
}
