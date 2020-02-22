package libraryManagement.ui.listGenre;

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

public class ListGenreController implements Initializable {

    ObservableList<Genre> list = FXCollections.observableArrayList();

    public AnchorPane rootPane;
    public TableView<Genre> tableView;
    public TableColumn<Genre, String> genreCol;

    // tried this but it gives an error
   static Genre selectedGenre;


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
        String qu = "SELECT DISTINCT bookGenre FROM BOOK";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String genre = rs.getString("bookGenre");
                if (!list.contains(genre)) {
                    list.add(new Genre(genre));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListGenreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));


    }



    public void selectGenre(MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            selectedGenre = tableView.getSelectionModel().getSelectedItem();


            try {
                Parent parent = FXMLLoader.load(getClass().getResource("/list_genre.fxml"));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle(selectedGenre.getGenre());
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


    public static class Genre {
        public String getGenre() {
            return genre.get();
        }


        private final SimpleStringProperty genre;


        Genre(String genre) {
            this.genre = new SimpleStringProperty(genre);

        }

    }

}
