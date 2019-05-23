package ajou.ac.kr.teaming.service.login;

import java.util.List;

import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MypageService {

    @GET("/signUp")   //thread?UserId=summy
    Call<RegisterVO> getsignUP(@Query("UserID") String UserID);
}
