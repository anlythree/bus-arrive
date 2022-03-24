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
    public List<BusDTO> getBusLocationList(String cityName, XiaoYuanRouteDTO routeDTO) {
        XiaoYuanBusRes xiaoYuanModel = getXiaoYuanBusRes(cityName, routeDTO);
        if (xiaoYuanModel == null) {
            return null;
        }
        // 返回公交列表
        if(CollectionUtils.isEmpty(xiaoYuanModel.getReturlList().getBuses())){
            return null;
        }
        return xiaoYuanModel.getReturlList().getBuses().stream().map(XiaoYuanBusRes.BusInfoRes::castBusDTO).collect(Collectors.toList());
    }

    @Override
    public XiaoYuanBusRes getXiaoYuanBusRes(String cityName, XiaoYuanRouteDTO routeDTO) {
        XiaoYuanCityDTO cityByName = cityService.getCityByName(cityName);
        String getBusLocationUrl = UrlUtil.createXiaoYuanUrl(
                new UrlUtil.UrlParam("optype", "rtbus"),
                new UrlUtil.UrlParam("uname", uname),
                new UrlUtil.UrlParam("cityid", cityByName.getId()),
                new UrlUtil.UrlParam("bus_linestrid", routeDTO.getRouteId()),
                new UrlUtil.UrlParam("bus_linenum", routeDTO.getRouteCode()),
                new UrlUtil.UrlParam("bus_staname", routeDTO.getRouteName()),
                new UrlUtil.UrlParam("keySecret", MD5Util.getMd5(uname + key + "rtbus"))
        );
        XiaoYuanBusRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(RestTemplateUtil.get(getBusLocationUrl, XiaoYuanBusRes.class));
        if(null == xiaoYuanModel || null == xiaoYuanModel.getReturlList()){
            return null;
        }
        // 把新的路线信息填充到缓存中
        XiaoYuanBusRes.LineInfoRes lineinfo = xiaoYuanModel.getReturlList().getLineinfo();
        routeDTO.setFirstTime(lineinfo.getFirTime());
        routeDTO.setEndTime(lineinfo.getEndTime());
        routeDTO.setMoneyQty(new BigDecimal(lineinfo.getBusMoney()));
        routeDTO.setStationList(xiaoYuanModel.getReturlList().getStations().stream()
                .map(XiaoYuanBusRes.StationInfoRes::getBusStaname).collect(Collectors.toList()));
        ACache.addRoute(routeDTO);
        return xiaoYuanModel;
    }
}
