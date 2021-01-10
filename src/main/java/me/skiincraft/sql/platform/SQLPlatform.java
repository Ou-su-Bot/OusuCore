package me.skiincraft.sql.platform;

import me.skiincraft.sql.util.StatementUpdater;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLPlatform {

    private final SQLConfiguration sqlConfiguration;
    private final StatementUpdater statementUpdater = new StatementUpdater(this);

    public SQLPlatform(SQLConfiguration sqlConfiguration) {
        this.sqlConfiguration = sqlConfiguration;
    }

    public abstract Connection getConnection();
    public abstract Statement createNewStatement(UseStatement useSQL) throws SQLException;
    //public abstract PreparedStatement createNewPreparedStatement(String sql, UseStatement useSQL) throws SQLException;

    public SQLConfiguration getSQLConfiguration() {
        return sqlConfiguration;
    }

    public abstract boolean connect() throws SQLException;
    public abstract boolean close() throws SQLException;

    public boolean isClosed() {
        try {
            return getConnection() == null || getConnection().isClosed();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return true;
        }
    }

    public StatementUpdater getStatementUpdater() {
        return statementUpdater;
    }
}
