package top.anlythree.api;


import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;

import java.util.List;

/**
 * 城市接口
 * @author anlythree
 */
public interface CityService {

    List<XiaoYuanCityDTO> cityList();

    XiaoYuanCityDTO getCityById(String id);

    XiaoYuanCityDTO getCityByName(String name);

}
