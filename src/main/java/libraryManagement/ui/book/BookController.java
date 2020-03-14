package libraryManagement.ui.book;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import libraryManagement.database.DatabaseHandler;
import libraryManagement.ui.listGenre.ListGenreController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookController implements Initializable {
    public Text bookID;
    public ImageView coverImage;
    public Button selectImageButton;
    DatabaseHandler handler = DatabaseHandler.getInstance();
    private static final String DB_URL = "jdbc:sqlite:database.db";
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
    String coverPath;
    Image cover;

    String description;
    private String selectedBookId;
    String lender;

    public void setSelectedBookId(String selectedBookId) {
        this.selectedBookId = selectedBookId;

        loadData();
        bookTitle.setText(title);

        bookGenre.setText(genre);
        bookAuthor.setText(author);
        bookID.setText(selectedBookId);
        System.out.println(coverPath + "Path ");
        try {
            if (coverPath != null) {
                cover = new Image(new FileInputStream(coverPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        coverImage.setImage(cover);

        //   String lender = " ";
        if (isLent) {
            isLentText.setText(lender);
        } else {
            isLentText.setText("Ne");
        }
        if (isRead) {
            isReadText.setText("Da");
        } else {
            isReadText.setText("Ne");
        }

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
                isReadText.setText("Ne");
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
                isReadText.setText("Da");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void isLentChange(ActionEvent actionEvent) throws SQLException {
        if (isLent) {
            isLent = false;
            String sql = "update BOOK set isLent=? where bookId=?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setBoolean(1, isLent);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();
                isLentText.setText("Ne");
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!isLent) {
            isLent = true;
            String sql = "update BOOK set isLent=? where bookId=?";
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/lender.fxml"));
                AnchorPane page = loader.load();
                Lender lender = loader.getController();
                lender.setParentBook(selectedBookId);
                Stage stage = new Stage();
                stage.setScene(new Scene(page));
                stage.setTitle("Kome je knjiga pozajmljena?");
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setBoolean(1, isLent);
                statement.setInt(2, Integer.parseInt(id));
                statement.executeUpdate();
                loadData();
                isLentText.setText(lender);
                statement.close();
                conn.close();
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
                    lender = rs.getString("bookLender");
                    coverPath = rs.getString("imagePath");
                    System.out.println(coverPath + "Path ");

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
            alert.setContentText("Book " + title + " has been deleted.");
            alert.showAndWait();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectImage(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = chooser.showOpenDialog(rootPane.getScene().getWindow());

        if (selectedFile != null) {
            System.out.println("Image selected");
            //adding selected image path to the database so that the image can be found
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement("update BOOK set imagePath=? where bookId=?")) {

                System.out.println(selectedFile.getAbsolutePath());
                Path slikePath = DatabaseHandler.getInstance().getSlikePath();
                System.out.println(slikePath);
                selectedFile.renameTo(new File(slikePath + "//" + id));
                String pathjFordatabase = slikePath + "//" + id;
                System.out.println(pathjFordatabase + "pfdb");
                pstmt.setString(1, pathjFordatabase);
                pstmt.setInt(2, Integer.parseInt(id));
                pstmt.executeUpdate();
                System.out.println(coverPath + "Path ");
                cover = new Image(new FileInputStream(pathjFordatabase));
                coverImage.setImage(cover);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
