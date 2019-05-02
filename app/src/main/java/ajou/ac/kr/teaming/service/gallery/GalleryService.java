package ajou.ac.kr.teaming.service.gallery;

import ajou.ac.kr.teaming.vo.SampleVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GalleryService {
    @GET("/users")   //users?name=1
    Call<SampleVO> getUser(@Query("name") int name);
}
