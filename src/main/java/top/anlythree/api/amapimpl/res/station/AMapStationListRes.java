package top.anlythree.api.amapimpl.res.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.anlythree.api.amapimpl.res.AMapResult;

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
}
