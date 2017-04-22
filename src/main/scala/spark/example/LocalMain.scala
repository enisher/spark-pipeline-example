package spark.example

import org.apache.spark.sql.SparkSession


object LocalMain {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .getOrCreate
    val wordCountApplication = new WordCountApplication(spark)

    wordCountApplication.mostPopularWords("./example.txt", "./out.txt")

  }
}
