package com.atompacman.lereza.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

	//======================================= FIELDS =============================================\\

	private Connection 		connection;
	private List<Statement> storedStatements;
	private Statement 		tempStatement;



	//======================================= METHODS ============================================\\

	//------------------------------ PACKAGE STATIC CONSTRUCTOR ----------------------------------\\

	static DatabaseConnection connectTo(String serverAdress, 
										String dbConnectorDriverClassName, 
										Login  login) {

		try {
			Class.forName(dbConnectorDriverClassName);
		} catch (Exception e) {
			Database.throwDBExcep("Invalid database connector driver class "
					+ "\"" + dbConnectorDriverClassName + "\"", e);
		}

		return new DatabaseConnection(serverAdress, login);
	}


	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private DatabaseConnection(String serverAdress, Login login) {
		try {
			connection = DriverManager.getConnection(serverAdress, 
					login.username(), login.password());
		} catch (SQLException e) {
			Database.throwDBExcep("Could not connect to Lereza database", e);
		}
		storedStatements = new ArrayList<Statement>();
	}


	//----------------------------------------- QUERY --------------------------------------------\\

	private Statement createStatement() {
		try {
			return connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			Database.throwDBExcep("Could not create statement", e);
		}
		return null;
	}

	ResultSet executeQuery(String sqlSelectQuery) {
		if (tempStatement == null) {
			tempStatement = createStatement();
		}
		return executeQuery(sqlSelectQuery, tempStatement);
	}

	ResultSet executeAndStoreQuery(String sqlSelectQuery) {
		Statement statementToStore = createStatement();
		storedStatements.add(statementToStore);
		return executeQuery(sqlSelectQuery, statementToStore);
	}


	//----------------------------------------- CLOSE --------------------------------------------\\

	void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			Database.throwDBExcep("Could not close connection to database", e);
		}
	}

	

	//==================================== STATIC METHODS ========================================\\

	//----------------------------------------- QUERY --------------------------------------------\\
	
	private static ResultSet executeQuery(String sqlSelectQuery, Statement statement) {
		try {
			return statement.executeQuery(sqlSelectQuery);
		} catch (SQLException e) {
			Database.throwDBExcep("Could not execute query on database", e);
		}
		return null;
	}
}
