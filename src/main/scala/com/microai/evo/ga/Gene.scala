package com.microai.evo.ga

import scala.reflect.ClassTag

/**
  *
  * @param chromosome An individual in a population
  * @param fitness A measure of fitness
  * @tparam Chromosome
  */
case class Gene[+Chromosome: ClassTag](chromosome: Chromosome, fitness: Double)
