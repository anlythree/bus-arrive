package top.anlythree.api;

import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanStationDTO;

/**
 * 地点接口
 *
 * @author anlythree
 * @description:
 * @time 2022/3/211:07 上午
 */
public interface StationService {

    /**
     * 根据城市名路线名和站点名称获取站点信息
     *
     * @param stationName
     * @return
     */
    XiaoYuanStationDTO getStationByStationName(String cityName, String routeName, String stationName);

}
