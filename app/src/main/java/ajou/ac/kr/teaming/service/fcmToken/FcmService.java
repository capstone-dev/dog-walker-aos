package ajou.ac.kr.teaming.service.fcmToken;

import ajou.ac.kr.teaming.vo.FcmVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FcmService {

    @GET("/signUp/token") // signUp/token?UserID=hong2
    Call<FcmVO> getFcmToken (@Query("UserID") String UserID);
}
