package top.anlythree.api.xiaoyuanimpl.res.route;

import org.springframework.util.StringUtils;
import top.anlythree.dto.Route;
import top.anlythree.utils.exceptions.AException;

/**
 * @author anlythree
 * @description:
 * @time 2022/3/13:31 下午
 */
public class XiaoYuanRouteRes {

    /**
     * 起始站
     */
    private String busStastan;

    /**
     * 终点站
     */
    private String busEndstan;

    /**
     * 路线ID
     */
    private String busLinestrid;

    /**
     * 路线编号
     */
    private String busLinenum;

    /**
     * 路线名称
     */
    private String busStaname;

    public Route castRoute(){
        Route route = new Route();
        if(StringUtils.isEmpty(busStastan) ||
        StringUtils.isEmpty(busEndstan) ||
        StringUtils.isEmpty(busLinestrid) ||
        StringUtils.isEmpty(busStaname)){
            throw new AException("路线信息不全，无法转换，route："+this);
        }
        route.setRouteId(this.busLinestrid);
        route.setRouteCode(this.busLinenum);
        route.setRouteName(this.busStaname);
        route.setStartStation(this.busStastan);
        route.setEndStation(this.busEndstan);
        return route;
    }
}
