package net.homelinux.mansierra.ddbb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

public class DBManager
{
	private final String dbname = "phones.db";
	private final String tableName = "phoneAppKeys";

	private final String dbDriver = "org.sqlite.JDBC";
	private final String dbType = "sqlite";
	private String DBUrl;

	private final String createTableIfNotExist = "CREATE TABLE IF NOT EXISTS %s (key TEXT, ApiKeyRegister TEXT, AppName TEXT, User TEXT, PRIMARY KEY(key, ApiKeyRegister, AppName))";
	private final String insertIntoDB = "INSERT INTO %s(key, ApiKeyRegister, AppName, User) VALUES('%s', '%s', '%s', '%s')";
	private final String deleteIntoDB = "DELETE FROM %s WHERE key = '%s' AND ApiKeyRegister = '%s' AND AppName = '%s'";
	private final String queryIntoDB_1 = "SELECT ApiKeyRegister FROM %s WHERE key = '%s' AND AppName = '%s' AND User = '%s'";
	private final String queryIntoDB_2 = "SELECT ApiKeyRegister FROM %s WHERE key = '%s' AND AppName = '%s'";

	private Connection conexion = null;
	private Statement Consulta = null;
	private ResultSet rs = null;

	public DBManager(ServletContext ServContext) throws SQLException, ClassNotFoundException
	{
		String realPath = ServContext.getRealPath("/");
		Class.forName(dbDriver);
		DBUrl = "jdbc:" + dbType + ":" + realPath + dbname;
		conexion = DriverManager.getConnection(DBUrl);
		Consulta = conexion.createStatement();
		Consulta.setQueryTimeout(30);
		Consulta.executeUpdate(String.format(createTableIfNotExist, tableName));
	}

	public void closeAndUnRegister() throws SQLException
	{
		try
		{
			conexion.close();
			java.sql.Driver mySqlDriver = DriverManager.getDriver(DBUrl);
			DriverManager.deregisterDriver(mySqlDriver);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void insert(String key, String ApiKeyRegister, String AppName, String User) throws SQLException
	{
		Consulta.executeUpdate(String.format(insertIntoDB, tableName, key, ApiKeyRegister, AppName, User));
	}

	public void delete(String key, String ApiKeyRegister, String AppName) throws SQLException
	{
		Consulta.executeUpdate(String.format(deleteIntoDB, tableName, key, ApiKeyRegister, AppName));
	}

	public String query(String key, String AppName, String User) throws SQLException
	{
		rs = Consulta.executeQuery(String.format(queryIntoDB_1, tableName, key, AppName, User));
		if (rs != null)
		{
			while (rs.next())
				return rs.getString(1);
		}
		return null;
	}

	public String query(String key, String AppName) throws SQLException
	{
		rs = Consulta.executeQuery(String.format(queryIntoDB_2, tableName, key, AppName));
		if (rs != null)
		{
			while (rs.next())
				return rs.getString(1);
		}
		return null;
	}

}
