package libraryManagement.database;


import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.sql.*;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:sqlite:database.db";

    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() throws SQLException {
        try {
            createConnection();
            createNewTable();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static DatabaseHandler getInstance() throws SQLException {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }


    /*  public static void createConnection(String dbName) throws Exception {
          String url = "jdbc:sqlite:" + dbName;
          File file = new File(dbName);

              try (Connection conn = DriverManager.getConnection(url)) {
                  if (conn != null) {
                      DatabaseMetaData meta = conn.getMetaData();
                      System.out.println("The driver name is " + meta.getDriverName());
                      System.out.println("A new database has been created.");
                  }
              } catch (SQLException e) {
                  e.printStackTrace();
              }


      }*/
    public void createNewTable() {
        String url = "jdbc:sqlite:database.db";
        String sql = "CREATE TABLE IF NOT EXISTS BOOK (\n"
                + "   bookId integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "   bookTitle VARCHAR(200),\n"
                + "   bookGenre VARCHAR(200),\n"
                + "   bookAuthor VARCHAR(200),\n"
                + "   bookDescription TEXT DEFAULT 'opis',\n"
                + "   isRead BOOLEAN DEFAULT 'false',\n"
                + "   isLent BOOLEAN DEFAULT 'false',\n"
                + "   bookLender VARCHAR(200) DEFAULT 'false'"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        ResultSet resultSet = null;
        try {
            if (conn == null) {
                System.out.println("Sorry, no connection");
            } else {
                stmt = conn.createStatement();
                resultSet = stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            System.out.println("Exeption at execQuery:dataHandler" + e.getLocalizedMessage());
            return null;
        }
        if (resultSet == null) {
            System.out.println("rs is null");
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
