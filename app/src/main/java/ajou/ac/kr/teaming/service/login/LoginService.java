package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @FormUrlEncoded
    @GET("/signUp")            //가입
    Call<RegisterVO> getsignUp(@Query("UserID") String id,@Query("UserPassword") String pwd);


}
