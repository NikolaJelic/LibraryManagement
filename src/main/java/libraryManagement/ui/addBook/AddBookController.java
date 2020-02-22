package libraryManagement.ui.addBook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import libraryManagement.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddBookController implements Initializable {


    @FXML
    public TextField title;
    @FXML
    public TextField author;
    @FXML
    public TextField genre;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public AnchorPane rootPane;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            databaseHandler = DatabaseHandler.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        checkData();
    }

    public void addBook(ActionEvent actionEvent) {
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookGenre = genre.getText();

        if (bookAuthor.isEmpty() || bookGenre.isEmpty() || bookTitle.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }
        String qu = "INSERT INTO BOOK(bookTitle, bookAuthor, bookGenre, isRead, isLent) VALUES (" +
                "'" + bookTitle + "'," +
                "'" + bookAuthor + "'," +
                "'" + bookGenre + "'," +
                "" + "false" + "," +
                "" + "false" + "" +
                ")";
        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Book has been added.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("An error has occurred - could not add book.");
            alert.showAndWait();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String qu = "SELECT bookTitle FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("bookTitle");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
