package sparkCassandra

import org.apache.spark.sql.functions._
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.SparkSession

object ETL {

    def main(args: Array[String]){

        val spark = SparkSession
            .builder()
            .getOrCreate()

        val csv_df = spark.read.format("csv").option("header", "true").load("/Users/arpan/spark-cassandra/previous_employees_by_title.csv")

        csv_df.createCassandraTable("demo", "pre", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
        csv_df.write.cassandraFormat("pre", "demo").mode("append").save()

        spark.conf.set("spark.sql.catalog.cassandra", "com.datastax.spark.connector.datasource.CassandraCatalog")

        import spark.implicits._

        spark.sql("use cassandra.demo")

        val sqlDF = spark.sql("select job_title, employee_id, employee_name, abs(datediff(last_day, first_day)) as days_worked from pre")

        sqlDF.createCassandraTable("demo", "post", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
        sqlDF.write.cassandraFormat("post", "demo").mode("append").save()

        spark.stop()
    }
}