package ajou.ac.kr.teaming.service.common;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    private static final String URL = "http://52.79.234.182:3000";

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <T> T create(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}