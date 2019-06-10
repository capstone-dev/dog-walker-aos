package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.RegisterVO;
import ajou.ac.kr.teaming.vo.ServiceVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GpsService {

    @GET("/gps") //gps?
    Call<GpsVo> doGetGpsInfo(@Query("id") int gpsId);

    @FormUrlEncoded
    @POST("/gps") //gps/?id
    Call<GpsVo> postGpsData(@FieldMap HashMap<String, Object> parameters);

    @FormUrlEncoded
    @PUT("/gps")
    Call<GpsVo> putGpsData(@Query("id") int gpsId, @FieldMap HashMap<String, Object> parameters);
    //Query


    @FormUrlEncoded
    @PUT("/walkingService")
    Call<ServiceVO> putGpsIdDataToWalkingService(@FieldMap HashMap<String, Object> parameters);
    //Query

} //GpsService
