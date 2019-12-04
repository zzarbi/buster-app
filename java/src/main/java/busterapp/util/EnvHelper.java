package busterapp.util;

public class EnvHelper {
    static boolean debug = false;

    static String busterAPIUrl = "http://34.102.239.194";
    static String busterAPIVersion = "1";

    static String ngrokHost = "host.docker.internal";
    static String ngrokPort = "4040";

    static String mysqlHost = "host.docker.internal";
    static String mysqlPort = "3306";
    static String mysqlUsername = "dev";
    static String mysqlPassword = "password123";
    static String mysqlDb = "buster_dev";

    public static String getBusterAPIUrl() {
        return !System.getenv("BUSTER_API_URL").isEmpty() ? System.getenv("BUSTER_API_URL") : busterAPIUrl;
    }
    
    public static String getBusterAPIVersion() {
        return !System.getenv("BUSTER_API_VERSION").isEmpty() ? System.getenv("BUSTER_API_VERSION") : busterAPIVersion;
    }

    public static String getNgrokHost() {
        return !System.getenv("NGROK_HOST").isEmpty() ? System.getenv("NGROK_HOST") : ngrokHost;
    }
    
    public static String getNgrokPort() {
        return !System.getenv("NGROK_PORT").isEmpty() ? System.getenv("NGROK_PORT") : ngrokPort;
    }

    public static String getMysqlHost() {
        return !System.getenv("MYSQL_HOST").isEmpty() ? System.getenv("MYSQL_HOST") : mysqlHost;
    }
    
    public static String getMysqlPort() {
        return !System.getenv("MYSQL_PORT").isEmpty() ? System.getenv("MYSQL_PORT") : mysqlPort;
    }

    public static String getMysqlUsername() {
        return !System.getenv("MYSQL_USERNAME").isEmpty() ? System.getenv("MYSQL_USERNAME") : mysqlUsername;
    }
    
    public static String getMysqlPassword() {
        return !System.getenv("MYSQL_PASSWORD").isEmpty() ? System.getenv("MYSQL_PASSWORD") : mysqlPassword;
    }

    public static String getMysqlDB() {
        return !System.getenv("MYSQL_DB").isEmpty() ? System.getenv("MYSQL_DB") : mysqlDb;
    }
}