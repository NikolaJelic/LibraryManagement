package libraryManagement.ui.listAuthor;

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
import libraryManagement.database.DatabaseHandler;
import libraryManagement.ui.book.BookController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListAuthorController implements Initializable {

    ObservableList<Author> list = FXCollections.observableArrayList();

    public AnchorPane rootPane;
    public TableView<Author> tableView;
    public TableColumn<Author, String> idCol;
    public TableColumn<Author, String> titleCol;
    public TableColumn<Author, String> genreCol;
    public TableColumn<Author, Boolean> isReadCol;
    public TableColumn<Author, Boolean> isLentCol;
    private String selectedAuthor = AuthorList.selectedAuthor.getAuthor();
    public static Author selectedBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String author = rs.getString("bookAuthor");
                if (author.equals(selectedAuthor)) {

                    String id = rs.getString("bookId");
                    String title = rs.getString("bookTitle");
                    String genre = rs.getString("bookGenre");
                    Boolean isRead = rs.getBoolean("isRead");
                    Boolean isLent = rs.getBoolean("isLent");
                    list.add(new Author(id, title, genre, isRead, isLent));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListAuthorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        isReadCol.setCellValueFactory(new PropertyValueFactory<>("isRead"));
        isLentCol.setCellValueFactory(new PropertyValueFactory<>("isLent"));

    }


    public void selectBook(MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            selectedBook = tableView.getSelectionModel().getSelectedItem();
            try {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/book.fxml"));
                AnchorPane page = loader.load();
                BookController bookController = loader.getController();
                System.out.println(selectedBook.getTitle() + "is the book before setting it");
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


    public static class Author {
        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getGenre() {
            return genre.get();
        }

        public boolean isIsRead() {
            return isRead.get();
        }

        public boolean isIsLent() {
            return isLent.get();
        }

        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty genre;
        private final SimpleBooleanProperty isRead;
        private final SimpleBooleanProperty isLent;

        Author(String id, String title, String genre, Boolean isRead, Boolean isLent) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.genre = new SimpleStringProperty(genre);
            this.isRead = new SimpleBooleanProperty(isRead);
            this.isLent = new SimpleBooleanProperty(isLent);
        }

    }
}
