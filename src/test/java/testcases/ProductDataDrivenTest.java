package testcases;

import java.util.Map;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import pojo.Product;
import routes.Routes;

public class ProductDataDrivenTest extends BaseClass {
	
	@Test(dataProvider = "jsonDataProvider", dataProviderClass = utils.DataProviders.class)
	public void AddNewProduct(Map<String, String> data)
	{
		String title = data.get("title");
		double price = Double.parseDouble(data.get("price"));
		String category = data.get("category");
		String description = data.get("description");
		String image = data.get("image");
		
		Product newProduct = new Product(title,price,description,image,category);
		
		int productId = given()
			.contentType(ContentType.JSON)
			.body(newProduct)
		.when()
			.post(Routes.CREATE_PRODUCT)
		.then()
			.log().body()
			.statusCode(200)
			.body("id", notNullValue())
			.body("title", equalTo(newProduct.getTitle()))
			.extract().jsonPath().getInt("id"); //Extracting Id from response body
		
		System.out.println("Product ID======> "+ productId);
		
		//Delete product
		given()
			.pathParam("id", productId)
		.when()
			.delete(Routes.DELETE_PRODUCT)
		.then()
			.statusCode(200);
		
		System.out.println("Deleted Product ID======> "+ productId);
		
	}
	

}
