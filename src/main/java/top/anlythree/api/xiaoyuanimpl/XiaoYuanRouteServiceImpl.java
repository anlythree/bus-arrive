package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.anlythree.api.CityService;
import top.anlythree.api.RouteService;
import top.anlythree.api.xiaoyuanimpl.res.city.XiaoYuanCityListRes;
import top.anlythree.dto.City;
import top.anlythree.dto.Route;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.exceptions.AException;

@Service
public class XiaoYuanRouteServiceImpl implements RouteService {

    @Autowired
    @Qualifier(value = "xiaoYuanCityServiceImpl")
    private CityService cityService;


    @Override
    public Route getRouteByNameAndCityId(String routeName, Integer cityId) {
        City cityById = cityService.getCityById(cityId);
        if(null == cityById){
            throw new AException("cityId错误，查不到指定的cityId，cityId："+cityId)
        }
        XiaoYuanCityListRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(
                RestTemplateUtils.get("http://api.dwmm136.cn/z_busapi/BusApi.php?optype=city&uname="+uname,
                        XiaoYuanCityListRes.class););
        return null;
    }
}
