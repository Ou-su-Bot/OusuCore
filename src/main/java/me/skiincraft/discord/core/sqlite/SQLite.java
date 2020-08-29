package me.skiincraft.discord.core.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.sqlobjects.Table;
import me.skiincraft.discord.core.sqlobjects.Table.Column;

public class SQLite {

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	
	private Plugin plugin;
	
	public SQLite(Plugin main) {
		this.plugin = main;
	}

	public synchronized boolean connect() {
		try {
			if (this.connection != null) {
				if (!connection.isClosed()) {
					System.out.println("Conexão com banco de dados já esta estabelecida!");
					return true;
				}
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:sqlite:" + plugin.getName() +"_sqlite.db";
			
			this.connection = DriverManager.getConnection(url);
			this.statement = connection.createStatement();
			
			System.out.println("Conexão com banco de dados estabelecida com sucesso.");
			return true;
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
			System.out.println("Driver JDBC não foi encontrado.");
			return false;
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("SQLException: Alguma operação com banco de dados falhou.");
			return false;
		}
	}

	public synchronized boolean stop() {
		if (this.connection == null) {
			return true;
		}
		try {
			this.connection.close();
			if (this.statement != null) {
				this.statement.close();
			}
			return true;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	public synchronized boolean existsTable(String tableName) {
		if (connection == null) {
			connect();
		}
        try {
        	ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            while (rs.next()) { 
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                	return true;
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
		}
        return false;
    }
	
	public synchronized boolean existsTable(Table table) {
		return existsTable(table.getTableName());
	}
	
	public synchronized void executeStatementTask(Consumer<Statement> statement) {
		Statement state = getOusuStatement();
		statement.accept(state);
		try {
			if (!state.isClosed()) {
				state.close();
				return;
			}
		} catch (SQLException e) {
			System.out.println("Não foi possive fechar a conexão com Statement");
			e.printStackTrace();
		}
	}
	
	public synchronized void executePrepareStatementTask(String query, Consumer<PreparedStatement> preparedstatement) {
		PreparedStatement state = getOusuPreparedStatement(query);
		preparedstatement.accept(state);
		
		try {
			if (!state.isClosed()) {
				state.close();
				return;
			}
		} catch (SQLException e) {
			System.out.println("Não foi possive fechar a conexão com PrepareStatement");
			e.printStackTrace();
		}
	}

	public synchronized void executeResultSet(String query, Consumer<ResultSet> resultset) throws SQLException {
		Statement state = getOusuStatement();
		ResultSet result = state.executeQuery(query);
		resultset.accept(result);
		try {
			if (result.isClosed()) {
				result.close();
				return;
			}
			if (!state.isClosed()) {
				state.close();
				return;
			}
		} catch (SQLException e) {
			System.out.println("Não foi possive fechar a conexão com Statement/ResultSet");
			e.printStackTrace();
		}
	}
	
	public synchronized Statement getOusuStatement() {
		if (statement != null && connection != null) {
			try {
				if (statement.isClosed()) {
					statement = connection.createStatement();
					return statement;
				}
				return statement;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return statement;	
		}
		connect();
		return statement;
	}
	
	public synchronized PreparedStatement getOusuPreparedStatement(String query) {
		if (connection != null) {
			try {
				if (preparedStatement.isClosed()) {
					preparedStatement = connection.prepareStatement(query);
					return preparedStatement;
				}
				return preparedStatement;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return preparedStatement;	
		}
		connect();
		return preparedStatement;
	}

	public synchronized void executeUpdateAsync(String update) {
		executeStatementTask(statement ->{
			try {
				statement.executeUpdate(update);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	public void createTable(Table table) {
		executeStatementTask(statement ->{
			StringBuffer buffer = new StringBuffer();
			buffer.append("CREATE TABLE IF NOT EXISTS ");
			buffer.append("" + table.getTableName() + " ");
			buffer.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
			
			int tb = 0;
			for (Column column :table.columns()) {
				tb++;
				buffer.append(column.toString());
				if (tb != table.columns().size()) {
					buffer.append(", ");
				}
			}
			buffer.append(");");
				try {
					statement.execute(buffer.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
		});
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
}
