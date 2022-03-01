package top.anlythree.bus.service.impl;

import top.anlythree.bus.service.BusService;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author anlythree
 * @description:
 * @time 2022/2/2811:27 上午
 */
public class BusServiceImpl implements BusService {

    @Override
    public Duration getBusArriveTime(String busCodeName, String busStationName) {
        LocalDateTime now = LocalDateTime.now();

        return null;
    }
}
