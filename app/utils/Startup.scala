package utils

import javax.inject.{Inject, Singleton}
import play.api.inject.ApplicationLifecycle
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Startup @Inject()(
                         dataSeeder: DataSeeder,
                         lifecycle: ApplicationLifecycle
                       )(implicit ec: ExecutionContext) {
  println("Running DataSeeder on startup")

  dataSeeder.seed().map { _ =>
    println("Seeding employees succeeded")
  }.recover { case ex =>
    println(s"Seeding employees failed: ${ex.getMessage}")
  }

  lifecycle.addStopHook { () =>
    println("Application shutting down...")
    Future.successful(())
  }
}
