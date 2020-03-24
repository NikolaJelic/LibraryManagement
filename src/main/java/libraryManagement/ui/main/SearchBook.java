package libraryManagement.ui.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import libraryManagement.ui.book.BookController;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SearchBook implements Initializable {
    private static final String DB_URL = "jdbc:sqlite:./Biblioteka/database.db";
    public static Book selectedBook;
    private static String searchedBook = MainController.searchBook;
    public AnchorPane rootPane;
    public TableView<Book> tableView;
    public TableColumn<Book, String> idCol;
    public TableColumn<Book, String> titleCol;
    public TableColumn<Book, String> authorCol;
    public TableColumn<Book, String> genreCol;
    public TableColumn<Book, Boolean> readCol;
    public TableColumn<Book, Boolean> lentCol;
    ObservableList<Book> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
            System.out.println(searchedBook);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {

        String sql = "SELECT * FROM BOOK";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("bookTitle").toLowerCase();
                if (title.equals(searchedBook.toLowerCase())) {
                    String id = rs.getString("bookID");
                    String author = rs.getString("bookAuthor");
                    String genre = rs.getString("bookGenre");
                    Boolean isRead = rs.getBoolean("isRead");
                    Boolean isLent = rs.getBoolean("isLent");
                    list.add(new Book(id, title, author, genre, isRead, isLent));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.getItems().setAll(list);
    }

    public void selectBook(MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
//opens the selected book
            selectedBook = tableView.getSelectionModel().getSelectedItem();
            try {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/book.fxml"));
                AnchorPane page = loader.load();
                BookController bookController = loader.getController();
                bookController.setSelectedBookId(selectedBook.getId());
                Stage stage = new Stage();
                stage.setScene(new Scene(page));
                stage.setTitle(selectedBook.getTitle());
                stage.setResizable(false);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();
        }
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        readCol.setCellValueFactory(new PropertyValueFactory<>("isRead"));
        lentCol.setCellValueFactory(new PropertyValueFactory<>("isLent"));


    }


    public static class Book {
        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty genre;
        private final SimpleStringProperty author;
        private final SimpleBooleanProperty isRead;
        private final SimpleBooleanProperty isLent;


        Book(String id, String title, String author, String genre, Boolean isRead, Boolean isLent) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.genre = new SimpleStringProperty(genre);
            this.author = new SimpleStringProperty(author);
            this.isRead = new SimpleBooleanProperty(isRead);
            this.isLent = new SimpleBooleanProperty(isLent);
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getGenre() {
            return genre.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public boolean isIsRead() {
            return isRead.get();
        }

        public boolean isIsLent() {
            return isLent.get();
        }

    }
}
