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
import java.util.*;

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

    public void generateShops(ApiToken apiToken, ShopRepository shopRepository, CategoryRepository categoryRepository) throws IOException {

        final JSONObject jsonObject = new JSONObject(defaultGetCall(SHOP_URL, X_API_KEY, apiToken).string());

        JSONArray itemsArray = jsonObject.getJSONArray("items");

        for (Object singleItem : itemsArray) {

            if (singleItem instanceof JSONObject) {
                final JSONObject singleJsonItem = (JSONObject) singleItem;

                final String shopId = singleJsonItem.getString("id");
                final String shopDescription = singleJsonItem.getString("description");
                final String shopName = singleJsonItem.getString("name");
                final Set<Category> categorieCollection = new HashSet<>();
                final Shop singleShop = new Shop(null,shopId, shopName, shopDescription, null);
                shopRepository.save(singleShop);

                JSONArray categoriesArray = singleJsonItem.getJSONArray("categories");
                for (Object singleCategory : categoriesArray) {
                    if (singleCategory instanceof JSONObject) {
                        final JSONObject singleJsonCategory = (JSONObject) singleCategory;
                        final String categoryId = singleJsonCategory.getString("id");
                        final String CategoryName = singleJsonCategory.getString("name");
                        final Category category = new Category(categoryId,CategoryName,singleShop);
                        categoryRepository.save(category);
//                        categorieCollection.add(category);
                    }
                }

            }
        }


        System.out.println("test");
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
