package ajou.ac.kr.teaming.service.login;

import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DogwalkerRegisterService {


    @Multipart
    @POST("MyPet")
    Call<MyPetVO> RegisterPet (@Part MultipartBody.Part image,
                               @Part("DogName")RequestBody dogname,
                               @Part("DogType")RequestBody dogtype,
                               @Part("UserID")RequestBody userrid,
                               @Part("DogAge")RequestBody dogage);
}
