package sparkCassandra

import org.apache.spark.sql.functions._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.SparkSession

object Manipulate {

    def main(args: Array[String]){

        val spark = SparkSession
            .builder()
            .getOrCreate()

        val csv_df = spark.read.format("csv").option("header", "true").load("/Users/arpan/spark-cassandra/previous_employees_by_title.csv")

        val newDF = csv_df.select("job_title", "employee_id", "employee_name", "first_day", "last_day").withColumn("days_worked", abs(datediff(col("first_day").cast("date"), col("last_day").cast("date"))))

        newDF.show()

        spark.stop()

    }
}