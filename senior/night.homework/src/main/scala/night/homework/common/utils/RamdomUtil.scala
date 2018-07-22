package night.homework.common.utils

import java.util.UUID

/**
  * Created by Night on 2018/7/22.
  */
trait RandomUtils {
  def randomValue = Math.abs(UUID.randomUUID.hashCode);

  def random(max: Int) = randomValue % max
}