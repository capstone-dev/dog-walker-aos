package ajou.ac.kr.teaming.service.login;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReservationService {

    @GET("/dogwalkerInfo")//가입
    Call<DogwalkerVO> dogwalkerThread (@Query("UserID") String UserID);

}
