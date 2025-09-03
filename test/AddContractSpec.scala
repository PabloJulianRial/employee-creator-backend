import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.openqa.selenium._
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.support.ui.{ExpectedConditions => EC, Select, WebDriverWait}
import java.time.{Duration, LocalDate}

class AddContractSpec extends AnyFunSuite with Matchers {
  private def byTest(id: String): By = By.cssSelector(s"[data-testid='$id']")
  private def waitx(d: WebDriver) = new WebDriverWait(d, Duration.ofSeconds(25))

  test("adding a contract increases the count by 1") {
    val options = new ChromeOptions()
    options.addArguments("--window-size=1280,900")
    val driver = new ChromeDriver(options)

    try {
      driver.get("http://localhost:5173/")
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("employees-list")))
      val viewButtons = driver.findElements(byTest("view-edit"))
      assert(!viewButtons.isEmpty, "Need at least one employee in the list.")
      viewButtons.get(0).click()

      waitx(driver).until(EC.visibilityOfElementLocated(byTest("toggle-contracts")))
      driver.findElement(byTest("toggle-contracts")).click()
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("contracts-section")))
      waitx(driver).until(EC.or(
        EC.visibilityOfElementLocated(By.cssSelector("[data-testid='contracts-list']")),
        EC.visibilityOfElementLocated(By.cssSelector("[data-testid='contracts-empty']"))
      ))
      val before = driver.findElements(By.cssSelector("[data-testid='contract-row']")).size()

      driver.findElement(byTest("add-contract")).click()
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("contract-form")))
      waitx(driver).until(EC.visibilityOfElementLocated(byTest("contractStart")))

      new Select(driver.findElement(byTest("contractType"))).selectByValue("permanent")
      new Select(driver.findElement(byTest("contractTime"))).selectByValue("full_time")

      val dateEl = driver.findElement(byTest("contractStart"))
      val js = driver.asInstanceOf[JavascriptExecutor]
      val today = LocalDate.now().toString
      js.executeScript(
        """
        const el = arguments[0], v = arguments[1];
        const proto = Object.getPrototypeOf(el);
        const desc = Object.getOwnPropertyDescriptor(proto, 'value');
        desc.set.call(el, v);
        el.dispatchEvent(new Event('input',  { bubbles: true }));
        el.dispatchEvent(new Event('change', { bubbles: true }));
        """,
        dateEl, today
      )

      driver.findElement(byTest("save-contract")).click()

      waitx(driver).until(EC.visibilityOfElementLocated(byTest("contracts-section")))
      waitx(driver).until(EC.or(
        EC.visibilityOfElementLocated(By.cssSelector("[data-testid='contracts-list']")),
        EC.visibilityOfElementLocated(By.cssSelector("[data-testid='contracts-empty']"))
      ))
      waitx(driver).until(_ => {
        val after = driver.findElements(By.cssSelector("[data-testid='contract-row']")).size()
        after == before + 1
      })

      Thread.sleep(400)
    } finally {
      driver.quit()
    }
  }
}
