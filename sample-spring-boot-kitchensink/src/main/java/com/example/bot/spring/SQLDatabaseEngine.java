package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String result = null;
		Statement stmt = null;
		String tableName = "keywordtable";
		String query = "SELECT response" +
						"FROM " + tableName +
						"WHERE keyword = '" + text + "'";
		try {
			stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				result = rs.getString("response");
			}
		} catch (SQLException e) {
			log.info("SQLException in  query: {}", e.toString());
		} finally {
			try{
				if(stmt != null) {stmt.close();}
			} catch (SQLException ex){
				log.info("SQLException while closing: {}", ex.toString());
			}
		}
		if(result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
