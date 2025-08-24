package contract

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContractRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val contracts = Table.contracts

  def findByEmployeeId(employeeId: Long): Future[Seq[Contract]] =
    db.run(
      contracts
        .filter(_.employeeId === employeeId)
        .result
    )
}
