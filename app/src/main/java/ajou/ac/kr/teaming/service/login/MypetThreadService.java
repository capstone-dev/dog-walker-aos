package ajou.ac.kr.teaming.service.login;

import java.util.List;

import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MypetThreadService {


    @GET("/login")//가입
    Call<List<MyPetVO>> petThread (@Query("UserID") String UserID,@Query("UserPassword")String UserPassword);



}
