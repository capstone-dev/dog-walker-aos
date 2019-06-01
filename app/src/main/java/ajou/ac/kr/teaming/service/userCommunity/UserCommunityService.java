package ajou.ac.kr.teaming.service.userCommunity;

import java.util.List;

import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * GET 방식, 유저 커뮤니티 클릭 시 사용자가 등록한
 * 게시글 리스트를 반환해준다.
 * 검색에 조건에 맞는 게시글 리스트 반환
 */
public interface UserCommunityService {

    @GET("/thread")   //thread?users=kim
    Call<List<UserCommunityThreadVO>> getThread();

    @GET("/thread")  //thread?user_UserID=hong2
    Call<List<UserCommunityThreadVO>> getUser_UserIDThread(@Query("user_UserID") String user_UserID);

    @GET("/thread")  //thread?threadTitle=테스트
    Call<List<UserCommunityThreadVO>> getThreadTitleThread(@Query("threadTitle") String user_UserID);

    @GET("/thread")  //thread?userLocation=dd
    Call<List<UserCommunityThreadVO>> getUserLocationThread(@Query("userLocation") String user_UserID);

}