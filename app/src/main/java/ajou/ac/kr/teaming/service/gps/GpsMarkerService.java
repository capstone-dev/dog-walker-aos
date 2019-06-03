package ajou.ac.kr.teaming.service.gps;

import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsLocationVo;
import ajou.ac.kr.teaming.vo.GpsMarkerVo;
import ajou.ac.kr.teaming.vo.GpsVo;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface GpsMarkerService {


    @Multipart
    @POST("/gps/marker") //gps/marker?id
    Call<GpsMarkerVo> postImageUploadData(@PartMap Map<String, RequestBody> params);

}
