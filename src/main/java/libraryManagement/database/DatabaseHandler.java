package libraryManagement.database;


import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:sqlite::resource:jar:file:LibraryManagement-1.0-all.jar!/database.db";

    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() throws SQLException {
        createConnection();
     //   setBookTable();
    }

    public static DatabaseHandler getInstance() throws SQLException {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    //     bookId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,  INCREMENT BY 1) ,
//
 /*   private void setBookTable() throws SQLException {
        String TABLE_NAME = "BOOK";
            stmt = conn.createStatement();
    //        DatabaseMetaData dbm = conn.getMetaData();
      //      ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            System.out.println("this is called");
        try ( conn = DriverManager.getConnection(DB_URL)
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     bookId INTEGER  not null  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ,\n "
                        + "     bookTitle varchar(200), \n"
                        + "     bookAuthor varchar(200),\n"
                        + "     bookGenre varchar(200),\n"
                        + "     bookDescription varchar(1000) default 'Description '  ,\n"
                        + "     isRead boolean default false,\n"
                        + "     isLent boolean default false,\n"
                        + "     CONSTRAINT primary_key PRIMARY KEY (bookId) "
                        + ")");
                System.out.println("table is made");



    }*/

    private void createConnection() throws SQLException {
        SQLiteConfig sqliteConfig = new SQLiteConfig();
        sqliteConfig.enforceForeignKeys(true);

        SQLiteDataSource dataSource = new SQLiteDataSource(sqliteConfig);
        dataSource.setUrl(DB_URL);

        conn = dataSource.getConnection();
        System.out.println("connection is made");
    }

    public ResultSet execQuery(String query) {
        ResultSet resultSet;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exeption at execQuery:dataHandler" + e.getLocalizedMessage());
            return null;
        } finally {

        }
        return resultSet;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "error" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {

        }
    }

}
