package ajou.ac.kr.teaming.service.userCommunity;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.UserCommunityContentCommentVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * POST방식
 * 댓글을 등록하게 되면 /comment로 서버로 post하게 된다.
 */
public interface UserCommunityContentCommentService {

    @POST("/comment")     //comment
    Call<UserCommunityContentCommentVO> postComment(@FieldMap HashMap<String, Object> param);
}
