package API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApiService {
    @POST("/chat")
    Call<ChatResponse> postChatRequest(@Body Chat chatRequest);
}