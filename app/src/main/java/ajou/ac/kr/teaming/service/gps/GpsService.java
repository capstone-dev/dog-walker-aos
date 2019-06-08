package ajou.ac.kr.teaming.service.gps;

import java.util.HashMap;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.RegisterVO;
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

    @GET("/gps?id={gpsId}") //gps?
    Call<GpsVo> doGetGpsInfo(@Path("gpsId") int gpsId);

    @FormUrlEncoded
    @POST("/gps") //gps/?id
    Call<GpsVo> postGpsData(@FieldMap HashMap<String, Object> parameters);

    @FormUrlEncoded
    @PUT("/gps?id={id}")
    Call<GpsVo> putGpsData(@Path("id") int gpsId, @FieldMap HashMap<String, Object> parameters);
    //Query?? Path??


} //GpsService
