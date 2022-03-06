package top.anlythree.api.amapimpl.res.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.anlythree.api.amapimpl.res.AMapResult;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStationListRes extends AMapResult {

    private List<GetStationRes> geocodes;
}
