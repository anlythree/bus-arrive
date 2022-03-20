package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.BusService;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.bussiness.dto.BusDTO;
import top.anlythree.cache.ACache;
import top.anlythree.utils.MD5Util;
import top.anlythree.utils.RestTemplateUtil;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtil;

import java.math.BigDecimal;
import java.util.List;
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
        XiaoYuanBusRes xiaoYuanModel = getXiaoYuanBusRes(cityName, routeName, endStation);
        if (xiaoYuanModel == null) return null;
        // 返回公交列表
        if(CollectionUtils.isEmpty(xiaoYuanModel.getReturlList().getBuses())){
            return null;
        }
        return xiaoYuanModel.getReturlList().getBuses().stream().map(XiaoYuanBusRes.BusInfoRes::castBusDTO).collect(Collectors.toList());
    }

    @Override
    public XiaoYuanBusRes getXiaoYuanBusRes(String cityName, String routeName, String endStation) {
        XiaoYuanCityDTO cityByName = cityService.getCityByName(cityName);
        XiaoYuanRouteDTO route = routeService.getRouteByNameAndCityIdAndStartStation(routeName, cityName, endStation);
        String getBusLocationUrl = UrlUtil.createXiaoYuanUrl(
                new UrlUtil.UrlParam("optype", "rtbus"),
                new UrlUtil.UrlParam("uname", uname),
                new UrlUtil.UrlParam("cityid", cityByName.getId()),
                new UrlUtil.UrlParam("bus_linestrid", route.getRouteId()),
                new UrlUtil.UrlParam("bus_linenum", route.getRouteCode()),
                new UrlUtil.UrlParam("bus_staname", route.getRouteName()),
                new UrlUtil.UrlParam("keySecret", MD5Util.getMd5(uname + key + "rtbus"))
        );
        XiaoYuanBusRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(RestTemplateUtil.get(getBusLocationUrl, XiaoYuanBusRes.class));
        if(null == xiaoYuanModel || null == xiaoYuanModel.getReturlList()){
            return null;
        }
        // 把新的路线信息填充到缓存中
        XiaoYuanRouteDTO routeByNameAndCityIdAndStartStation = routeService.getRouteByNameAndCityIdAndStartStation(routeName, cityName, endStation);
        XiaoYuanBusRes.LineInfoRes lineinfo = xiaoYuanModel.getReturlList().getLineinfo();
        routeByNameAndCityIdAndStartStation.setFirstTime(lineinfo.getFirTime());
        routeByNameAndCityIdAndStartStation.setEndTime(lineinfo.getEndTime());
        routeByNameAndCityIdAndStartStation.setMoneyQty(new BigDecimal(lineinfo.getBusMoney()));
        routeByNameAndCityIdAndStartStation.setStationList(xiaoYuanModel.getReturlList().getStations().stream()
                .map(XiaoYuanBusRes.StationInfoRes::getBusStaname).collect(Collectors.toList()));
        ACache.addRoute(routeByNameAndCityIdAndStartStation);
        return xiaoYuanModel;
    }
}
