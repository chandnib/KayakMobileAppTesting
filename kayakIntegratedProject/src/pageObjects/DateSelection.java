package pageObjects;

import java.util.regex.Matcher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DateSelection {
	WebDriver driver;
	public DateSelection(WebDriver dr) {
		driver = dr;
	}
	
	
	
	public void timelapse(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean assertDateSelection(Matcher m){
		timelapse();
		//Assert.assertEquals("2 Rooms", ((WebElement) By.id("com.kayak.android:id/roomsText")).getText());
		return m.matches();
	}
}
