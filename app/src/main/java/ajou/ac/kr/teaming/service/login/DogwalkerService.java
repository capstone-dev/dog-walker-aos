package ajou.ac.kr.teaming.service.login;

import java.util.List;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DogwalkerService {

    @GET("/dogwalkerInfo")//가입
    Call<DogwalkerVO> dogwalkerinfo(@Query("UserID") String UserID, @Query("UserPassword")String UserPassword);

}
