package top.anlythree.api.xiaoyuanimpl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import top.anlythree.api.CityService;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanCityListRes;
import top.anlythree.dto.City;
import top.anlythree.utils.RestTemplateUtils;

import java.util.HashMap;
import java.util.List;

@Component
public class XiaoYuanCityServiceImpl implements CityService {

    @Value("${xiaoyuan.username}")
    private String uname


    @Override
    public List<City> cityList() {
        HashMap<String,String> cityListParamMap = Maps.newHashMap();
        cityListParamMap.put("optype","city");
        cityListParamMap.put("uname",uname);
        ResponseEntity<XiaoYuanCityListRes> xiaoYuanCityListResResponseEntity = RestTemplateUtils.get("http://api.dwmm136.cn/z_busapi/BusApi.php", XiaoYuanCityListRes.class, cityListParamMap);
        return null;
    }
}
