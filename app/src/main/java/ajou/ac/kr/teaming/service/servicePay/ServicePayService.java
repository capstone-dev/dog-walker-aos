package ajou.ac.kr.teaming.service.servicePay;

import java.util.HashMap;
import java.util.List;

import ajou.ac.kr.teaming.vo.ServiceVO;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * GET방식
 * 해당 유저아이디에 해당하는 리스트 GET
 * POST방식
 * 해당 submitAcivity에서 결제 승인버튼을 누르게 되면 /walkingService로 서버로 post하게 된다.
 */
public interface ServicePayService {

    @FormUrlEncoded
    @POST("/walkingService")            //walkingService
    Call<ServiceVO> postService(@FieldMap HashMap<String, Object> param);

    @GET("/walkingService") //walkingService 서비스 리스트 가져오는 요청  /walkingService?user_UserID=12
    Call<List<ServiceVO>> getComment(@Query("user_UserID") String user_UserID);
}