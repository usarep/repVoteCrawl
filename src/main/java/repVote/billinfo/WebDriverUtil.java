package repVote.billinfo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverUtil
{

	public static WebElement findElement(WebElement root, By by)
	{
		WebElement result = null;
		
		try {
			result = root.findElement(by);
		} catch (Exception e) {}
		
		return result;
	}
	
	public static List<WebElement> findElements(WebElement root, By by)
	{
		List<WebElement> result = null;
		
		try {
			result = root.findElements(by);
		} catch (Exception e) {}
		
		return result;
	}
	
	public static WebElement findElement(WebDriver driver, By by)
	{
		WebElement result = null;
		
		try {
			result = driver.findElement(by);
		} catch (Exception e) {}
		
		return result;
	}
	
	public static List<WebElement> findElements(WebDriver driver, By by)
	{
		List<WebElement> result = null;
		
		try {
			result = driver.findElements(by);
		} catch (Exception e) {}
		
		return result;
	}
}
