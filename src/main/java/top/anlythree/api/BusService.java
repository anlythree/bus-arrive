package top.anlythree.api;

import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanBusRes;

import java.util.List;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/94:31 下午
 */
public interface BusService {

    /**
     * 根据城市名和路线名获取公交列表（其中还包含路线的所有站点名称，路线费用，首班车末班车）
     * @param cityName
     * @param routeName
     * @return
     */
    List<XiaoYuanBusRes> getBusLocation(String cityName, String routeName, String endStation);

}
