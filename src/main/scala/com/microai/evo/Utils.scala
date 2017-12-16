package com.microai.evo

import scala.util.Random

/**
  * Helper utility functions
  */
object Utils {

  /**
    * Tosses a biased coin
    * @param prob Probability of a heads (`true`) outcome.
    * @return
    */
  def biasedCoin(prob: Double): Boolean = {
    Random.nextFloat <= prob
  }
}
