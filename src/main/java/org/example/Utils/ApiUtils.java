package org.example.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.Model.ApiToken;
import org.example.Repo.TokenRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class ApiUtils {

    private final static String BEARER_URL = "https://testapi.mehrwerk.de/v2/iam/oauth/token";
    private final static String X_API_KEY = "lQeUjTylHDCxqfISyZ05C7m1rov3hEZLYAqO42zs7h1fPBL2RF";
    

    public void generateAuthenticationToken(TokenRepository tokenRepository) throws IOException, InterruptedException {

        final ResponseBody tokenResponse = defaultPostCall(BEARER_URL, X_API_KEY, createAuthBody());
        ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> mapping = mapper.readValue(tokenResponse.string(), new TypeReference<>() {
        });

        final Calendar expireDate = Calendar.getInstance();
        expireDate.setTime(new Date());
        expireDate.add(Calendar.SECOND, (Integer) mapping.get("expires_in"));


        tokenRepository.save(new ApiToken(null, (String) mapping.get("access_token"), (String) mapping.get("token_type"), expireDate.getTime()));
    }

    private ResponseBody defaultPostCall(String apiUrl, String xApiKey, RequestBody requestBody) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader("X-API-KEY", xApiKey)
                .build();
        Response response = client.newCall(request).execute();


        return response.body();
    }

    private RequestBody createAuthBody() {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "client_credentials")
                .addFormDataPart("client_secret", "hj52Ws4kF")
                .addFormDataPart("client_id", "bewerber")
                .build();

    }
}
