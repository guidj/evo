package com.microai.evo.examples.highestsum

import com.microai.evo.Utils
import com.microai.evo.ga.{GAPolicy, GAProblem, SolverParameters}
import com.microai.evo.solvers.GASparkSolver
import org.rogach.scallop.ScallopConf

import scala.util.Random


/**
  * Searches a pair of numbers that yield the highest sum, given an initial population of pairs
  * Uses genetic algorithm to search for the best solution
  */
class HighestSumOfPair extends GAProblem[Dos] with Serializable {
  /**
    * Name of problem. E.g. "Highest Sum of Pairs".
    *
    * @return
    */
  override def name: String = this.getClass.getCanonicalName

  /**
    * An instance of an individual to start with as the best.
    *
    * @return
    */
  override def seed: Dos = Dos(0, 0)

  /**
    * Provides the initial population for the cycle
    *
    * @return
    */
  override def initialPopulation: List[Dos] = {

    val individuals = List.range(0, 100).map(_ => Dos(Random.nextInt(9 + 1),
      Random.nextInt(9 + 1)))

    individuals
  }

  /**
    * Computes the score of an individual.
    *
    * @param chromosome Individual to be scored
    * @return
    */
  override def score(chromosome: Dos): Float = {
    (chromosome.first * chromosome.second) - Random.nextInt(10)
  }

  /**
    * Cross-over operator for the problem.
    *
    * @param a First parent
    * @param b Second parent
    * @return
    */
  override def crossover(a: Dos, b: Dos): Dos = {
    val values = scala.Array(a.first, b.first, a.second, b.second)

    Dos(values(Random.nextInt(4)), values(Random.nextInt(4)))
  }

  /**
    * Mutation operator for the problem.
    *
    * @param chromosome Individual to be mutated
    * @return
    */

  override def mutate(chromosome: Dos): Dos = {
    if (Utils.biasedCoin(.5f)) {
      Dos(chromosome.first + 1, chromosome.second - 1)
    } else if (Utils.biasedCoin(.5f)) {
      Dos(chromosome.first - 1, chromosome.second + 1)
    } else {
      chromosome
    }
  }
}

object HighestSumOfPair {

  class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val master = opt[String](required = false, default = Some("local[%d]".format(
      Runtime.getRuntime.availableProcessors)))
    verify()
  }

  def main(args: Array[String]): Unit = {

    val conf = new Conf(args)

    val sparkSolver = new GASparkSolver(conf.master())

    val problem = new HighestSumOfPair()
    val policy = GAPolicy(crossOverRate = 0.3f, mutationRate = 0.3f)
    val parameters = SolverParameters(maxIterations = 100)

    sparkSolver.solve(problem, policy, parameters)
  }
}
