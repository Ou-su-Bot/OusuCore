package me.skiincraft.discord.core.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.function.Consumer;

import me.skiincraft.discord.core.plugin.Plugin;

public class SQLite {

	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	
	private Plugin plugin;

	public Connection getConnection() {
		return connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public SQLite(Plugin main) {
		this.plugin = main;
	}

	public synchronized void abrir() {
		try {
			if (connection != null) {
				if (!connection.isClosed()) {
					return;
				}
			}
			//new File("banco_de_dados").mkdirs();
			Class.forName("com.mysql.jdbc.Driver"); //Driver
			String url = "jdbc:sqlite:" + plugin.getDiscordInfo().getBotname() +"_sqlite.db";
			
			this.connection = DriverManager.getConnection(url);
			this.statement = connection.createStatement();
			
			System.out.println("Conexão com banco de dados estabelecida com sucesso.");
			return;
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
			System.out.println("Driver JDBC não foi encontrado.");
			return;
		} catch (SQLTimeoutException exception) {
			exception.printStackTrace();
			System.out.println("Tempo de conexão excedido");
			return;
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("SQLException: Alguma operação com banco de dados falhou.");
			return;
		}
	}

	public synchronized void close() {
		if (this.connection == null) {
			return;
		}
		try {
			this.connection.close();
			if (this.statement != null) {
				this.statement.close();
			}
			return;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
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
	
	public Statement getOusuStatement() {
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
		abrir();
		return statement;
	}
	
	public PreparedStatement getOusuPreparedStatement(String query) {
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
		abrir();
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

	
	public void createTable(String tablename, String...strings) {
		executeStatementTask(statement ->{
				StringBuffer buffer = new StringBuffer();
				buffer.append("CREATE TABLE IF NOT EXISTS ");
				buffer.append("`" + tablename + "` ");
				buffer.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
				for (int i = 0; i < strings.length; i++) {
					buffer.append(strings[i]);
					if (i != strings.length -1) {
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

	public synchronized void setup() {
		if (this.connection == null || this.statement == null) {
			/*ousu.logger("Conexão ou Statement estão nulos.", Level.SEVERE);
			ousu.setDBSQL(false);*/
			return;
		}
			createTable("users",
					"`userid` VARCHAR(64) NOT NULL",
					"`username` VARCHAR(64) NOT NULL",
					"`lastpp` VARCHAR(64) NOT NULL",
					"`lastscore` VARCHAR(64) NOT NULL");
			
			createTable("servidores",
					"`guildid` VARCHAR(64) NOT NULL",
					"`nome` VARCHAR(64) NOT NULL",
					"`membros` INT",
					"`prefix` VARCHAR(10) NOT NULL",
					"`adicionado em` VARCHAR(24) NOT NULL",
					"`language` VARCHAR(24) NOT NULL");

			//ousu.setDBSQL(true);
	}
}
