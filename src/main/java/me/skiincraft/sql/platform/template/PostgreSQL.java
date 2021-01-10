package me.skiincraft.sql.platform.template;

import me.skiincraft.sql.platform.SQLConfiguration;
import me.skiincraft.sql.platform.SQLPlatform;
import me.skiincraft.sql.platform.UseStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSQL extends SQLPlatform {

    private Connection connection;
    public PostgreSQL(SQLConfiguration sqlConfiguration) {
        super(sqlConfiguration);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Statement createNewStatement(UseStatement useSQL) throws SQLException {
        return getStatementUpdater().getStatement(useSQL);
    }

    @Override
    public boolean connect() throws SQLException {
        try {
            if (this.connection != null) {
                if (!connection.isClosed()) {
                    return true;
                }
            }

            Class.forName(getSQLConfiguration().getDriver());
            this.connection = DriverManager.getConnection(getSQLConfiguration().getHost() + getSQLConfiguration().getDatabase(),
                    getSQLConfiguration().getUser(),
                    getSQLConfiguration().getPassword());
            connection.setAutoCommit(false);

            return true;
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getCause());
        }
    }

    @Override
    public boolean close() throws SQLException {
        if (this.connection == null || this.connection.isClosed()){
            return true;
        }

        getStatementUpdater().close();
        this.connection.close();
        return true;
    }
}
