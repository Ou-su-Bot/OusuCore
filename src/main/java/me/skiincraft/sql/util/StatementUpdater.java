package me.skiincraft.sql.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.skiincraft.sql.platform.SQLPlatform;
import me.skiincraft.sql.platform.UseStatement;
import org.postgresql.PGStatement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatementUpdater {

    private final SQLPlatform platform;
    private final List<StatementData> statementData = new ArrayList<>();
    private final ScheduledExecutorService threadPool;

    public StatementUpdater(SQLPlatform platform) {
        this.platform = platform;
        this.threadPool = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
                .setNameFormat("StatementUpdater").build());
        prepareScheduler();
    }

    private void prepareScheduler(){
        this.threadPool.scheduleAtFixedRate(() -> {
            if (getStatementData().size() == 0) {
                return;
            }
            List<StatementData> expiredData = statementData.stream().filter(data -> {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - data.getLastUpdate());
                return seconds >= 30;
            }).collect(Collectors.toList());
            for (StatementData expired : expiredData){
                try {
                    expired.getStatement().closeOnCompletion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            statementData.removeAll(expiredData);
        }, 60, 30, TimeUnit.SECONDS);
    }

    private Statement getExistent(String sql, UseStatement useSQL) throws SQLException {
        StatementData data;
        if (sql == null) {
             data = statementData.stream()
                    .filter(stmtData -> stmtData.getStatementOwner() == useSQL)
                    .filter(stmtData -> !(stmtData.getStatement() instanceof PreparedStatement))
                    .findFirst()
                    .orElse(new StatementData(getSQL().getConnection().createStatement(), useSQL));
        } else {
            data = new StatementData(getSQL().getConnection().prepareStatement(sql), useSQL);
        }

        statementData.add(data);
        return data.getStatement();
    }

    public Statement getStatement(UseStatement useSQL) throws SQLException {
        return getExistent(null, useSQL);
    }

    public PreparedStatement getStatement(String prepareSQL, UseStatement useSQL) throws SQLException {
        return (PreparedStatement) getExistent(prepareSQL, useSQL);
    }


    public SQLPlatform getSQL() {
        return platform;
    }

    private List<StatementData> getStatementData() {
        return statementData;
    }

    public void close() {
        threadPool.shutdownNow();
    }

    static class StatementData {

        private final UseStatement statementOwner;
        private final Statement statement;
        private long lastUpdate;

        public StatementData(Statement statement, UseStatement useSQL) {
            this.statementOwner = useSQL;
            this.statement = statement;
            this.lastUpdate = System.currentTimeMillis();
        }

        public UseStatement getStatementOwner() {
            return statementOwner;
        }

        public Statement getStatement() {
            lastUpdate = System.currentTimeMillis();
            return statement;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }
    }
}
