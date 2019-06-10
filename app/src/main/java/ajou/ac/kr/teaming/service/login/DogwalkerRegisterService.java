package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DogwalkerRegisterService {





    /*@Multipart
    @POST("/dogwalkerInfo")
    Void create_board(@Body DogwalkerVO dogwalkerVO, Callback<DogwalkerVO> bd);
*/


    @FormUrlEncoded
    @POST("/dogwalkerInfo")
    Call<DogwalkerVO> post(@FieldMap HashMap<String, Object> param);

}
