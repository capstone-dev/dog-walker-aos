package ajou.ac.kr.teaming.service.login;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Map;

import ajou.ac.kr.teaming.vo.DogwalkerVO;
import ajou.ac.kr.teaming.vo.MyPetVO;
import ajou.ac.kr.teaming.vo.RegisterVO;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface DogwalkerRegisterService {





    @Multipart
    @POST("/dogwalkerInfo")
    Call<DogwalkerVO> RegisterDogwalker (@PartMap Map<String, RequestBody> params);
}
