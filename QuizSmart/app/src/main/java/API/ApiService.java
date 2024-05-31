package API;

import java.util.List;

import HttpModel.*;
import Class.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Database backend route
    @POST("api/login")
    Call<LoginResponse> verifyLogin(@Body LoginRequest loginRequest);
    @GET("api/employee/{id}")
    Call<Employee> getEmployee(@Path("id") int employeeId);

    @GET("api/employer/{id}/sessions")
    Call<List<Session>> getEmployerSessions(@Path("id") int employerId);

    @GET("api/employer/{id}/employees")
    Call<List<Employee>> getEmployeesByEmployer(@Path("id") int employerId);

    @POST("api/session")
    Call<Session> addSession(@Body Session session);
    @PUT("api/session/{id}")
    Call<Session> updateSession(@Path("id") int sessionId);
    @GET("api/session/{id}/employee-session")
    Call<List<EmployeeSession>> getEmployeeSessionBySessionId(@Path("id") int sessionId);


    // Llama backend route
    @GET("getQuiz")
    Call<List<QuizResponse>> getQuestions(
            @Query("jobTitle") String jobTitle,
            @Query("jobRequirements") String jobRequirements,
            @Query("jobResponsibilities") String jobResponsibilities,
            @Query("companyCulture") String companyCulture
    );
}
