package ReusableLibrary;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReusableActions {

    //Create a reusable method for navigate to a page
    public static WebDriver defineTheDriver() throws IOException, InterruptedException {
        // Kill all chrome instances that are open
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
        Thread.sleep(1500);
        //set the path of the driver
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
       //define the chrome options argument
        ChromeOptions options = new ChromeOptions();
        // maximized windows
        options.addArguments("start-maximized");
        //set the driver to incognito - private
        options.addArguments("incognito");
        //set it to headless
        //options.addArguments("headless");

        // define the webdriver
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;

    }// end of defineTheDriver method

    // method to click on any web element by explicit wait

    public static void clickOnElement(WebDriver driver, String xpathLocator, ExtentTest logger, String elementName){
        // define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver,10);

        //use try catch to locate an element and click on it
        try{
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator))).click();
            logger.log(LogStatus.PASS,"Successfully click on Element"+elementName);
        }catch (Exception e){
            System.out.println("Unable to click on element "+elementName+ " "+ e);
            logger.log(LogStatus.FAIL,"Failure"+elementName);
        }
    }// end of click method


    public static void sendKeysMethod(WebDriver driver, String xpathLocator,String userValue,ExtentTest logger, String elementName){
        // define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver,10);

        //use try catch to locate an element and click on it
        try{
           WebElement element =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
           element.clear();
           element.sendKeys(userValue);
            logger.log(LogStatus.PASS,"Successfully Send keys ");
        }catch (Exception e){
            System.out.println("Unable to click on element "+elementName+ " "+ e);
        }
    }// end of  sendKeysMethod


    public static void Submit(WebDriver driver, String xpathLocator,ExtentTest logger){
        // define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver,10);

        //use try catch to locate an element and click on it
        try{
            WebElement element =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
            element.submit();
            logger.log(LogStatus.PASS,"Successfully Submit ");
        }catch (Exception e){
            System.out.println("Unable to click on element "+ e);
            logger.log(LogStatus.FAIL,"Failure");
        }
    }// end of submit method


    public static void dropdownByText(WebDriver driver,String xpath,String userValue,String elementName){
        WebDriverWait wait = new WebDriverWait(driver,10);
        try{
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            Select dropDown = new Select(element);
            dropDown.selectByVisibleText(userValue);
        } catch (Exception e) {
            System.out.println("Unable to select a value from " + elementName + " " + e);
           // getScreenShot(driver,elementName,logger); Update and add logger
        }
    }//end of dropdown by text

    public static String captureText(WebDriver driver,String xpath,int index,ExtentTest logger,String elementName){
        WebDriverWait wait = new WebDriverWait(driver,10);
        String result = "";
        try{
            WebElement element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).get(index);
            result = element.getText();
            logger.log(LogStatus.PASS,"Successfully Capture Text "+elementName);
        } catch (Exception e) {
            System.out.println("Unable to select a value from " + elementName + " " + e);
            logger.log(LogStatus.FAIL,"Failure"+elementName);
        }
        return result;
    }//end of dropdown by text

    //house action

    public static void hoverAction(WebDriver driver,String xpath,String elementName){
        WebDriverWait wait = new WebDriverWait(driver,10);
        Actions mouseAction = new Actions(driver);

        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            mouseAction.moveToElement(element).perform();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Unable to Hover on element " + elementName + " " + e);
        }

    }// end of hover action


    public static void clickMultiElement(WebDriver driver,String xpath,int index,String elementName){
        WebDriverWait wait = new WebDriverWait(driver,10);

        try{
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).get(index).click();

        } catch (Exception e) {
            System.out.println("Unable to select a value from " + elementName + " " + e);
        }

    }


    //method to capture screenshot when logger fails
    public static void getScreenShot(WebDriver driver,String imageName, ExtentTest logger) {
        try {
            String fileName = imageName + ".png";
            String directory = null;
            String snPath = null;
            directory = "src/main/java/HTMLReport/Screenshots/";
            snPath = "Screenshots//";
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, new File(directory + fileName));
            //String imgPath = directory + fileName;
            String image = logger.addScreenCapture(snPath + fileName);
            logger.log(LogStatus.FAIL, "", image);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL, "Error Occured while taking SCREENSHOT!!!");
            e.printStackTrace();
        }
    }//end of getScreenshot method

    public static void uploadFile(String fileLocation) {
        try {
//Setting clipboard with file location
            StringSelection stringSelection = new StringSelection(fileLocation);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
//native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }//end of uploadFile method



}//end of class
