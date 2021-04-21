package sparkCassandra

import org.apache.spark.sql.functions._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.SparkSession

object Read {

    def main(args: Array[String]){

        val spark = SparkSession
            .builder()
            .getOrCreate()

        val csv_df = spark.read.format("csv").option("header", "true").load("/path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv")

        csv_df.show()

        spark.stop()

    }
}
