package ajou.ac.kr.teaming.service.gps;

import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.GpsMarkerVo;
import ajou.ac.kr.teaming.vo.PhotoVO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface GpsMarkerService {

    @GET("/gps/marker") //gps?
    Call<List<PhotoVO>> doGetMarkerInfo(@Query("id") int gpsId);

    @Multipart
    @POST("/gps/marker") //gps/marker?id
    Call<PhotoVO> postImageUploadData(@PartMap Map<String, RequestBody> params);

}
