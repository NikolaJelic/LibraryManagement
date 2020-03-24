package libraryManagement.database;


import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler {

    private static DatabaseHandler handler = null;



    private static Connection conn = null;
    private static Statement stmt = null;
    private static final String DB_URL = "jdbc:sqlite:./Biblioteka/database.db";
    private Path SlikePath;

    private DatabaseHandler() throws SQLException {
        try {


            File Slike = new File("./Biblioteka/Slike");
            SlikePath = Paths.get(Slike.getAbsolutePath());
            //jarLocation.mkdir();
            boolean dirCreated = Slike.mkdirs();
            if (dirCreated) {
                System.out.println("folder napravljen");
            } else {
                System.out.println("nije napravljen");
            }
//Creates a new folder in which all the cover images, database and generated pdf will be stored
            List<String> lines = Arrays.asList("U ovaj folder se automatski dodaju sve slike.", "Mijenjanje naziva ili lokacije ovog foldera ili njegovog sadrzaja moze izazvati probleme u radu programa!");
            Path file = Paths.get(Slike.getAbsolutePath() + "//Napomena.txt");
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createConnection();
        createNewTable();
    }

    public Path getSlikePath() {
        return SlikePath;
    }

    public static DatabaseHandler getInstance() throws SQLException {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public void createNewTable() {
        String url = "jdbc:sqlite:./Biblioteka/database.db";
        String sql = "CREATE TABLE IF NOT EXISTS BOOK (\n"
                + "   bookId integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "   bookTitle VARCHAR(200),\n"
                + "   bookGenre VARCHAR(200),\n"
                + "   bookAuthor VARCHAR(200),\n"
                + "   imagePath VARCHAR(200),\n"
                + "   bookDescription TEXT DEFAULT 'opis',\n"
                + "   isRead BOOLEAN DEFAULT 'ne',\n"
                + "   isLent BOOLEAN DEFAULT 'ne',\n"
                + "   bookLender VARCHAR(200) DEFAULT 'Ime pozajmioca'"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
