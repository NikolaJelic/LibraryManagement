package libraryManagement.ui.listGenre;

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

public class ListGenre implements Initializable {

    ObservableList<Genre> list = FXCollections.observableArrayList();

    public AnchorPane rootPane;
    public TableView<Genre> tableView;
    public TableColumn<Genre, String> idCol;
    public TableColumn<Genre, String> titleCol;
    public TableColumn<Genre, String> authorCol;
    public TableColumn<Genre, Boolean> isReadCol;
    public TableColumn<Genre, Boolean> isLentCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String selectedGenre = ListGenreController.selectedGenre.getGenre();


    public static Genre selectedBook;


    private void loadData() throws SQLException {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = handler.execQuery(qu);
        System.out.println("find " + selectedGenre);
        try {

            while (rs.next()) {
                String genre = rs.getString("bookGenre");
                if (genre.equals(selectedGenre)) {
                    String id = rs.getString("bookId");
                    String title = rs.getString("bookTitle");
                    String author = rs.getString("bookAuthor");
                    Boolean isRead = rs.getBoolean("isRead");
                    Boolean isLent = rs.getBoolean("isLent");
                    list.add(new Genre(id, title, author, isRead, isLent));

                }
                System.out.println(selectedGenre);


            }
        } catch (SQLException ex) {
            Logger.getLogger(ListGenreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
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


    public static class Genre {

        public String getTitle() {
            return title.get();
        }


        public String getId() {
            return id.get();
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


        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleBooleanProperty isRead;
        private final SimpleBooleanProperty isLent;

        Genre(String id, String title, String author, Boolean isRead, Boolean isLent) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.isRead = new SimpleBooleanProperty(isRead);
            this.isLent = new SimpleBooleanProperty(isLent);
        }


    }
}

