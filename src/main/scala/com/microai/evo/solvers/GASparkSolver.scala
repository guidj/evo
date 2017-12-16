package com.microai.evo.solvers

import com.microai.evo.Utils
import com.microai.evo.ga._
import org.apache.spark.sql.SparkSession

import scala.reflect.ClassTag
import scala.util.Random

/**
  * Runs a Genetic Algorithm cycle for a given GAProblem using Spark
  *
  * This solver provides an implicit [[org.apache.spark.sql.SparkSession]] named `spark`.
  */
class GASparkSolver(sparkMaster: String) extends GASolver {

  override def solve[A <: Chromosome : ClassTag](problem: GAProblem[A],
                                                 policy: GAPolicy,
                                                 parameters: SolverParameters):
  List[Gene[A]] = {

    implicit val spark: SparkSession = SparkSession
      .builder()
      .appName(problem.name)
      .master(sparkMaster)
      .getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames
    //    import spark.implicits._

    var population = spark.sparkContext
      .parallelize(problem.initialPopulation)
      .map(d => Gene(d, problem.score(d)))

    val seedChromosome = problem.seed
    var bestGene = Gene(seedChromosome, problem.score(seedChromosome))

    var epoch = 1

    while (epoch < parameters.maxIterations) {

      //Crossover
      val crossoverCandidates = population.filter(_ => Utils.biasedCoin(policy.crossOverRate))
        .collect()

      val newGenes = crossoverCandidates.map { gene =>
        problem.crossover(gene.chromosome,
          crossoverCandidates(Random.nextInt(crossoverCandidates.length)).chromosome)
      }.map {
        chromosome => Gene(chromosome, problem.score(chromosome))
      }

      // randomly replace genes with new ones
      val crossedOverPop = population.map {
        gene =>
          if (Utils.biasedCoin(policy.crossOverRate)) {
            newGenes(Random.nextInt(newGenes.length))
          } else {
            gene
          }
      }

      //Mutation
      val mutatedPop = crossedOverPop.map {
        gene =>
          if (Utils.biasedCoin(policy.mutationRate)) {
            (problem.mutate(gene.chromosome), None)
          } else {
            (gene.chromosome, Some(gene.fitness))
          }
      }

      //Compute scores

      val scoredPopulation = mutatedPop.map {
        case (chromosome, None) => Gene(chromosome, problem.score(chromosome))
        case (chromosome, Some(score)) => Gene(chromosome, score)
      }

      population = scoredPopulation

      //New Best
      val highestScoredGene = scoredPopulation.reduce((a, b) => if (a.fitness > b.fitness) a else b)

      println("Gen [%d], best gene [%s], highest scored [%s]".format(
        epoch, bestGene, highestScoredGene))

      if (highestScoredGene.fitness > bestGene.fitness) {
        bestGene = highestScoredGene
      }

      epoch = epoch + 1
    }

    val finalGeneration = population.collect().toList

    spark.close()

    finalGeneration
  }
}
