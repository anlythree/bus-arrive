package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anlythree
 * @description: 笑园api城市实体
 * @time 2022/3/110:12 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XiaoYuanCityRes {

    /**
     * 笑园城市业务id
     */
    private String cityId;

    /**
     * 笑园城市名称
     */
    private String city;
}
