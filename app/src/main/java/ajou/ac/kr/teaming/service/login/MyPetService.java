package ajou.ac.kr.teaming.service.login;

import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.MyPetVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface MyPetService {


    @Multipart
    @POST("/login")
    Call<MyPetVO> myPet(@PartMap Map<String, RequestBody> params);

}
