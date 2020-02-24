package libraryManagement.ui.book;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import libraryManagement.database.DatabaseHandler;
import libraryManagement.ui.listGenre.ListGenreController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookController implements Initializable {
    public Text bookID;
    DatabaseHandler handler = DatabaseHandler.getInstance();
    private static final String DB_URL = "jdbc:sqlite::resource:jar:file:LibraryManagement.jar!/database.db";
    private static Connection conn = null;
    private static Statement stmt = null;
    public Button isReadToggle;
    public Button isLentToggle;
    public Button deleteButton;
    public AnchorPane rootPane;
    public TextArea descriptionBox;
    public Text bookTitle;
    public Text bookGenre;
    public Text bookAuthor;
    //open new window for text input with this textField and put a Text in this one

    public Text isLentText;
    public Text isReadText;
    String id;
    String title;
    String genre;
    String author;
    Boolean isRead;
    Boolean isLent;


    String description;
    private String selectedBookId;

    public void setSelectedBookId(String selectedBookId) {
        this.selectedBookId = selectedBookId;

        loadData();
        bookTitle.setText(title);

        bookGenre.setText(genre);
        bookAuthor.setText(author);
        bookID.setText(selectedBookId);
        isLentText.setText(isLent.toString());
        isReadText.setText(isRead.toString());
        descriptionBox.setText(description);
        System.out.println(description);
    }


    public String getselectedBookId() {
        return selectedBookId;
    }


    public BookController() throws SQLException {
    }


    public void isReadChange(ActionEvent actionEvent) {

        if (isRead) {
            isRead = false;
            String sql = "update BOOK set isRead=? where bookId=?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, isRead);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();

                isReadText.setText(isRead.toString());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!isRead) {
            isRead = true;
            String sql = "update BOOK set isRead=? where bookId=?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, isRead);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();
                isReadText.setText(isRead.toString());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public void isLentChange(ActionEvent actionEvent) {
        if (isLent == true) {
            isLent = false;
            String sql = "update BOOK set isLent=? where bookId=?";


            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, isLent);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();

                isLentText.setText(isLent.toString());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (isLent == false) {
            isLent = true;
            String sql = "update BOOK set isLent=? where bookId=?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, isLent);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();
                isLentText.setText(isLent.toString());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        createConnection();

    }

    private void createConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        // DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = handler.execQuery(qu);

        try {
            while (rs.next()) {
                if (selectedBookId == null) {
                }
                id = rs.getString("bookId");

                if (id.equals(selectedBookId)) {
                     title = rs.getString("bookTitle");

                    genre = rs.getString("bookGenre");
                    author = rs.getString("bookAuthor");
                    isRead = rs.getBoolean("isRead");
                    isLent = rs.getBoolean("isLent");
                    description = rs.getString("bookDescription");


                }


            }
        } catch (SQLException ex) {
            Logger.getLogger(ListGenreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void editDescription(ActionEvent actionEvent) {
        try {


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/book_description.fxml"));
            AnchorPane page = loader.load();
            BookDescription bookDescription = loader.getController();
            bookDescription.setParentBook(selectedBookId);
            Stage stage = new Stage();
            stage.setScene(new Scene(page));
            stage.setTitle("Book Description");
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDescription(ActionEvent actionEvent) {
        loadData();
        descriptionBox.setText(description);

    }


    public void deleteBook(ActionEvent actionEvent) {
        String sql = "DELETE FROM BOOK WHERE bookId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, Integer.parseInt(id));
            // execute the delete statement
            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book "+ title + " has been deleted.");
            alert.showAndWait();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
