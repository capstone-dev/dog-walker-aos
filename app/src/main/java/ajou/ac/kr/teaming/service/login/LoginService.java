package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.vo.LoginVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @GET("/signUp")//가입
    Call<LoginVO> DoLogin (@Query("UserID") String UserID, @Query("UserPassword") String UserPassword);


}