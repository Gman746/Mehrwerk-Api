package org.example.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.Model.ApiToken;
import org.example.Model.Category;
import org.example.Model.Shop;
import org.example.Repo.CategoryRepository;
import org.example.Repo.ShopRepository;
import org.example.Repo.TokenRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class ApiUtils {

    private final static String BEARER_URL = "https://testapi.mehrwerk.de/v2/iam/oauth/token";
    private final static String SHOP_URL = "https://testapi.mehrwerk.de/v3/cashback/shops";
    private final static String X_API_KEY = "lQeUjTylHDCxqfISyZ05C7m1rov3hEZLYAqO42zs7h1fPBL2RF";


    public ApiToken generateAuthenticationToken(TokenRepository tokenRepository) throws IOException, InterruptedException {

        final ResponseBody tokenResponse = defaultPostCall(BEARER_URL, X_API_KEY, createAuthBody());
        ObjectMapper mapper = new ObjectMapper();
        final Map<String, Object> jsonTokenMap = mapper.readValue(tokenResponse.string(), new TypeReference<>() {
        });

        final Calendar expireDate = Calendar.getInstance();
        expireDate.setTime(new Date());
        expireDate.add(Calendar.SECOND, (Integer) jsonTokenMap.get("expires_in"));


        final ApiToken apiToken = new ApiToken(null, (String) jsonTokenMap.get("access_token"), StringUtils.capitalize((String) jsonTokenMap.get("token_type")), expireDate.getTime());
        tokenRepository.save(apiToken);
        return apiToken;
    }

    public ResponseBody getShopsAsJson(ApiToken apiToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(SHOP_URL)
                .get()
                .addHeader("X-API-KEY", X_API_KEY)
                .addHeader("Authorization", apiToken.getType() + " " + apiToken.getContent())
                .build();
        Response response = client.newCall(request).execute();


        return response.body();
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


    private ResponseBody defaultGetCall(String apiUrl, String xApiKey, ApiToken apiToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("X-API-KEY", xApiKey)
                .addHeader("Authorization", apiToken.getType() + " " + apiToken.getContent())
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
