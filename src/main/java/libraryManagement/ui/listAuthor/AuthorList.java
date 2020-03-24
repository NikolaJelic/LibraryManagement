package libraryManagement.ui.listAuthor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libraryManagement.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorList implements Initializable {
    //Lists all the unique authors and if one is selected, opens a new window that lists all the books written by that author

    ObservableList<Author> list = FXCollections.observableArrayList();


    public AnchorPane rootPane;
    public TableView<Author> tableView;
    public TableColumn<Object, Object> authorCol;
    static Author selectedAuthor;

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
        String qu = "SELECT DISTINCT bookAuthor FROM BOOK";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String author = rs.getString("bookAuthor");
                if (!list.contains(author)) {
                    list.add(new Author(author));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));


    }


    public void selectAuthor(MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            selectedAuthor = tableView.getSelectionModel().getSelectedItem();


            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/list_author.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle(selectedAuthor.getAuthor());
                stage.setScene(new Scene(parent));
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
        public String getAuthor() {
            return author.get();
        }


        private final SimpleStringProperty author;


        Author(String author) {
            this.author = new SimpleStringProperty(author);

        }

    }

}
