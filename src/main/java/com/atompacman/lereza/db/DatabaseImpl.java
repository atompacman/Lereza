package com.atompacman.lereza.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.atompacman.lereza.Parameters;
import com.atompacman.lereza.Parameters.Paths.Assets;
import com.atompacman.lereza.api.ConfigManager;
import com.atompacman.lereza.api.Database;
import com.atompacman.lereza.api.Wizard;
import com.atompacman.lereza.exception.DatabaseException;
import com.atompacman.lereza.gui.LoginDialog;
import com.atompacman.toolkat.exception.Throw;

public class DatabaseImpl implements Database {
	
	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final DatabaseImpl instance = new DatabaseImpl();
	}
	
	public static DatabaseImpl getInstance() {
		return InstanceHolder.instance;
	}
	
	
	
	//======================================= FIELDS =============================================\\

	private DatabaseConnection dbConnection;
	
	
	
	//======================================= METHODS ============================================\\
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private DatabaseImpl() {
		ConfigManager config = Wizard.getDevice(ConfigManager.class);
		
		String iconPath 			  = (String) Assets.LOGIN_ICON.value();
		String serverAdress 		  = config.getString(Parameters.Database.FULL_SERVER_ADRESS);
		String dbConnectorDriverClass = config.getString(Parameters.Database.DB_CONNECTOR_CLASS);
		
		Login login = LoginDialog.askForLogin("MySQL Database login", iconPath);

		if (login == null) {
			throwDBExcep("A login must be provided", null);
		}
		
		dbConnection = DatabaseConnection.connectTo(serverAdress, dbConnectorDriverClass, login);
	}
	
	
	//----------------------------------------- QUERY --------------------------------------------\\

	public ResultSet executeQuery(String sqlQuery, String... jokers) {
		return dbConnection.executeQuery(substituteJokers(sqlQuery, jokers));
	}
	
	public ResultSet executeAndStoreQuery(String sqlQuery, String... jokers) {
		return dbConnection.executeAndStoreQuery(substituteJokers(sqlQuery, jokers));
	}
	
	private String substituteJokers(String sqlQueryTempl, String[] jokers) {
		for (int i = 0; i < jokers.length; ++i) {
			sqlQueryTempl = sqlQueryTempl.replaceAll("%" + Integer.toString(i + 1), jokers[i]);
		}
		return sqlQueryTempl;
	}
	
	
	//----------------------------------------- CLOSE --------------------------------------------\\

	public void close() {
		dbConnection.close();
	}
	
	
	//----------------------------------- THROW EXCEPTIONS ---------------------------------------\\

	public static void throwDBExcep(String msg, Exception e) {
		if (e instanceof SQLException) {
			msg += " (Error code " + ((SQLException) e).getErrorCode() + ")";
		}
		Throw.aRuntime(DatabaseException.class, msg, e);
	}
}
