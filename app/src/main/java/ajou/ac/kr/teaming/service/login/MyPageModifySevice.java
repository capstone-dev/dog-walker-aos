package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyPageModifySevice {


    @GET("/login")//가입
    Call<RegisterVO> DoMypage(@Query("UserID") String UserID);



    @FormUrlEncoded
    @POST("/signUp")
    Call<RegisterVO> postModify(@FieldMap HashMap<String, Object> param);

}
