import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[utils.Startup]).asEagerSingleton()
  }
}
