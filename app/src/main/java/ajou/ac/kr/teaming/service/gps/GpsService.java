package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.SampleVO;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GpsService {

    @GET("current/hourly")
    Call<GpsVo> doGetDogwalkerLocation (
            @Query("latitude") double lat, @Query("longitude") double lon);


    @POST("LocationToServer")
            Call<GpsVo> postLocation(@FieldMap HashMap<String, Object> param);

} //GpsService
