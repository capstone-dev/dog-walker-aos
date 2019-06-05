package ajou.ac.kr.teaming.service.myService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface MyServiceService {

    //해당 서비스 삭제하는 기능
    @DELETE("/walkingService")   //walkingService?id=1
    Call<ResponseBody> deleteService(@Query("id") String id);
}
