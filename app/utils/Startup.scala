package utils

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import play.api.Logger

@Singleton
class Startup @Inject()(seeder: DataSeeder)(implicit ec: ExecutionContext) {
  private val log = Logger(this.getClass)

  seeder.seed().map(_ => log.info("Seeding complete"))
    .recover { case ex => log.error("Seeding failed", ex) }
}
