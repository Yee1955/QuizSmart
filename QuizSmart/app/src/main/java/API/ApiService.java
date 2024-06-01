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



    // ------------------ Database backend route (Employee) ------------------
    @GET("api/employee/{id}")
    Call<Employee> getEmployee(@Path("id") int employeeId);
    @GET("api/employee/{id}/employee-session")
    Call<List<EmployeeSession>> getEmployeeSessionByEmployeeId(@Path("id") int employeeId);



    // ------------------ Database backend route (Employer) ------------------
    @GET("api/employer/{id}")
    Call<Employer> getEmployer(@Path("id") int employerId);
    @GET("api/employer/{id}/sessions")
    Call<List<Session>> getEmployerSessions(@Path("id") int employerId);
    @GET("api/employer/{id}/employees")
    Call<List<Employee>> getEmployeesByEmployer(@Path("id") int employerId);



    // ------------------ Database backend route (Session) ------------------
    @POST("api/session")
    Call<Session> addSession(@Body Session session);
    @PUT("api/session/{id}")
    Call<Session> updateSession(@Path("id") int sessionId, @Body Session session);
    @GET("api/session/{id}")
    Call<Session> getSession(@Path("id") int sessionId);
    @GET("api/session/{id}/employee-session")
    Call<List<EmployeeSession>> getEmployeeSessionBySessionId(@Path("id") int sessionId);
    @GET("api/session/{id}/completed")
    Call<Integer> getNumOfCompleted(@Path("id") int sessionId);
    @GET("api/session/session-code/{sessionCode}")
    Call<Session> getSessionByCode(@Path("sessionCode") String sessionCode);



    // ------------------ Database backend route (Employee-Session) ------------------
    @GET("api/employee-session/{id}")
    Call<EmployeeSession> getEmployeeSession(@Path("id") int employeeSessionId);
    @POST("api/employee-session")
    Call<EmployeeSession> addEmployeeSession(@Body EmployeeSession employeeSession);
    @PUT("api/employee-session/{id}")
    Call<EmployeeSession> updateEmployeeSession(@Path("id") int employeeSessionId, @Body EmployeeSession employeeSession);



    // ------------------ Llama backend route ------------------
    @GET("getQuiz")
    Call<List<QuizResponse>> getQuestions(
            @Query("jobTitle") String jobTitle,
            @Query("jobRequirements") String jobRequirements,
            @Query("jobResponsibilities") String jobResponsibilities,
            @Query("companyCulture") String companyCulture
    );
    @POST("evaluate")
    Call<EvaluationResponse> evaluateCandidate(@Body EvaluationRequest evaluationRequest);
    @POST("chat")
    Call<ChatResponse> normalChat(@Body ChatRequest inputMessage);
}
