package sparkCassandra

import org.apache.spark.sql.functions._
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.SparkSession

object Write {

    def main(args: Array[String]){

        val spark = SparkSession
            .builder()
            .getOrCreate()

        val csv_df = spark.read.format("csv").option("header", "true").load("/path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv")

        val calcDF = csv_df.select("job_title", "employee_id", "employee_name", "first_day", "last_day").withColumn("days_worked", abs(datediff(col("first_day").cast("date"), col("last_day").cast("date"))))

        val finalDF = calcDF.select("job_title", "employee_id", "employee_name", "days_worked")

        finalDF.createCassandraTable("demo", "test", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
        finalDF.write.cassandraFormat("test", "demo").mode("append").save()

        spark.stop()
    }
}
