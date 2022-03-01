package top.anlythree.api.xiaoyuanimpl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.anlythree.api.CityService;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanCityListRes;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanCityRes;
import top.anlythree.dto.City;
import top.anlythree.utils.RestTemplateUtils;
import top.anlythree.utils.ResultUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XiaoYuanCityServiceImpl implements CityService {

    @Value("${xiaoyuan.username}")
    private String uname;

    /**
     * 缓存到JVM中的城市列表
     */
    private static List<City> cityListOnMemory;

    @Override
    public List<City> cityList(){
        HashMap<String,String> cityListParamMap = Maps.newHashMap();
        cityListParamMap.put("optype","city");
        cityListParamMap.put("uname",uname);
        XiaoYuanCityListRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(
                RestTemplateUtils.get("http://api.dwmm136.cn/z_busapi/BusApi.php", XiaoYuanCityListRes.class, cityListParamMap));
        ArrayList<XiaoYuanCityRes> returlList = xiaoYuanModel.getReturlList();

        return null;
    }
}
