package libraryManagement.ui.book;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDescription {
    public Button submitButton;
    public TextField descriptionBox;
    public AnchorPane rootPane;
    Connection conn = null;

    public void setParentBook(String parentBook) {
        this.parentBook = parentBook;
    }

    private String parentBook;


    public void saveDescription(ActionEvent actionEvent) {
        String description = descriptionBox.getText();
        try {
            conn = DriverManager.getConnection("jdbc:sqlite::resource:jar:file:LibraryManagement-1.0-all.jar!/database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement pstmt = conn.prepareStatement("update BOOK set bookDescription=? where bookId=?");
            pstmt.setString(1, description);
            pstmt.setString(2, parentBook);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
