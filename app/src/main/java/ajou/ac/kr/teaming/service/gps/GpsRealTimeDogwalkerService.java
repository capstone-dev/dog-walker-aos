package ajou.ac.kr.teaming.service.gps;

import java.util.List;

import ajou.ac.kr.teaming.vo.DogwalkerListVO;
import ajou.ac.kr.teaming.vo.DogwalkerVo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * GET 방식, 유저 커뮤니티 클릭 시 사용자가 등록한
 * 게시글 리스트를 반환해준다.
 */
public interface GpsRealTimeDogwalkerService {

    @GET("/dogwalkerRealTimeService")   ///dogwalkerRealTimeService?users=summy
    Call<List<DogwalkerListVO>> getThread();
}