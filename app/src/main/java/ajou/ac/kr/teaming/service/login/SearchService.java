package ajou.ac.kr.teaming.service.login;

import java.util.List;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchService {


    @GET("/dogwalkerInfo")//가입
    Call<List<DogwalkerVO>> dogwalkersearch (@Query("UserYear") String UserYear,
                                            @Query("UserMonth") String UserMonth,
                                            @Query("Userdate") String Userdate,
                                            @Query("UserSmallcity") String UserSmallcity,
                                            @Query("UserverySmallcity") String UserverySmallcity,
                                            @Query("UserTime") String UserTime);
}
