package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.LoginVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @GET("/login")//가입
    Call<RegisterVO> DoLogin (@Query("UserID") String UserID, @Query("UserPassword") String UserPassword);


    @GET("/login")
    Call<DogwalkerVO> Login (@Query("UserID") String UserID, @Query("UserPassword") String UserPassword);

}
