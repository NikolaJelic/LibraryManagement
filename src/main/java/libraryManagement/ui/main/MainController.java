package libraryManagement.ui.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.sql.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    public TextField searchBox;
    public Text bookIDDisplay;
    public Text authorDisplay;
    public Text genreDisplay;
    Connection conn = null;


    public void addBook(ActionEvent actionEvent) {
        loadWindow("/AddBook.fxml", "Add new Book");
    }

    public void loadBookTable(ActionEvent actionEvent) {
        loadWindow("/list_book.fxml", "Book List");
    }

    public void loadGenreTable(ActionEvent actionEvent) {
        loadWindow("/GenreList.fxml", "Genre List");
    }

    public void loadAuthorTable(ActionEvent actionEvent) {
        loadWindow("/AuthorList.fxml", "Author List");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchBook(ActionEvent actionEvent) throws SQLException {
        String searchBook = searchBox.getText().toLowerCase();

        try {
            conn = DriverManager.getConnection("jdbc:sqlite::resource:resources/database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BOOK WHERE bookTitle = ?");
        stmt.setString(1, searchBook);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String title = rs.getString("bookTitle").toLowerCase();
            if (title.equals(searchBook)) {
                String id = rs.getString("bookID");
                String genre = rs.getString("bookGenre");
                String author = rs.getString("bookAuthor");

                bookIDDisplay.setText(id);
                genreDisplay.setText(genre);
                authorDisplay.setText(author);

            }
        } else if (!rs.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Book not found.");
            alert.showAndWait();

            bookIDDisplay.setText("Book ID");
            genreDisplay.setText("Genre");
            authorDisplay.setText("Author");
        }


    }
}
