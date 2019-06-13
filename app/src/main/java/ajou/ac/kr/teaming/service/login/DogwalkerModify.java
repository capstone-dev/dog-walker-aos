package ajou.ac.kr.teaming.service.login;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;

public interface DogwalkerModify {

    @FormUrlEncoded
    @PUT("/dogwalkerInfo")
    Call<DogwalkerVO> ModifyD(@FieldMap HashMap<String, Object> param);
}
