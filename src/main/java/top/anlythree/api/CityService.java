package top.anlythree.api;


import org.springframework.stereotype.Service;
import top.anlythree.dto.City;

import java.util.List;

/**
 * 城市接口
 * @author anlythree
 */
public interface CityService {

    List<City> cityList();

    City getCityById(Integer id);

    City getCityByName(String name);

}
