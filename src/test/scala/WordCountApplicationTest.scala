import org.apache.spark.sql.SparkSession
import org.scalatest.{FunSuite, ShouldMatchers}
import spark.example.WordCountApplication

class WordCountApplicationTest extends FunSuite with ShouldMatchers {
  private val spark = SparkSession.builder().master("local[*]").getOrCreate
  import spark.implicits._

  private val app = new WordCountApplication(spark)

  test("should calculate counts") {
    val input = Seq("Learning Spark is easy").toDS

    val result = app.calculateWordCounts(input).collect()

    result should be (Array(("easy", 1),("is", 1),("learning", 1),("spark", 1)))
  }

  test("should disregard punctuation") {
    val input = Seq("spark spark!").toDS

    val result = app.calculateWordCounts(input).collect()

    result should be (Array(("spark", 2)))
  }

  test("should disregard case") {
    val input = Seq("Spark spark").toDS

    val result = app.calculateWordCounts(input).collect()

    result should be (Array(("spark", 2)))
  }
}
