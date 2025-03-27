import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.collection.ArrayAsIterableMatcher;
import org.testng.Assert;

import POJO.Api;
import POJO.GetCourse;
import POJO.WebAutomation;
public class oAuthTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] courseTitle = {"Selenium Webdriver Java","Cypress","Protractor"};
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		String response = given().log().all()
		.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.formParam("grant_type", "client_credentials")
		.formParam("scope", "trust")
		.when().post("oauthapi/oauth2/resourceOwner/token").asString();
		System.out.println(response);
		
		JsonPath js = new JsonPath(response);
		String accessToken = js.get("access_token");
		
		//getCourseDetails
		
		GetCourse getCourse = given().queryParam("access_token", accessToken)
				.when().log().all().get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourse.class);
		System.out.println(getCourse);
		
		System.out.println(getCourse.getLinkedIn());
		System.out.println(getCourse.getInstructor());
		
		System.out.println(getCourse.getCourses().getApi().get(1).getCourseTitle());
		System.out.println("Print the price of Rest Assured Automation using Java Course API");
		List<Api> apiCourses = getCourse.getCourses().getApi();
		
		for(int i=0;i<apiCourses.size();i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java"))
			{
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		
		System.out.println("Get the Course Title of web automation");
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> webAutomationCourses = getCourse.getCourses().getWebAutomation();
		for(int j = 0; j<webAutomationCourses.size();j++) {
			a.add(webAutomationCourses.get(j).getCourseTitle());
		}
		System.out.println("Actual : " +a);
		List<String> expectedList = Arrays.asList(courseTitle);
		Assert.assertTrue(a.equals(expectedList));
		System.out.println("Expected : " +expectedList);
	}

}
