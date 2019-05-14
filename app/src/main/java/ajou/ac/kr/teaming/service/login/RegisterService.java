package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {


    @FormUrlEncoded
    @POST("/signUp")
    Call<RegisterVO> postsignUp(@Body HashMap<String, Object> param);

}