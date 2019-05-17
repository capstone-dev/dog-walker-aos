package ajou.ac.kr.teaming.service.gps;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.SampleVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GpsService {

    static final String BASEURL = "http://apis.skplanetx.com/";
    static final String APIKEY ="78f4044b-3ca4-439d-8d0e-10135941f054";


    @GET("weather/current/hourly")
    Call<GpsVo> getDogwalkerLocation (
        //    @Header("appKey")String appKey,
            @Query("version") int version,
            @Query("lat") double lat, @Query("lon") double lon);

}
