package ajou.ac.kr.teaming.service.userCommunity;

import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * POST방식
 * 댓글을 등록하게 되면 /comment로 서버로 post하게 된다.
 */
public interface UserCommunityContentCommentService {

    @GET("comment") //comment 댓글 리스트 가져오는 요청  /comment?threadId=12
    Call<List<UserCommunityContentCommentVO>> getComment(@Query("threadId") String threadId);


    @FormUrlEncoded
    @POST("/comment")     //comment댓글을 달면 post해주는 요청   /comment
    Call<UserCommunityContentCommentVO> postComment(@FieldMap HashMap<String, Object> param);
}
