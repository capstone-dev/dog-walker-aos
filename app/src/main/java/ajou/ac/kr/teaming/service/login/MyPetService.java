package ajou.ac.kr.teaming.service.login;

import java.util.List;
import java.util.Map;

import ajou.ac.kr.teaming.vo.GpsVo;
import ajou.ac.kr.teaming.vo.MyPetVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface MyPetService {


    @GET("/myPet") //gps?
    Call<List<MyPetVO>> getMyPetInfo(@Query("UserID") String params);

    @Multipart
    @POST("/myPet")
    Call<MyPetVO> myPet(@PartMap Map<String, RequestBody> params);

}
