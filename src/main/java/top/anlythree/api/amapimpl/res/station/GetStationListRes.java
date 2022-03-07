package top.anlythree.api.amapimpl.res.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.anlythree.api.amapimpl.res.AMapResult;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStationListRes extends AMapResult {

    private List<StationRes> geocodes;

    public StationRes getOneStationRes(String keyWord){
        for (StationRes geocode : this.getGeocodes()) {
            if(StringUtils.isNotEmpty(geocode.getFormattedAddress()) &&
                    geocode.getFormattedAddress().contains(keyWord)){

            }
        }
    }
}
