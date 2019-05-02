package ajou.ac.kr.teaming.service.sample;

import ajou.ac.kr.teaming.vo.SampleVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SampleService {

    @GET("/users")
    Call<SampleVO> getUser(@Query("name") int name);
}
