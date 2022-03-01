package top.anlythree.api.xiaoyuanimpl.res.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import top.anlythree.dto.City;
import top.anlythree.utils.UnicodeUtil;
import top.anlythree.utils.exceptions.AException;

import java.text.CharacterIterator;

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

    public City castCity(){
        City city = new City();
        if (this.getCityId() == null || StringUtils.isEmpty(this.getCity())) {
            throw new AException("城市信息不完整，无法转换，city:" + city);
        }
        city.setId(Integer.valueOf(this.getCityId()));
        city.setName(UnicodeUtil.unicodeToString(this.getCity()));
        return city;
    }
}
