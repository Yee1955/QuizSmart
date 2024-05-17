package API;

import java.util.List;

import Class.*;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApiService {
    @GET("/getQuiz")
    Call<TaskResponse> getQuiz(@Query("topic") String topic);
}
