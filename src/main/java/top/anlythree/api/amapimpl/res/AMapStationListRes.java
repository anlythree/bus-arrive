package top.anlythree.api.amapimpl.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import top.anlythree.bussiness.dto.LocationDTO;
import top.anlythree.bussiness.dto.StationDTO;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AMapStationListRes extends AMapResult {

    private List<AMapStationRes> geocodes;

    public AMapStationRes getOneStationRes(String keyWord){
        for (AMapStationRes geocode : this.getGeocodes()) {
            if(StringUtils.isNotEmpty(geocode.getFormattedAddress()) &&
                    geocode.getFormattedAddress().contains(keyWord) &&
                    geocode.getFormattedAddress().contains("公交站") &&
                    Objects.equals("兴趣点",geocode.getLevel())){
                return geocode;
            }
        }
        return null;
    }

    public AMapStationRes getOneLocationRes(String keyWord){
        if(CollectionUtils.isEmpty(this.getGeocodes())){
            return null;
        }
        for (AMapStationRes geocode : this.getGeocodes()) {
            if(geocode.getFormattedAddress().contains(keyWord)){
                return geocode;
            }
        }
        return geocodes.get(0);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AMapStationRes {

        /**
         * 省份＋城市＋区县＋城镇＋乡村＋街道＋门牌号码
         */
        private String formattedAddress;

        /**
         * 国家。国内地址默认返回中国
         */
        private String country;

        /**
         * 省
         */
        private String province;

        /**
         * 市
         */
        private String city;

        /**
         * 城市编码
         */
        private String citycode;

        /**
         * 区
         */
        private String district;

        /**
         * 街道
         */
        private Object street;

        /**
         * 门牌
         */
        private Object number;

        /**
         * 区域编码
         */
        private String adcode;

        /**
         * 坐标点经度,纬度
         */
        private String location;

        /**
         * 匹配级别
         */
        private String level;

        public StationDTO castStationDto(String stationName){
            return new StationDTO(stationName,formattedAddress,location,country,province,city,district);
        }

        public LocationDTO castLocationDto(String stationName){
            return new LocationDTO(stationName,formattedAddress,location,country,province,city,district);
        }
    }
}
