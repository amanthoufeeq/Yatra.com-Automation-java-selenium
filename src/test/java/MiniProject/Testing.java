package MiniProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Testing {
	
	WebDriver driver;
	XSSFWorkbook resultWorkbook;
	XSSFSheet resultSheet;
	XSSFSheet itemSheet;
	XSSFSheet priceSheet;
	XSSFWorkbook dataWorkbook;
	
	List<String> handles;
	WebDriverWait wait;
	JavascriptExecutor js;
	
	int result_row_count=0;
	int item_row_count=0;
	int price_row_count=0;
	
	String browserName;
	FileOutputStream fos_result;
	FileOutputStream fos_items;

	
	
	//Setting up the Driver according to browser
	@BeforeClass
	@Parameters({"browser","url"})
	public void createdriver(String br,String url)
	{
		this.browserName=br;
		switch(br.toLowerCase())
		{
		case "chrome":
			driver=new ChromeDriver();
			break;
		case "edge":
			System.setProperty("webdriver.edge.driver","C:\\Users\\2403586\\Downloads\\edgedriver_win64\\msedgedriver.exe");
			driver=new EdgeDriver();
			break;
		default:
			Assert.fail("Invalid Browser: " + br); 
			return;
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.get(url);
		
		resultWorkbook=new XSSFWorkbook();
		dataWorkbook=new XSSFWorkbook();
		
		//initializing excel elements
		resultSheet=resultWorkbook.createSheet("Test Result");
		itemSheet=dataWorkbook.createSheet("Packages");
		priceSheet=dataWorkbook.createSheet("Price");
		
		//creating excel data headers
		createResultHeader();
		createItemListHeader();
		createPriceHeader();
		
	}

	//create header for TestResult
	public void createResultHeader()
	{
		Row row=resultSheet.createRow(result_row_count++);
		row.createCell(0).setCellValue("TestCase ID");
        row.createCell(1).setCellValue("TestCase Description");
        row.createCell(2).setCellValue("Expected Result");
        row.createCell(3).setCellValue("Actual Result");
        row.createCell(4).setCellValue("Status");
	}
	
	//create header for itemsSheet
	public void createItemListHeader()
	{
		Row row=itemSheet.createRow(item_row_count++);
		row.createCell(0).setCellValue("Names");
		
	}
	
	//create header for priceSheet
	public void createPriceHeader()
	{
		Row row=priceSheet.createRow(price_row_count++);
		row.createCell(0).setCellValue("Rate");
	}
	
	//inserting items into excel
	public void addItem(String itemName)
	{
		Row row=itemSheet.createRow(item_row_count++);
		row.createCell(0).setCellValue(itemName);
	}
	
	//inserting price into excel
	public void addPrice(String cost)
	{
		Row row=priceSheet.createRow(price_row_count++);
		row.createCell(0).setCellValue(cost);
	}
	
	//inserting the TestCase details into excel
	public void addResult(String testId,String testDescription,String expResult,String actualresult,Boolean status)
	{
		Row row=resultSheet.createRow(result_row_count++);
		row.createCell(0).setCellValue(testId);
		row.createCell(1).setCellValue(testDescription);
		row.createCell(2).setCellValue(expResult);
		row.createCell(3).setCellValue(actualresult);
		row.createCell(4).setCellValue(status ? "Passed":"Failed");
	}
	
	
	
	//Navigation through Home Page and Offers Link verification
	@Test(priority=1)
	public void homePage()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		
		//handling the login screen
			
		WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//section//span//img)[1]")));		

		js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", img);
		img.click();
		
		//handling the ad
		driver.switchTo().frame("webklipper-publisher-widget-container-notification-frame");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='_we_wk_close_36cc9706-1536-4e6e-9ed8-a2a4072baa80']"))).click();
		driver.switchTo().defaultContent();
		
		//View all Offers
		
		WebElement offersLink=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='View all offers']")));
		offersLink.click();
		
		handles=new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(handles.get(1));
		
		//validating view offer link works or not
		try {
		WebElement bannerText=driver.findElement(By.xpath("//h2[@class='wfull bxs']"));
		Assert.assertTrue(bannerText.isDisplayed(),"The following page is not displayed");
		
		addResult("TC_001", "Verifies View all Offers Link", "The following webpage with Banner Text 'Great Offers & Amazing Deals' is displayed","The following webpage having Banner Text 'Great Offers & Amazing Deals' is displayed", true);
		}
		catch(Exception e)
		{
			addResult("TC_001", "Verifies View all Offers Link", "The following webpage with Banner Text 'Great Offers & Amazing Deals' is displayed","Error: "+e.getMessage(), false);
		}
	}
	
	
	//title verification in the offer page
	
	@Test(priority=2,dependsOnMethods= {"homePage"})
	public void verifyOfferPageTitle()
	{
		String actual_title;
		String expected_title="";
		
		actual_title=driver.getTitle();
		expected_title="Domestic Flights Offers | Deals on Domestic Flight Booking | Yatra.com";
		
		try 
		{	
		Assert.assertEquals(actual_title,expected_title,"The page title does not match");
		addResult("TC_002","Holiday page title verification",expected_title,actual_title,true);
		}
		catch(AssertionError e)
		{
			addResult("TC_002","Page Title Verification",expected_title,actual_title,false);
			
		}

	}
	
	
	//Validating BannerText
	@Test(priority=3)
	public void verifyBannerText() throws IOException
	{
		String bannerText_act=driver.findElement(By.xpath("//h2[@class='wfull bxs']")).getText();
		String bannertext_exp="Great Offers & Amazing Deals";
		
		try
		{
		Assert.assertEquals(bannerText_act,bannertext_exp,"Banner text does not match");
		addResult("TC_003","Verifies BannerText visibility",bannertext_exp,bannerText_act,true);
		}
		catch(AssertionError e)
		{
			addResult("TC_003","Verifies BannerText visibility",bannertext_exp,bannerText_act,false);
			
		}
		
		//getting screenshot
		TakesScreenshot shot=(TakesScreenshot) driver;
		File src=shot.getScreenshotAs(OutputType.FILE);
		File target=new File(System.getProperty("user.dir")+"\\screenshot\\Bannerpage.png");
		FileUtils.copyFile(src,target);
		
	}
	
	//Retrieve the package names
	@Test(priority=4)
	public void packageList()
	{
		
		//navigate to holiday
		WebElement holiday=driver.findElement(By.xpath("//ul[@id='offer-box-shadow']/descendant::a[text()='Holidays']"));
		holiday.click();
		int count=1;
		List<WebElement> mylist=driver.findElements(By.xpath("//ul[@class='wfull noListStyle list']/li"));
		for(WebElement element:mylist)
		{
			if(count>5)
			{
				break;
			}
			addItem(element.getAttribute("title"));
			count++;
			
		}
	}
	
	//retrieve the price
	@Test(priority=5)
	public void priceList()
	{
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		driver.findElement(By.xpath("//ul[@class='wfull noListStyle list']/li[2]")).click();
		
		
		handles=new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(handles.get(2));
		
		WebElement area=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='mbot10 lato-bold txt-cap font-18']")));
		
		//table area
		js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", area);
		
		int row_total=driver.findElements(By.xpath("//table/tbody/tr")).size();
		for(int r=2;r<=row_total;r++)
		{
			String rs=driver.findElement(By.xpath("//table/tbody/tr["+r+"]/td[2]")).getText();
			addPrice(rs);
		}
	}
	
	//writing to excel
	public void writeToExcel(String browserName) throws IOException
	{
		String filepath_result=System.getProperty("user.dir")+"\\MiniProject_"+browserName+"_TestResults"+".xlsx";
		String filepath_data=System.getProperty("user.dir")+"\\MiniProject_"+"data_"+browserName+".xlsx";

		try
		{
		fos_result=new FileOutputStream(filepath_result);
		resultWorkbook.write(fos_result);
		resultWorkbook.close();
		fos_result.close();
		
		fos_items=new FileOutputStream(filepath_data);
		dataWorkbook.write(fos_items);
		dataWorkbook.close();
		fos_items.close();
		
		System.out.println("Test results of "+browserName+" browser is saved to Excel file.");
		}
		catch(Exception e)
		{
			System.out.println("Error while saving Excel: " + e.getMessage());
		}
	}
	
	
	
	//close all browsers
	@AfterClass
	public void closeDriver() throws IOException
	{
		writeToExcel(this.browserName);
		driver.quit();
	}
	

}
