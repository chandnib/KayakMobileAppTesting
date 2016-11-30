package pageObjects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class guests {
	
	// Locators
	
	By login_id = By.id("com.application.zomato:id/login_email");
	By pwd = By.id("com.application.zomato:id/login_password");
	By login_button = By.id("com.application.zomato:id/submit_button");
	By profile_button = By.id("com.application.zomato:id/dineline_tab_button");

	WebDriver driver;
	
	public guests(WebDriver dr) {
		driver = dr;
	}
	
	
	
	public void login(String id, String p){
		driver.findElement(login_id).sendKeys(id);
		driver.findElement(pwd).sendKeys(p);
		driver.findElement(login_button).click();
		
	}
	
	public boolean assertRoomsPerGuests(){
		timelapse();
		//Assert.assertEquals("2 Rooms", ((WebElement) By.id("com.kayak.android:id/roomsText")).getText());
		return driver.findElement(By.id("com.kayak.android:id/roomsText")).getText().equals("2 Rooms");
	}
	
	public void assertSuccessfulLogin(){
		timelapse();
		driver.findElement(profile_button).isDisplayed();
	}
	
	
	public void timelapse(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
