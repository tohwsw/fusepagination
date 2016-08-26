package com.redhat.fuse;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.*;
import org.slf4j.*;

public class DBProcessor implements Processor{
	
	public void process(Exchange exchange) throws Exception {
		
		// slf4j!
	    Logger log = LoggerFactory.getLogger(DBProcessor.class); 
		
		//log.info("======= in here ========");
		Map<String, Object> row = exchange.getIn().getBody(Map.class);
		log.info("custid is" + row.get("custid"));
		
        
        String sqlquery = new String();
        
        String customerId = "'" + (String)row.get("custid") + "'";
		
		String customerAddress = null;
		if(row.get("Address")!=null){
			customerAddress = "'" + (String)row.get("Address") + "'";
		}

		
		String createdTime = null;
		if(row.get("created_time")!=null){
			createdTime = "'" + (Date)row.get("created_time") + "'";
		}
		
		sqlquery = sqlquery + "INSERT into address_info_copy values (" + customerId + "," + customerAddress + "," + createdTime + ");";

        

        
        // Insert information into Postgresql database
        exchange.getIn().setBody(sqlquery);
	
	}
	
	public void preparePagination(Exchange exchange) throws Exception {
		
		// slf4j!
	    Logger log = LoggerFactory.getLogger(DBProcessor.class); 
		
		log.info("======= In Pagination ========");
		
		List<Map<String, Object>> dataList = exchange.getIn().getBody(List.class);
		//log.info("Size of the table is" + dataList.size());
		Map<String, Object> row = dataList.get(0);
		log.info("Count is " + row.get("count"));
		
		double pageSize = 2;
		
		double numofPages = (long)row.get("count")/pageSize;
		int loopcount = (int)Math.ceil(numofPages);
		
		exchange.getOut().setHeader("loopcount",loopcount);
		
		
	}
	
	public void setQuery(Exchange exchange) throws Exception {
		
		// slf4j!
	    Logger log = LoggerFactory.getLogger(DBProcessor.class);
		log.info("======= In Setquery ========");
		
		int pageSize = 2;
		
		String sqlquery = "SELECT * FROM address_info LIMIT " + pageSize + " OFFSET " +  pageSize*(int)exchange.getProperty("CamelLoopIndex");
		
		exchange.getOut().setBody(sqlquery);
		
	}
	

}
