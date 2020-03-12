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

public class Lender {
    public TextField inputBox;
    public Button saveButton;
    public AnchorPane rootPane;
    private Connection conn;

    public void setParentBook(String parentBook) {
        this.parentBook = parentBook;
    }

    private String parentBook;

    public void saveLender(ActionEvent actionEvent) {
        String lender = inputBox.getText();
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement pstmt = conn.prepareStatement("update BOOK set bookLender=? where bookId=?");
            pstmt.setString(1, lender);
            pstmt.setString(2, parentBook);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
