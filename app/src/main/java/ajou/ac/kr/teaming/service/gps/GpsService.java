package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.GpsVo;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GpsService {

    @GET("/gps") //gps?
    Call<GpsVo> doGetDogwalkerLocation (
            @Query("latitude") double lat, @Query("longitude") double lon);

    @POST("/gps") ///gps?
            Call<GpsVo> postStringData(@FieldMap HashMap<String, String> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postIntegerData(@FieldMap HashMap<String, Integer> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postDoubleData(@FieldMap HashMap<String, Double> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postLongData(@FieldMap HashMap<String, Long> param);

} //GpsService
