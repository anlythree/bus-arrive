package top.anlythree.api.xiaoyuanimpl;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.anlythree.api.CityService;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanCityListRes;
import top.anlythree.cache.ACache;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.utils.RestTemplateUtil;
import top.anlythree.utils.ResultUtil;
import top.anlythree.utils.UrlUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class XiaoYuanCityServiceImpl implements CityService {

    @Value("${xiaoyuan.username}")
    private String uname;

    @Override
    public List<XiaoYuanCityDTO> cityList() {
        XiaoYuanCityListRes xiaoYuanModel = ResultUtil.getXiaoYuanModel(RestTemplateUtil.get(
                UrlUtil.createXiaoYuanUrl(new UrlUtil.UrlParam("optype","city"),
                        new UrlUtil.UrlParam("uname",uname)),
                XiaoYuanCityListRes.class));
        if (null == xiaoYuanModel) {
            return null;
        }
        List<XiaoYuanCityListRes.XiaoYuanCityRes> returlList = xiaoYuanModel.getReturlList();
        if (CollectionUtils.isEmpty(returlList)) {
            return null;
        }
        return returlList.stream().map(XiaoYuanCityListRes.XiaoYuanCityRes::castCity).collect(Collectors.toList());
    }

    @Override
    public XiaoYuanCityDTO getCityById(String id) {
        for (XiaoYuanCityDTO city : ACache.getCityCacheList()) {
            if(Objects.equals(id , city.getId())){
                return city;
            }
        }
        List<XiaoYuanCityDTO> cityList = cityList();
        for (XiaoYuanCityDTO city : cityList) {
            if(Objects.equals(id,city.getId())){
                return city;
            }
        }
        return null;
    }

    @Override
    public XiaoYuanCityDTO getCityByName(String name) {
        List<XiaoYuanCityDTO> cityCacheList = ACache.getCityCacheList();
        for (XiaoYuanCityDTO city : cityCacheList) {
            if(Objects.equals(name+"市",city.getName())){
                return city;
            }
        }
        List<XiaoYuanCityDTO> cityList = cityList();
        for (XiaoYuanCityDTO city : cityList) {
            if(Objects.equals(name,city.getName()) ||
                    Objects.equals(name+"市",city.getName())){
                ACache.addCity(city);
                return city;
            }
        }
        return null;
    }
}
