package ajou.ac.kr.teaming.service.login;

import ajou.ac.kr.teaming.vo.MyPetVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyPetService {



    @Multipart
    @POST("MyPet")
    Call<MyPetVO> RegisterPet (@Part MultipartBody.Part image,
                               @Part("dog_name") RequestBody dogname,
                               @Part("dog_species")RequestBody dogtype,
                               @Part("UserID")RequestBody userrid,
                               @Part("dog_age")RequestBody dogage);




}
