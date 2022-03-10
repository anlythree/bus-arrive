package top.anlythree.api.xiaoyuanimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import top.anlythree.api.xiaoyuanimpl.dto.XiaoYuanCityDTO;
import top.anlythree.api.xiaoyuanimpl.res.XiaoYuanResult;
import top.anlythree.utils.UnicodeUtil;
import top.anlythree.utils.exceptions.AException;

import java.util.List;

/**
 * @author anlythree
 * @description: 笑园api城市列表返回值
 * @time 2022/3/110:11 上午
 */
@Data
public class XiaoYuanCityListRes extends XiaoYuanResult {

    private List<XiaoYuanCityRes> returlList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XiaoYuanCityRes {

        /**
         * 笑园城市业务id
         */
        private String cityid;

        /**
         * 笑园城市名称
         */
        private String city;

        public XiaoYuanCityDTO castCity(){
            XiaoYuanCityDTO city = new XiaoYuanCityDTO();
            if (this.getCityid() == null || StringUtils.isEmpty(this.getCity())) {
                throw new AException("城市信息不完整，无法转换，xiaoYuanCity:" + city);
            }
            city.setId(this.getCityid());
            city.setName(UnicodeUtil.unicodeToString(this.getCity()));
            return city;
        }
    }

}
