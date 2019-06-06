package ajou.ac.kr.teaming.service.userCommunity;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * POST방식
 * 게시글을 등록하게 되면 /thread로 서버로 post하게 된다.
 */
public interface UserCommunityThreadRegisterService {

    @FormUrlEncoded
    @POST("/thread")            //thread
    Call<UserCommunityThreadVO> postThread(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @PUT("/thread")            //thread
    Call<UserCommunityThreadVO> updateThread(@FieldMap HashMap<String, Object> param);

    @DELETE("/thread")   //thread?threadId=1
    Call<ResponseBody> deleteThread(@Query("threadId") String threadId);
}

