package busterapp.util;

import static java.lang.System.*;

public class EnvHelper {
    static boolean debug = false;

    // buster-api default
    static String busterAPIUrl = "http://34.102.239.194";
    static String busterAPIVersion = "1";

    // ngrok default
    static String ngrokHost = "host.docker.internal";
    static String ngrokPort = "4040";

    // mysql default
    static String mysqlHost = "host.docker.internal";
    static String mysqlPort = "3306";
    static String mysqlUsername = "dev";
    static String mysqlPassword = "password123";
    static String mysqlDb = "buster_dev";

    public static boolean isDebug() {
        String value = getenv("DEBUG");
        return value != null && !value.isEmpty() ? Boolean.parseBoolean(value) : debug;
    }

    public static String getBusterAPIUrl() {
        String value = getenv("BUSTER_API_URL");
        return value != null && !value.isEmpty() ? value : busterAPIUrl;
    }
    
    public static String getBusterAPIVersion() {
        String value = getenv("BUSTER_API_VERSION");
        return value != null && !value.isEmpty() ? value : busterAPIVersion;
    }

    public static String getNgrokHost() {
        String value = getenv("NGROK_HOST");
        return value != null && !value.isEmpty() ? value : ngrokHost;
    }
    
    public static String getNgrokPort() {
        String value = getenv("NGROK_PORT");
        return value != null && !value.isEmpty() ? value : ngrokPort;
    }

    public static String getMysqlHost() {
        String value = getenv("MYSQL_HOST");
        return value != null && !value.isEmpty() ? value : mysqlHost;
    }
    
    public static String getMysqlPort() {
        String value = getenv("MYSQL_PORT");
        return value != null && !value.isEmpty() ? value : mysqlPort;
    }

    public static String getMysqlUsername() {
        String value = getenv("MYSQL_USERNAME");
        return value != null && !value.isEmpty() ? value : mysqlUsername;
    }
    
    public static String getMysqlPassword() {
        String value = getenv("MYSQL_PASSWORD");
        return value != null && !value.isEmpty() ? value : mysqlPassword;
    }

    public static String getMysqlDB() {
        String value = getenv("MYSQL_DB");
        return value != null && !value.isEmpty() ? value : mysqlDb;
    }
}