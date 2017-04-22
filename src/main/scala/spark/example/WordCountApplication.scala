package spark.example

import org.apache.spark.sql.{Dataset, SparkSession}

class WordCountApplication(spark: SparkSession) {

  import spark.implicits._

  def mostPopularWords(inputFile: String, outputFile: String): Unit = {

    val inputText = spark.sqlContext.read.text(inputFile).as[String]

    val counts = calculateWordCounts(inputText)

    counts.write.csv(outputFile)

  }

  def calculateWordCounts(inputText: Dataset[String]) = {
    val counts = inputText
      .flatMap(_.split(" "))
      .map(_.replaceAll("\\W", ""))
      .filter(!_.isEmpty)
      .groupByKey(_.toLowerCase())
      .count()
      .orderBy($"count(1)".desc, $"value")
      .limit(100)
    counts
  }
}
