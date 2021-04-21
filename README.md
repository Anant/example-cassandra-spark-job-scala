# Apache Spark Jobs in Scala for Cassandra Data Operations

## Prerequisites
- Docker
- sbt
- Apache Spark 3.0.x

## **1. Build Fat JAR**

### **1.1 - Clone repo and `cd` into it**

```bash
git clone https://github.com/Anant/example-cassandra-spark-job-scala.git
```

```bash
cd example-cassandra-spark-job-scala
```

### **1.2 - Start sbt server in directory**

```bash
sbt
```

### **1.3 - Run `assembly` in sbt server**

```bash
assembly
```

## **2. Navigate to Spark Directory and Start Spark**

### **2.1 - Start Master**

```bash
./sbin/start-master.sh
```

### **2.2 - Get Master URL**

Navigate to `localhost:8080` and copy the master URL.

### **2.3 - Start Worker**

```bash
./sbin/start-slave.sh <master-url>
```

## **3. Start Apache Cassandra Docker Container**
```bash
docker run --name cassandra -p 9042:9042 -d cassandra:latest
```

### **3.1 - Run CQLSH**
```bash
docker exec -it cassandra CQLSH
```

### **3.2 - Create `demo` keyspace**
```bash
CREATE KEYSPACE demo WITH REPLICATION={'class': 'SimpleStrategy', 'replication_factor': 1};
```

## **4. Read Spark Job**
In this job, we will look at a CSV with 100,000 records and load it into a dataframe. Once read, we will display the first 20 rows.
```bash
./bin/spark-submit --class sparkCassandra.Read \
--master <master-url> \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

## **5. Manipulate Spark Job**
In this job, we will do the same read; however, we will now take the `first_day` and `last_day` columns and calculate the absolute value difference in days worked. Again, then display the top 20 rows.

```bash
./bin/spark-submit --class sparkCassandra.Manipulate \
--master <master-url> \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

## **6. Write to Cassandra Spark Job**
In this job, we will do the same thing we did in the manipulate job; however, we will now write the outputted dataframe to Cassandra instead of just displaying it to the console.
```bash
./bin/spark-submit --class sparkCassandra.Write \
--master <master-url> \
--conf spark.cassandra.connection.host=127.0.0.1 \
--conf spark.cassandra.connection.port=9042 \
--conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

## **7. SparkSQL Spark Job**
In this job, we will write the CSV data into one Cassandra table and then pick it up using SparkSQL and transform it at the same time. We will then write the newly transformed data into a new Cassandra table.
```bash
./bin/spark-submit --class sparkCassandra.ETL \
--master <master-url> \
--conf spark.cassandra.connection.host=127.0.0.1 \
--conf spark.cassandra.connection.port=9042 \
--conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

### Additional Resources
- [Live Demo]()
- [Accompanying Blog]()
- [Accompanying SlideShare]()
