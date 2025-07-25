package employee

import javax.inject._
import scala.concurrent.ExecutionContext

import play.api.libs.json._
import play.api.mvc._

@Singleton
class EmployeeController @Inject()(
                                    cc: ControllerComponents,
                                    repo: EmployeeRepository
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getAllEmployees: Action[AnyContent] = Action.async {

    repo.findAll().map { employees =>

      val json = JsArray(
        employees.map { e =>
          Json.obj(
            "id"            -> e.id,
            "firstName"     -> e.firstName,
            "lastName"      -> e.lastName,
            "email"         -> e.email,
            "mobileNumber"  -> e.mobileNumber,
            "address"       -> e.address,
            "contractStart" -> e.contractStart.toString,
            "contractType"  -> e.contractType,
            "contractTime"  -> e.contractTime,
            "contractEnd"   -> e.contractEnd.map(_.toString),
            "hoursPerWeek"  -> e.hoursPerWeek,
            "createdAt"     -> e.createdAt.toInstant.toString,
            "updatedAt"     -> e.updatedAt.toInstant.toString
          )
        }
      )
      Ok(json)
    }
  }
}
