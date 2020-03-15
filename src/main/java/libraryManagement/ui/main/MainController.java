package libraryManagement.ui.main;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libraryManagement.database.DatabaseHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    public TextField searchBox;
    public Text bookIDDisplay;
    public Text authorDisplay;
    public Text genreDisplay;
    public Button pdfButton;
    public ImageView image;
    public AnchorPane rootPane;
    Connection conn = null;
    Image book;

    DatabaseHandler handler = DatabaseHandler.getInstance();

    public MainController() throws SQLException {
    }


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
        {
            book = new Image("/book.png");
        }
        image.setImage(book);
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
            conn = DriverManager.getConnection("jdbc:sqlite:./Biblioteka/database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BOOK WHERE bookTitle = ? COLLATE NOCASE");
        stmt.setString(1, searchBook);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String title = rs.getString("bookTitle").toLowerCase();
            System.out.println(rs.getString("bookTitle").toLowerCase());
            if (title.equals(searchBook)) {
                String id = rs.getString("bookID");
                String genre = rs.getString("bookGenre");
                String author = rs.getString("bookAuthor");

                bookIDDisplay.setText(id);
                genreDisplay.setText(genre);
                authorDisplay.setText(author);
                stmt.close();
                conn.close();
            }
        } else if (!rs.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Book not found.");
            alert.showAndWait();

            bookIDDisplay.setText("ID Knjige");
            genreDisplay.setText("Zanr");
            authorDisplay.setText("Autor");
        }


    }

    public void convertToPDF(ActionEvent actionEvent) throws FileNotFoundException, DocumentException, SQLException {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = handler.execQuery(qu);


        Document biblioteka = new Document();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph title = new Paragraph("Lista knjiga");
        Paragraph line = new Paragraph("-------------------");
        title.setAlignment(Element.ALIGN_CENTER);
        line.setAlignment(Element.ALIGN_CENTER);


        PdfWriter.getInstance(biblioteka, new FileOutputStream("./Biblioteka/Biblioteka.pdf"));
        biblioteka.open();
        biblioteka.add(title);
        biblioteka.add(line);
        PdfPTable knjigeTabela = new PdfPTable(6);
        PdfPCell table_cell;
        table_cell = new PdfPCell(new Phrase("ID"));
        knjigeTabela.addCell(table_cell);
        table_cell = new PdfPCell(new Phrase("Naslov"));
        knjigeTabela.addCell(table_cell);
        table_cell = new PdfPCell(new Phrase("Autor"));
        knjigeTabela.addCell(table_cell);
        table_cell = new PdfPCell(new Phrase("Žanr"));
        knjigeTabela.addCell(table_cell);
        table_cell = new PdfPCell(new Phrase("Pozajmljeno"));
        knjigeTabela.addCell(table_cell);
        table_cell = new PdfPCell(new Phrase("Procitano"));
        knjigeTabela.addCell(table_cell);

        while (rs.next()) {
            table_cell.setCellEvent(new MyBorder());
            String bookId = rs.getString("bookId");
            table_cell = new PdfPCell(new Phrase(bookId));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
            String bookTitle = rs.getString("bookTitle");
            table_cell = new PdfPCell(new Phrase(bookTitle));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
            String bookAuthor = rs.getString("bookAuthor");
            table_cell = new PdfPCell(new Phrase(bookAuthor));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
            String bookGenre = rs.getString("bookGenre");
            table_cell = new PdfPCell(new Phrase(bookGenre));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
            String isLent = rs.getString("isLent");
            System.out.println(isLent);
            if (isLent.equals("1")) {
                isLent = rs.getString("bookLender");
            } else {
                isLent = "ne";
            }
            table_cell = new PdfPCell(new Phrase(isLent));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
            String isRead = rs.getString("isRead");
            if (isRead.equals("1")) {
                isRead = "da";
            } else {
                isRead = "ne";
            }
            table_cell = new PdfPCell(new Phrase(isRead));
            table_cell.setCellEvent(new MyBorder());

            knjigeTabela.addCell(table_cell);
        }
        /* Attach report table to PDF */
        biblioteka.add(knjigeTabela);
        biblioteka.close();

        /* Close all DB related objects */
        rs.close();

        conn.close();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Pdf je kreiran u folderu \"Biblioteka\"");
        alert.showAndWait();
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public class MyBorder implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            float x1 = position.getLeft() + 1;
            float x2 = position.getRight() - 1;
            float y1 = position.getTop() - 1;
            float y2 = position.getBottom() + 1;
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
            canvas.stroke();
        }
    }
}
