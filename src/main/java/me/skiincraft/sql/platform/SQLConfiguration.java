package me.skiincraft.sql.platform;

public class SQLConfiguration {

    private String driver;
    private String host;
    private String user;
    private String password;
    private String database;

    public SQLConfiguration() {
    }

    public SQLConfiguration(String driver, String host, String user, String password, String database) {
        this.driver = driver;
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public String getDriver() {
        return driver;
    }

    public SQLConfiguration setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public String getHost() {
        return host;
    }

    public SQLConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SQLConfiguration setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public SQLConfiguration setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getUser() {
        return user;
    }

    public SQLConfiguration setUser(String user) {
        this.user = user;
        return this;
    }

    public static SQLConfiguration getPostgresConfig(){
        return new SQLConfiguration()
                .setDriver("org.postgresql.Driver")
                .setHost("jdbc:postgresql://")
                .setDatabase("postgres");
    }

    @Override
    public String toString() {
        return "SQLConfiguration{" +
                "driver='" + driver + '\'' +
                ", host='" + host + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
