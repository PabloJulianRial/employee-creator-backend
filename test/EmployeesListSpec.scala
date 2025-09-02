import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.openqa.selenium._
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import java.time.Duration

class EmployeesListSpec extends AnyFunSuite with Matchers {
  private def byTest(id: String): By = By.cssSelector(s"[data-testid='$id']")

  test("homepage shows employees list toolbar and list") {
    val options = new ChromeOptions()

    val driver = new ChromeDriver(options)

    try {
      driver.get("http://localhost:5173/")

      val wait = new WebDriverWait(driver, Duration.ofSeconds(30))
      wait.until(ExpectedConditions.visibilityOfElementLocated(byTest("employees-toolbar")))
      wait.until(ExpectedConditions.visibilityOfElementLocated(byTest("employees-list")))

      driver.findElement(byTest("add-employee")).isDisplayed shouldBe true

      val rows = driver.findElements(byTest("employee-row"))
      rows.size() should be > 0
    } finally {
      Thread.sleep(5000)
      driver.quit()
    }
  }
}
