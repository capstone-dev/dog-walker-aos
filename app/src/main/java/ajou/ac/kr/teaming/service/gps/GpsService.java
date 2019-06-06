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

    @FormUrlEncoded
    @POST("/gps") //gps/?id
    Call<GpsVo> postGpsData(@FieldMap HashMap<String, Object> parameters);


} //GpsService
