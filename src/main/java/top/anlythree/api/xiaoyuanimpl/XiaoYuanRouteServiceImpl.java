package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;

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
    public Route getRouteByNameAndCityId(String routeName, String cityId) {
        City cityById = cityService.getCityById(cityId);
        if(null == cityById){
            throw new AException("cityId错误，查不到指定的cityId，cityId："+cityId)
        }
        String keySecret = MD5Utils.getMd5(uname + key + "luxian")
        XiaoYuanRouteList xiaoYuanRouteListRes = ResultUtil.getXiaoYuanModel(
                RestTemplateUtils.get(UrlUtils.createXiaoYuan("optype","luxian","uname",uname,"cityid",cityId,"keywords",routeName,"keySecret",keySecret),
                        XiaoYuanRouteListRes.class));
        xiaoYuanRouteListRes
    }
}
