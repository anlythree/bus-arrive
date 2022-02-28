package top.anlythree.bus.service;

import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:26 上午
 */
@Service
public interface BusService {

    /**
     * 根据公交名称和公交站名称获取公交车到达时间
     * @param busCodeName
     * @param busStationName
     * @return
     */
    Duration getBusArriveTime(String busCodeName, String busStationName);
}
