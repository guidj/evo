package com.microai.evo.ga

import scala.reflect.ClassTag

/**
  * Trait for a framework that executes the `GA` cycle for a [[com.microai.evo.ga.GAProblem]].
  */
trait GASolver {

  /**
    *
    * @param problem    A GA problem specification.
    * @param policy     GA cycle execution policy.
    * @param parameters GA cycle execution parameters.
    * @tparam A A type that represents an individual in the GA problem.
    * @return A list of genes from the final generation.
    */
  def solve[A <: Chromosome : ClassTag](problem: GAProblem[A],
                                        policy: GAPolicy,
                                        parameters: SolverParameters):
  List[Gene[A]]
}
