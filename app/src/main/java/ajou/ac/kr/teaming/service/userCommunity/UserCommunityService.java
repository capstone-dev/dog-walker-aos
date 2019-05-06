package ajou.ac.kr.teaming.service.userCommunity;

import java.util.List;

import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * GET 방식, 유저 커뮤니티 클릭 시 사용자가 등록한
 * 게시글 리스트를 반환해준다.
 */
public interface UserCommunityService {

    @GET("/thread")   //thread?users=kim
    Call<List<UserCommunityThreadVO>> getThread(@Query("users") String name);
}