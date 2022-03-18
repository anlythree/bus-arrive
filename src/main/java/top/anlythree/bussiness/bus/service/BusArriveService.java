package top.anlythree.bussiness.bus.service;

import org.springframework.stereotype.Service;
import top.anlythree.bussiness.dto.BusDTO;

/**
 * @author anlythree
 * @description: 公交到达接口（主接口）
 * @time 2022/3/1810:48 上午
 */
public interface BusArriveService {

   BusDTO getBusByArriveTimeAndLocation(String dateTime,String startLocation,String endLocation);
}
