package ajou.ac.kr.teaming.service.login;

import java.util.List;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DogwalkerThreadService {


    @GET("/dogwalkerInfo")//가입
    Call<List<DogwalkerVO>> dogwalkerThread (@Query("UserYear") String UserYear,
                                             @Query("UserMonth") String UserMonth,
                                             @Query("UserDay") String Userday);
}
