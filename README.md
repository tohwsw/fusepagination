# Fuse Pagination

When using Fuse to perform ETL from one database to another, a huge database with millions of columns will cause Fuse to run out of memory if all the records are selected at the same time.
This pattern shows how to implement the pagination pattern to extract records by batch.


# Installation of Fuse Bundle

In the Fuse shell, run the following command
install -s mvn:com.redhat/postgres-postgres/0.0.1-SNAPSHOT

# Camel Code Walkthrough

1) On start of the route, the target database is purged using an SQL delete query and using Camel-JDBC to communicate to the database.

2) The total count of the source database is retrieved

3) The preparePagination function is invoked.
   The DBProcessor.java contains the contains 3 main functions.
   The preparePagination function sets the loopcount variable. This variable is used to iterate the retrieval of records by batches. The pageSize value should be set. Currently it is set at 2.

4) Once the loop count is calculated, a Camel loop is implemented. 
   In each loop, the Camel route queries the database with an offset and inserts the records into the target database.
   String sqlquery = "SELECT * FROM address_info LIMIT " + pageSize + " OFFSET " +  pageSize*(int)exchange.getProperty("CamelLoopIndex");

5) The insertion is done using a thread pool so that threads can insert the records in parallel.
   The threadpool value should be set. It is currently set at 10. 


