package testng;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class TestNgDemo {
	WebDriver driver;
	@Test
	public void openSite()
	{
		driver=new ChromeDriver();
		driver.get("https://scriptng.com/practise-site/selenium-form-automation-practice/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
	}
	
	@Test
	public void page1()
	{
	driver.findElement(By.cssSelector("input[placeholder='First Name']")).sendKeys("John");
	driver.findElement(By.cssSelector("input[placeholder='Last Name']")).sendKeys("Shaji");
	driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys("johnshaji01@gmail.com");
	
	WebElement gender=driver.findElement(By.xpath("//input[@type='radio' and @id='form-field-gender-1']"));
	JavascriptExecutor js=(JavascriptExecutor) driver;
	js.executeScript("arguments[0].click();", gender);
	
	driver.findElement(By.xpath("//input[@type='date' and @id='form-field-dob']")).sendKeys("09-07-2003");
	WebElement next=driver.findElement(By.xpath("//button[@type='button' and @data-direction='next']"));
	js.executeScript("arguments[0].click();", next);
	}
	
	@Test
	public void page2()
	{
		JavascriptExecutor js=(JavascriptExecutor) driver;
		
		driver.findElement(By.cssSelector("input[name='form_fields[mobile]']")).sendKeys("8156962729");
		WebElement check=driver.findElement(By.xpath("//input[@type='checkbox' and @value='Music']"));
		js.executeScript("arguments[0].click();", check);
		WebElement option=driver.findElement(By.xpath("//select[@name='form_fields[subject]']"));
		Select select=new Select(option);
		select.selectByVisibleText("Java");
		driver.findElement(By.xpath("//textarea[@id='form-field-address1']")).sendKeys("Kadavil (H), Kasargod");
		driver.findElement(By.xpath("//input[@id='form-field-city']")).sendKeys("Kasargod");
		driver.findElement(By.xpath("//input[@id='form-field-field_a1f76da']")).sendKeys("Kerala");
		
		driver.findElement(By.xpath("//input[@id='form-field-zip']")).sendKeys("683105");
		WebElement next=driver.findElement(By.xpath("//button[@class='elementor-button elementor-size-md e-form__buttons__wrapper__button e-form__buttons__wrapper__button-next' and @type='button']"));
		
		js.executeScript("arguments[0].click();", next);
	}
	
	@Test
	public void page3()
	{
		driver.findElement(By.xpath("//textarea[@id='form-field-comments']")).sendKeys("Hello i am John");
		WebElement next=driver.findElement(By.xpath("//button[@type='submit' and @class='elementor-button elementor-size-md e-form__buttons__wrapper__button']"));
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", next);
	}
}
