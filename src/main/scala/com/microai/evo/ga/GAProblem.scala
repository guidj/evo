package com.microai.evo.ga

/**
  * Specification for an GA problem.
  * @tparam A A type that extends the [[com.microai.evo.ga.Chromosome]] trait.
  */
trait GAProblem[A <: Chromosome]{
  /**
    * Name of problem. E.g. "Highest Sum of Pairs".
    * @return
    */
  def name: String

  /**
    * An instance of an individual to start with as the best.
    * @return
    */
  def seed: A

  /**
    * Provides the initial population for the cycle.
    * @return [[List]] of [[com.microai.evo.ga.Chromosome]] instances
    */
  def initialPopulation: List[A]

  /**
    * Computes the score of an individual.
    * @param chromosome Individual to be scored.
    * @return Chromosome score
    */
  def score(chromosome: A): Float

  /**
    * Cross-over operator for the problem.
    * @param a First parent
    * @param b Second parent
    * @return An offspring
    */
  def crossover(a: A, b: A): A

  /**
    * Mutation operator for the problem.
    * @param chromosome Chromosome to be mutated
    * @return A mutated chromosome
    */
  def mutate(chromosome: A): A
}
