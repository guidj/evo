name := "evo"

version := "1.0"

scalaVersion := "2.11.11"


val sparkVersion = "2.2.0"
val kryoVersion = "4.0.1"
val algebirdVersion = "0.13.3"
val scalaTestVersion = "3.0.4"
val scallopVersion = "3.1.1"

libraryDependencies ++= List(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion, // % "provided"
  "com.esotericsoftware" % "kryo" % kryoVersion,
  "com.twitter" %% "algebird-core" % algebirdVersion,
  "org.rogach" %% "scallop" % scallopVersion,
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
