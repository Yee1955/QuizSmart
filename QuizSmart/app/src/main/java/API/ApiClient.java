package API;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import HttpModel.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String DB_BASE_URL = "http://10.0.2.2:5275/";
    private static final String LLM_BASE_URL = "http://10.0.2.2:5000/";
    private static Retrofit retrofit;

    public static Retrofit getDBRetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, java.util.concurrent.TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LoginResponse.class, new UserDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit getLLMRetrofitInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(LLM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();
        Log.d("TESTING", retrofit.baseUrl() + "");
        return retrofit;
    }

    public static ApiService getApiService(String type) {
        // Type: "DB", "LLM"
        if (type.equals("DB")) return getDBRetrofitInstance().create(ApiService.class);
        else return getLLMRetrofitInstance().create(ApiService.class);
    }
}
