package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.vo.GpsLocationVo;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GpsDogwalkerLocationService {

    @GET("/gps/dogwalkerPosition") //gps?
    Call<List<GpsLocationVo>> doGetLocationInfo(@Query("id") int gpsId);

    @FormUrlEncoded
    @POST("/gps/dogwalkerPosition") //gps/dogwalkerPosition?id
    Call<GpsLocationVo> postLocationData(@FieldMap HashMap<String, Object> parameters);
}
