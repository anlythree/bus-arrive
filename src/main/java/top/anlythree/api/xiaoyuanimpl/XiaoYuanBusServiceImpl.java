package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import top.anlythree.api.BusService;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanRouteDTO;
import top.anlythree.utils.MD5Utils;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtils;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/95:18 下午
 */
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
    public List<XiaoYuanBusRes> getBusLocation(String cityName, String routeName, String endStation) {

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
        System.out.println(xiaoYuanModel);
        return null;
    }
}
