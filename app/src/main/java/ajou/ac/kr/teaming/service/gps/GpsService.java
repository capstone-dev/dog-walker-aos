package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface GpsService {

    @GET("/gps") //gps?
            Call<GpsVo> doGetDogwalkerLocation();

  /*  @POST("/gps") ///gps?
            Call<GpsVo> postStringData(@FieldMap HashMap<String, String> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postIntegerData(@FieldMap HashMap<String, Integer> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postDoubleData(@FieldMap HashMap<String, Double> param);

    @POST("/gps") ///gps?
            Call<GpsVo> postLongData(@FieldMap HashMap<String, Long> param);*/

//    @POST("/gps") ///gps?
//            Call<GpsVo> postObjectData(@FieldMap HashMap<String, Object> param);


    @Multipart
    @POST("/gps") ///gps?
    Call<GpsVo> postObjectData(@PartMap Map<String, RequestBody> params);


} //GpsService
