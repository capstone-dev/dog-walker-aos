package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface GpsService {

    @GET("/gps") //gps?
    Call<GpsVo> doGetGpsInfo();


    @GET("/gps/marker") //gps?
    Call<GpsVo> doGetMarkerInfo();


    @GET("/gps/dogwalkerPosition") //gps?
    Call<GpsVo> doGetLocationInfo();


    @FormUrlEncoded
    @POST("/gps") //gps/?id
    Call<GpsVo> postGpsData(@FieldMap HashMap<String, Object> parameters);

    @Multipart
    @POST("/gps/marker") //gps/marker?id
    Call<GpsVo> postImageUploadData(@PartMap Map<String, RequestBody> params);


    @POST("/gps/dogwalkerPosition") //gps/dogwalkerPosition?id
    Call<GpsVo> postLocationData(@FieldMap HashMap<String, Object> parameters);


} //GpsService
