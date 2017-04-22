package spark.example

import org.apache.spark.sql.SparkSession


object ClusterMain {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .getOrCreate
    val wordCountApplication = new WordCountApplication(spark)

    wordCountApplication.mostPopularWords(args(0), args(1))
  }
}
