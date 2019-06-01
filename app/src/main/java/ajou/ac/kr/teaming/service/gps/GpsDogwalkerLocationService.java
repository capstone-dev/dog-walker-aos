package ajou.ac.kr.teaming.service.gps;

import ajou.ac.kr.teaming.vo.GpsVo;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GpsDogwalkerLocationService {

    @GET("/gps/dogwalkerPosition") //gps/dogwalkerPosition?id=??
    Call<GpsVo> doGetDogwalkerLocation();
}
