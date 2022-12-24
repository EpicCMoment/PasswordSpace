package kozmikoda.passwordspace;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


// table fetching is not implemented here
// each data type should implement its own data fetching scheme in their classes

public class PSQLConnection{


    // database connection handler
    private Connection database = null;

    // SQL command execution handler
    private Statement commandExecutor = null;



    // following variables are used for accessing the database
    // ************************************
    static private String accountInfoTable = "user_credentials";
    static private String serviceInfoTable = "services";
    static private String serviceNameColumn = "service_name";
    static private String userIdentifierColumn = "user_identifier";
    static private String passwordColumn = "encrypted_password";
    // ************************************

    // connects to the database named after the user of the OS
    PSQLConnection() throws SQLException {

        if (isWindows()) {
            connectToDatabase("postgres", "postgres", "postgres");
        } else {
            connectToDatabase("template1");
        }



    }

    // connects to an arbitrary named database
    PSQLConnection(String dbName) throws SQLException {

        if (isWindows()) {
            connectToDatabase(dbName, "postgres", "postgres");
        } else {
            connectToDatabase(dbName);
        }

    }

    // connects to the database dbName
    public void connectToDatabase(String dbName) throws SQLException{

        if (isWindows()) {
            connectToDatabase(dbName, "postgres", "postgres");
            return;
        }

        if (database != null) {
            commandExecutor.close();
            database.close();

        }

        database = DriverManager.getConnection("jdbc:postgresql:" + dbName);
        commandExecutor = database.createStatement();

    }

    public void connectToDatabase(String dbName, String username, String password) throws SQLException{
        if (database != null) {
            commandExecutor.close();
            database.close();

        }

        database = DriverManager.getConnection("jdbc:postgresql:" + dbName, username, password);
        commandExecutor = database.createStatement();

    }

    // creates database if it does not exist
    public void createDatabase(String dbName) throws SQLException{
        String command = String.format("CREATE DATABASE %S;", dbName);

        commandExecutor.execute(command);

    }

    /*
        createTable(String tableName, Pair<String, String>... tableElement)

        tableName represents the table which is the subject of the insertion.

        each tableElement pair consists of a variable name (first String) and
        a variable type (second String)

     */
    @SafeVarargs
    public final void createTable(String tableName, Pair<String, String>... tableElement) throws SQLException {
        StringBuilder command = new StringBuilder("CREATE TABLE " + tableName + " (");

        for (int a = 0; a < tableElement.length; a++) {

            Pair<String, String> temp = tableElement[a];

            command.append(temp.getKey());
            command.append(" ");
            command.append(temp.getValue());
            if (a != tableElement.length -1) {
                command.append(",");
            }

        }

        command.append(");");

        commandExecutor.execute(command.toString());

    }

    public void createUserTable(String userName) throws SQLException{
        createTable(userName,
                new Pair<>(PSQLConnection.getServiceNameColumn(), "varchar(64)"),
                new Pair<>(PSQLConnection.getUserIdentifierColumn(), "varchar(64)"),
                new Pair<>(PSQLConnection.getPasswordColumn(), "varchar(200)")
                );
    }

    public final void insertIntoTable(String tableName, Object ... values) throws SQLException{

        StringBuilder command = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

        for (int i = 0; i < values.length; i++) {
            command.append("\'" + values[i].toString() + "\'" );
            if (i != values.length - 1) {
                command.append(", ");
            }
        }

        command.append(");");

        commandExecutor.execute(command.toString());

    }

    public final void removeFromTable(String tableName, String columnName, String toBeRemoved) throws SQLException{

        String removeCommand = "DELETE FROM " + tableName + " WHERE " + columnName + " = " + "'" + toBeRemoved + "'" + ";";

        commandExecutor.execute(removeCommand);

    }

    public static String getServiceInfoTable() {
        return serviceInfoTable;
    }

    public static void setServiceInfoTable(String serviceInfoTable) {
        PSQLConnection.serviceInfoTable = serviceInfoTable;
    }

    public static String getServiceNameColumn() {
        return serviceNameColumn;
    }

    public static void setServiceNameColumn(String serviceNameColumn) {
        PSQLConnection.serviceNameColumn = serviceNameColumn;
    }

    public static String getUserIdentifierColumn() {
        return userIdentifierColumn;
    }

    public static void setUserIdentifierColumn(String userIdentifierColumn) {
        PSQLConnection.userIdentifierColumn = userIdentifierColumn;
    }

    public static String getPasswordColumn() {
        return passwordColumn;
    }

    public static void setPasswordColumn(String passwordColumn) {
        PSQLConnection.passwordColumn = passwordColumn;
    }

    public Statement getCommandExecutor() {
        return commandExecutor;
    }

    public static String getAccountInfoTable() {
        return accountInfoTable;
    }

    public static void setAccountInfoTable(String accountInfoTable) {
        PSQLConnection.accountInfoTable = accountInfoTable;
    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }
}
