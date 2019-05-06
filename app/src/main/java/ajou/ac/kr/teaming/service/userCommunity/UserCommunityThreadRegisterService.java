package ajou.ac.kr.teaming.service.userCommunity;

import java.util.HashMap;

import ajou.ac.kr.teaming.vo.UserCommunityThreadVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.POST;

/**
 * POST방식
 * 게시글을 등록하게 되면 /thread로 서버로 post하게 된다.
 */
public interface UserCommunityThreadRegisterService {
    @POST("/thread")            //thread
    Call<UserCommunityThreadVO> postThread(@FieldMap HashMap<String, Object> param);
}

