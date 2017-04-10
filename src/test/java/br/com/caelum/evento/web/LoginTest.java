package br.com.caelum.evento.web;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LoginTest {

	private WebDriver browser;

	@Before
	public void setUp() {
		this.browser = new FirefoxDriver();

	}

	@After
	public void tearDown() {
		this.browser.close();
	}

	@Test
	public void efetuaLogin() {
		this.browser.get("http://localhost:8080/evento/publico/login.xhtml");
		WebElement form = this.browser.findElement(By.id("dataForm"));
		form.findElement(By.id("dataForm:nome")).sendKeys("admin");
		form.findElement(By.id("dataForm:senha")).sendKeys("admin");
		form.findElement(By.id("dataForm:botaoLogin")).click();
		this.browser.get("http://localhost:8080/evento/index.xhtml");
		String conteudo = this.browser.getPageSource();
		Assert.assertTrue(conteudo.contains("Bem-vindo"));
	}

}
