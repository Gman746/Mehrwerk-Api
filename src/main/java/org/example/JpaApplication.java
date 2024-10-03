package org.example;

import org.example.Model.ApiToken;
import org.example.Model.Category;
import org.example.Model.Shop;
import org.example.Repo.CategoryRepository;
import org.example.Repo.ShopRepository;
import org.example.Repo.TokenRepository;
import org.example.Utils.ApiUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class JpaApplication {

    private static final Logger log = LoggerFactory.getLogger(JpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class);
    }

    private static final ApiUtils apiUtils = new ApiUtils();
    private ApiToken apiToken = null;

    @Bean
    public CommandLineRunner demo(TokenRepository tokenRepository, ShopRepository shopRepository, CategoryRepository categoryRepository) {
        return (args) -> {
            final Optional<ApiToken> tokenFromDb =  tokenRepository.findById(12345L); //TODO: Add Method for getting Valid Tokens
            final Date currentDate = new Date();


            apiToken = (tokenFromDb.isPresent() && currentDate.before(tokenFromDb.get().getExpiryDate())) ? tokenFromDb.get() : apiUtils.generateAuthenticationToken(tokenRepository);
            generateShops(apiToken, shopRepository, categoryRepository);

        };
    }

    public void generateShops(ApiToken apiToken, ShopRepository shopRepository, CategoryRepository categoryRepository) throws IOException {

        final JSONObject jsonObject = new JSONObject(apiUtils.getShopsAsJson(apiToken).string());

        JSONArray itemsArray = jsonObject.getJSONArray("items");
        for (Object singleItem : itemsArray) {
            if (singleItem instanceof JSONObject) {
                final JSONObject singleJsonItem = (JSONObject) singleItem;
                final Shop shop = createShopFromJson(singleJsonItem, shopRepository);
                JSONArray categoriesArray = singleJsonItem.getJSONArray("categories");
                for (Object singleCategory : categoriesArray) {
                    if (singleCategory instanceof JSONObject) {
                        createCategoryFromJson((JSONObject) singleCategory, categoryRepository, shop);
                    }
                }

            }
        }
    }


    private Shop createShopFromJson(JSONObject jsonShop, ShopRepository shopRepository) {
        final String shopId = jsonShop.getString("id");
        final String shopDescription = jsonShop.getString("description");
        final String shopName = jsonShop.getString("name");
        final Shop singleShop = new Shop(shopId, shopName, shopDescription, null);

        log.info("Saving Shop: " + shopRepository.save(singleShop).getId());
        return singleShop;
    }

    private void createCategoryFromJson(JSONObject jsonCategory, CategoryRepository categoryRepository, Shop shop) {
        final String categoryId = jsonCategory.getString("id");
        final String CategoryName = jsonCategory.getString("name");
        final Category category = new Category(categoryId, CategoryName, shop);

        log.info("Saving Category: " + categoryRepository.save(category).getId());
    }

}