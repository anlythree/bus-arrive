package top.anlythree.api;

import top.anlythree.api.amapimpl.res.AMapBusRouteTimeRes;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;
import top.anlythree.bussiness.dto.BusDTO;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/94:31 下午
 */
public interface BusService {

    /**
     * 根据城市名和路线名和方向获取当前公交列表（其中还包含路线的所有站点名称，路线费用，首班车末班车）
     * @param cityName
     * @param routeName
     * @return
     */
    List<BusDTO> getBusLocationList(String cityName, String routeName, String endStation);



}
