package com.atompacman.lereza.core.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.atompacman.lereza.core.Parameters;
import com.atompacman.lereza.core.Wizard;
import com.atompacman.lereza.core.Parameters.Paths.Assets;
import com.atompacman.toolkat.exception.Throw;
import com.atompacman.toolkat.gui.Login;
import com.atompacman.toolkat.gui.LoginDialog;
import com.atompacman.toolkat.module.Module;

public class Database extends Module {
	
	//====================================== SINGLETON ===========================================\\

	private static class InstanceHolder {
		private static final Database instance = new Database();
	}
	
	public static Database getInstance() {
		return InstanceHolder.instance;
	}
	
	
	
	//======================================= FIELDS =============================================\\

	private DatabaseConnection dbConnection;
	
	
	
	//======================================= METHODS ============================================\\
	
	//---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

	private Database() {
		String iconPath 			  = Assets.LOGIN_ICON;
		String serverAdress 		  = Wizard.getString(Parameters.Database.FULL_SERVER_ADRESS);
		String dbConnectorDriverClass = Wizard.getString(Parameters.Database.DB_CONNECTOR_CLASS);
		
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
	
	
	//-------------------------------------- FINALIZE --------------------------------------------\\	
	
	public void finalize() {
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
