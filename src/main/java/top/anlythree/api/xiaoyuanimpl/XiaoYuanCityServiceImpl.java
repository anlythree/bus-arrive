package top.anlythree.api.xiaoyuanimpl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.CityService;
import top.anlythree.api.xiaoyuanimpl.res.city.XiaoYuanCityListRes;
import top.anlythree.api.xiaoyuanimpl.res.city.XiaoYuanCityRes;
import top.anlythree.dto.City;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class XiaoYuanCityServiceImpl implements CityService {

    @Value("${xiaoyuan.username}")
    private String uname;

    @Override
    public List<City> cityList() {
        HashMap<String, String> cityListParamMap = Maps.newHashMap();
        ResponseEntity<XiaoYuanCityListRes> xiaoYuanCityListResResponseEntity = RestTemplateUtils.get("http://api.dwmm136.cn/z_busapi/BusApi.php?optype=city&uname="+uname,XiaoYuanCityListRes.class);
        XiaoYuanCityListRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(xiaoYuanCityListResResponseEntity);
        if (null == xiaoYuanModel) {
            return null;
        }
        List<XiaoYuanCityRes> returlList = xiaoYuanModel.getReturlList();
        if (CollectionUtils.isEmpty(returlList)) {
            return null;
        }
        return returlList.stream().map(XiaoYuanCityRes::castCity).collect(Collectors.toList());
    }

    @Override
    public City getCityById(Integer id) {
        List<City> cityList = cityList();
        for (City city : cityList) {
            if(Objects.equals(id,city.getId())){
                return city;
            }
        }
        return null;
    }

    @Override
    public City getCityByName(String name) {
        List<City> cityList = cityList();
        for (City city : cityList) {
            if(Objects.equals(name,city.getName())){
                return city;
            }
        }
        return null;
    }
}
