package night.homework.common.utils.patterns

/**
  * Created by Night on 2018/7/22.
  */
trait UsingPattern {

  type Closable = {def close()}

  def using[R <: Closable, A](resource: R)(f: R => A): A = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }
}
