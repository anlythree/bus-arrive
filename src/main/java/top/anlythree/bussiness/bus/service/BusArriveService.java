package top.anlythree.bussiness.bus.service;

import org.springframework.stereotype.Service;
import top.anlythree.bussiness.dto.;import java.time.LocalDateTime;

/**
 * @author anlythree
 * @description: 公交到达接口（主接口）
 * @time 2022/3/1810:48 上午
 */
public interface BusArriveService {

   /**
    * 根据公交路线，到达目的地时间来推算  合适的公交什么时候从起始站出发
    * @param cityName
    * @param busName
    * @param arriveTime
    * @return 这个时间将是播报的时间，在 这个时间 会提示还可以准备多久
    */
    LocalDateTime getStartTimeByArriveTime(String cityName, String busName, String arriveTime, String startLocation, String endLocation);

}
