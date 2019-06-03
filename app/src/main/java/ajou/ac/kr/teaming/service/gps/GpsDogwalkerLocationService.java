package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.GpsLocationVo;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GpsDogwalkerLocationService {

    @FormUrlEncoded
    @POST("/gps/dogwalkerPosition") //gps/dogwalkerPosition?id
    Call<GpsLocationVo> postLocationData(@FieldMap HashMap<String, Object> parameters);
}
