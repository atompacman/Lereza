package com.atompacman.lereza.api;

import java.sql.ResultSet;

public interface Database extends Device {

	//----------------------------------------- QUERY --------------------------------------------\\

	ResultSet executeQuery(String sqlSelectQuery, String... jokers);

	ResultSet executeAndStoreQuery(String sqlSelectQuery, String... jokers);

	
	//----------------------------------------- CLOSE --------------------------------------------\\

	void close();
}
