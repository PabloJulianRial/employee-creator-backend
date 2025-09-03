import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.openqa.selenium._
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.support.ui.{ExpectedConditions => EC, WebDriverWait}
import java.time.Duration

class CreateEmployeeSpec extends AnyFunSuite with Matchers {
  private def byTest(id: String): By = By.cssSelector(s"[data-testid='$id']")
  private def waitx(d: WebDriver) = new WebDriverWait(d, Duration.ofSeconds(15))

  test("create a new employee and see it in the list") {
    val email = s"auto_${System.currentTimeMillis()}@test.local"

    val options = new ChromeOptions()
    options.addArguments("--window-size=1280,900")
    val driver = new ChromeDriver(options)

    try {
      driver.get("http://localhost:5173/")
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("employees-list")))


      driver.findElement(byTest("add-employee")).click()
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("employee-form-title")))


      driver.findElement(byTest("firstName")).sendKeys("Auto")
      driver.findElement(byTest("lastName")).sendKeys("User")
      driver.findElement(byTest("email")).sendKeys(email)


      driver.findElement(byTest("save-employee")).click()


      waitx(driver).until(EC.visibilityOfElementLocated(byTest("employees-list")))
      val rows = driver.findElements(By.cssSelector("[data-testid='employee-row']"))
      val found = rows.stream().anyMatch { r =>
        val t = r.getText
        t != null && t.toLowerCase.contains(email.toLowerCase)
      }
      found shouldBe true

      Thread.sleep(1200)
      driver.quit()
    }
  }
}
