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

    static final String BASEURL = "http://54.180.115.101:3000";
    static final String APIKEY ="78f4044b-3ca4-439d-8d0e-10135941f054";



    public static final Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GpsService.BASEURL)
            .build();

    @GET("current/hourly")
    Call<GpsVo> doGetDogwalkerLocation (
            @Query("latitude") double lat, @Query("longitude") double lon);


    @POST("setLocationToServer")
    Call<GpsVo> doSetDogwalkerLocation(
            @Query("latitude") double lat, @Query("longitude") double lon);

} //GpsService
