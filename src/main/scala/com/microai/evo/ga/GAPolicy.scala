package com.microai.evo.ga

/**
  * A policy for the [[com.microai.evo.ga.GASolver]].
  * @param crossOverRate The probability of given chromosome being cross-over with another.
  * @param mutationRate The probability of given chromosome being mutated.
  */
case class GAPolicy(crossOverRate: Float, mutationRate: Float)
