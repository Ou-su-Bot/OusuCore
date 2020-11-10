package me.skiincraft.discord.core.sqlite;

import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.sqlobjects.Table;
import me.skiincraft.discord.core.sqlobjects.Table.Column;

import java.sql.*;
import java.util.function.Consumer;

public class SQLite {

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private final String databaseName;

	public String getDatabaseName() {
		return databaseName;
	}

	public SQLite(String databaseName) {
		this.databaseName = databaseName;
	}

	public synchronized boolean connect() {
		try {
			if (this.connection != null) {
				if (!connection.isClosed()) {
					OusuCore.getLogger().warn("Ainda há uma conexão estabelecida com banco de dados.");
					return true;
				}
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s_sqlite.db", databaseName));
			this.statement = connection.createStatement();
			OusuCore.getLogger().info("Conexão com banco de dados estabelecida com sucesso.");
			return true;
		} catch (ClassNotFoundException e) {
			OusuCore.getLogger().info("Não foi encontrado o Driver JDBC necessario para ativar o banco de dados.");
			return false;
		} catch (SQLException e) {
			OusuCore.getLogger().info("OusuCore falhou ao tentar conectar com o banco de dados.");
			OusuCore.getLogger().throwing(e);
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
		} catch (SQLException e) {
			OusuCore.getLogger().info("OusuCore falhou ao tentar parar conexão com o banco de dados.");
			OusuCore.getLogger().throwing(e);
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
			}
		} catch (SQLException e) {
			OusuCore.getLogger().error("Não foi possível fechar a conexão com um statement");
			OusuCore.getLogger().throwing(e);
		}
	}
	
	public synchronized void executePrepareStatementTask(String query, Consumer<PreparedStatement> preparedstatement) {
		PreparedStatement state = getOusuPreparedStatement(query);
		preparedstatement.accept(state);
		
		try {
			if (!state.isClosed()) {
				state.close();
			}
		} catch (SQLException e) {
			OusuCore.getLogger().error("Não foi possível fechar a conexão com um prepared statement");
			OusuCore.getLogger().throwing(e);
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
			}
		} catch (SQLException e) {
			OusuCore.getLogger().error("Não foi possível fechar a conexão com um statement/resultset");
			OusuCore.getLogger().throwing(e);
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
			StringBuilder buffer = new StringBuilder();
			buffer.append("CREATE TABLE IF NOT EXISTS ")
					.append(table.getTableName())
					.append(" (ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
			
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
					OusuCore.getLogger().info(String.format("[SQL] A tabela '%s' foi criada com sucesso", table.getTableName()));
				} catch (SQLException e) {
					OusuCore.getLogger().error("[SQL] Não foi possível criar uma nova tabela.");
					OusuCore.getLogger().throwing(e);
				}
		});
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}

}
