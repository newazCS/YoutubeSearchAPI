package YoutubeSearch;

import ReusableLibrary.AbstractClass;
import ReusableLibrary.ReusableActions;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class YoutubeAPI extends AbstractClass {
    String apiKey ="AIzaSyBsCp6wdmubpursYCH9hJ1PO5u0_kY2ZXA";
    @Test(priority = 1)
    public void youtubeSearchTest() throws InterruptedException {
        RestAssured.baseURI = "https://www.googleapis.com/customsearch";
        RestAssured.basePath = "/v1";
        Response resp =
                given()
                        .queryParam("key", apiKey)
                        .queryParam("cx", "7a9603e282b3970ec")
                        .queryParam("q", "Apple iMac")
                        .with().get().
                        then().extract().response();

        //Verify the Status code
        if(resp.statusCode()==200){
            logger.log(LogStatus.PASS, "HTTP Response is successful ");
        }else {
            logger.log(LogStatus.FAIL, "HTTP Response is fail ");
        }

        //Verify the count of title
        ArrayList titleCount = resp.path("items.title");
        System.out.println("Number of title "+titleCount.size());
        logger.log(LogStatus.INFO, "Number of title "+titleCount.size());


        //Verify the title with iMac
        for(int i=0; i<5;i++) {
            String linkUrl = resp.path("items.link[" + i + "]");
            driver.navigate().to(linkUrl);
            Thread.sleep(1000);
            //Actual title - from youtube video link
            String  ExpectedTitle = ReusableActions.captureText(driver, "(//h1[contains(@class,'title')])[1]", 0, logger, "Video Title");
            // Expected title - getting from Json response
            String ActualTitleOriginal = resp.path("items.title[" + i + "]");
            String[]at = ActualTitleOriginal.split(" - ");
            String ActualTitle = at[0];

            // Match the both title
            if (ExpectedTitle.equals(ActualTitle)) {
                logger.log(LogStatus.PASS, "Match the title: " + ExpectedTitle);
                System.out.println(ExpectedTitle);
            } else {
                logger.log(LogStatus.FAIL, "Does not match the title: " + ActualTitle);
                System.out.println(ActualTitle);
            }
        }


    }
}
