package com.perfecto.sampleproject;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class PerfectoAppiumCustomerApp {
	AndroidDriver<AndroidElement> driver;
	ReportiumClient reportiumClient;

	@Test
	public void appiumTest() throws Exception {
		// Replace <<cloud name>> with your perfecto cloud name (e.g. demo) or pass it as maven properties: -DcloudName=<<cloud name>>
		String cloudName = "trial";

		// Replace <<security token>> with your perfecto security token or pass it as maven properties: -DsecurityToken=<<SECURITY TOKEN>>  More info: https://developers.perfectomobile.com/display/PD/Generate+security+tokens
		String securityToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2ZDM2NmJiNS01NDAyLTQ4MmMtYTVhOC1kODZhODk4MDYyZjIifQ.eyJpYXQiOjE3NDE2MDkwNzMsImp0aSI6ImI4Y2I0YTJlLTU2NjItNDEzOC05MDZjLWI2NTkyZTE5NGJiNCIsImlzcyI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsImF1ZCI6Imh0dHBzOi8vYXV0aDMucGVyZmVjdG9tb2JpbGUuY29tL2F1dGgvcmVhbG1zL3RyaWFsLXBlcmZlY3RvbW9iaWxlLWNvbSIsInN1YiI6ImVhMWZiYzE2LTdmZDYtNDA0Ny04ZTFiLTE5YTVlZDU3ZDFhOCIsInR5cCI6Ik9mZmxpbmUiLCJhenAiOiJvZmZsaW5lLXRva2VuLWdlbmVyYXRvciIsIm5vbmNlIjoiMmM1ZmJiN2ItZGVlZC00ZjVmLTg2NGItMmIzY2E2NTY1YTk2Iiwic2Vzc2lvbl9zdGF0ZSI6IjVjNTBjZDhhLWFkM2EtNDU1Yy1iNzBhLTZhYmUxYjI2NjcwNiIsInNjb3BlIjoib3BlbmlkIG9mZmxpbmVfYWNjZXNzIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI1YzUwY2Q4YS1hZDNhLTQ1NWMtYjcwYS02YWJlMWIyNjY3MDYifQ.LbcNLc6qtssK0inyJP4T_gAgFbaOJdTPggJ04rf6Bas";

		cloudName = PerfectoLabUtils.fetchCloudName(cloudName);
		securityToken = PerfectoLabUtils.fetchSecurityToken(securityToken);


		//Mobile: Auto generate capabilities for device selection: https://developers.perfectomobile.com/display/PD/Select+a+device+for+manual+testing#Selectadeviceformanualtesting-genCapGeneratecapabilities
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		capabilities.setCapability("model", "Galaxy S.*|LG.*");
		capabilities.setCapability("enableAppiumBehavior", true);
		capabilities.setCapability("openDeviceTimeout", 2);
		capabilities.setCapability("appPackage", "<<MY App package>>"); // Set the unique identifier of your app
		capabilities.setCapability("autoLaunch", true); // Whether to install and launch the app automatically.
		capabilities.setCapability("takesScreenshot", false);
		capabilities.setCapability("screenshotOnError", true); // Take screenshot only on errors
		capabilities.setCapability("automationName", "Appium");
		// The below capability is mandatory. Please do not replace it.
		capabilities.setCapability("securityToken", securityToken);

		driver = new AndroidDriver<AndroidElement>(new URL("https://" + cloudName  + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		reportiumClient = PerfectoLabUtils.setReportiumClient(driver, reportiumClient); //Creates reportiumClient
		reportiumClient.testStart("Android Java Native Sample", new TestContext("tag2", "tag3")); //Starts the reportium test

		reportiumClient.stepStart("first step");
		WebDriverWait wait = new WebDriverWait(driver, 30);

		// Enter your code here
		reportiumClient.stepEnd();
		// Add as many test steps as needed


	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		TestResult testResult = null;
		if(result.getStatus() == result.SUCCESS) {
			testResult = TestResultFactory.createSuccess();
		}
		else if (result.getStatus() == result.FAILURE) {
			testResult = TestResultFactory.createFailure(result.getThrowable());
		}
		reportiumClient.testStop(testResult);

		driver.close();
		driver.quit();
		// Retrieve the URL to the DigitalZoom Report
		String reportURL = reportiumClient.getReportUrl();
		System.out.println(reportURL);
	}



}

