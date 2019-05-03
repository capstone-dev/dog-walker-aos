package ajou.ac.kr.teaming.service.userCommunity;

import java.util.List;

import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserCommunityService {

    @GET("/thread")   //thread?users=kim
    Call<List<UserCommunityThreadVO>> getThread(@Query("users") String name);
}