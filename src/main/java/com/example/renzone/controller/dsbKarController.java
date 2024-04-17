package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Game;
import com.example.renzone.models.curPlay;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class dsbKarController implements Initializable {
    @FXML
    private AnchorPane ttlMm;
    @FXML
    private Button btnDtl1;
    @FXML
    private Button btnDtl2;
    @FXML
    private Button btnDtl3;
    @FXML
    private Button dshbrdP;
    @FXML
    TableView<curPlay> curPlayTableView;
    @FXML
    TableColumn<curPlay, String> kodeObatCol;
    @FXML
    TableColumn<curPlay, String> namaObatCol;
    @FXML
    TableColumn<curPlay, String> merkCol;
    @FXML
    TableColumn<curPlay, String> kemasanCol;
    @FXML
    TableColumn<curPlay, String> detCol;
    @FXML
    private AnchorPane btnHolder;
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    curPlay cPlay = null ;

    @FXML
    protected ImageView imageView;

    // Path ke file GIF
    private Image gifImage;
    private int currentFrame = 0;

    ObservableList<curPlay> StudentList = FXCollections.observableArrayList();

    @FXML
    protected void onEtrBar() {
        // Membuat transisi fade in dengan durasi 0.3 detik
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.3), btnDtl1);
        fadeInTransition.setFromValue(0); // Mulai dengan keadaan transparan (alpha = 0)
        fadeInTransition.setToValue(1); // Akhiri dengan keadaan terlihat (alpha = 1)
        fadeInTransition.play(); // Memulai transisi fade in

        btnDtl1.setVisible(true);
    }



    @FXML
    protected void onExtBar() {
        // Membuat transisi fade out dengan durasi 0.3 detik
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.3), btnDtl1);
        fadeOutTransition.setFromValue(1); // Mulai dengan keadaan terlihat (alpha = 1)
        fadeOutTransition.setToValue(0); // Akhiri dengan keadaan transparan (alpha = 0)
        fadeOutTransition.setOnFinished(event -> btnDtl1.setVisible(false)); // Mengatur tombol tidak terlihat setelah transisi selesai
        fadeOutTransition.play(); // Memulai transisi fade out
    }
    @FXML
    protected void onEtrBar2() {
        // Membuat transisi fade in dengan durasi 0.3 detik
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.3), btnDtl2);
        fadeInTransition.setFromValue(0); // Mulai dengan keadaan transparan (alpha = 0)
        fadeInTransition.setToValue(1); // Akhiri dengan keadaan terlihat (alpha = 1)
        fadeInTransition.play(); // Memulai transisi fade in

        btnDtl2.setVisible(true);
    }

    @FXML
    protected void onExtBar2() {
        // Membuat transisi fade out dengan durasi 0.3 detik
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.3), btnDtl2);
        fadeOutTransition.setFromValue(1); // Mulai dengan keadaan terlihat (alpha = 1)
        fadeOutTransition.setToValue(0); // Akhiri dengan keadaan transparan (alpha = 0)
        fadeOutTransition.setOnFinished(event -> btnDtl2.setVisible(false)); // Mengatur tombol tidak terlihat setelah transisi selesai
        fadeOutTransition.play(); // Memulai transisi fade out
    }
    @FXML
    protected void onEtrBar3() {
        // Membuat transisi fade in dengan durasi 0.3 detik
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.3), btnDtl3);
        fadeInTransition.setFromValue(0); // Mulai dengan keadaan transparan (alpha = 0)
        fadeInTransition.setToValue(1); // Akhiri dengan keadaan terlihat (alpha = 1)
        fadeInTransition.play(); // Memulai transisi fade in

        btnDtl3.setVisible(true);
    }

    @FXML
    protected void onExtBar3() {
        // Membuat transisi fade out dengan durasi 0.3 detik
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.3), btnDtl3);
        fadeOutTransition.setFromValue(1); // Mulai dengan keadaan terlihat (alpha = 1)
        fadeOutTransition.setToValue(0); // Akhiri dengan keadaan transparan (alpha = 0)
        fadeOutTransition.setOnFinished(event -> btnDtl3.setVisible(false)); // Mengatur tombol tidak terlihat setelah transisi selesai
        fadeOutTransition.play(); // Memulai transisi fade out
    }

/*    private void updateFrame() {
        int frameCount = (int) (gifImage.getWidth() / imageView.getFitWidth());
        currentFrame = (currentFrame + 1) % frameCount;
        imageView.setImage(gifImage);
        imageView.setViewport(new javafx.geometry.Rectangle2D(currentFrame * imageView.getFitWidth(), 0,
                imageView.getFitWidth(), imageView.getFitHeight()));
    }*/



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dshbrdP.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        setImageFromDatabase(fprofil);
        // Membuat ImageView untuk menampilkan GIF
        // Mendapatkan daftar tombol dari btnHolder
        List<Button> buttons = btnHolder.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .collect(Collectors.toList());

        // Mengatur aksi untuk setiap tombol
        for (Button button : buttons) {
            button.setOnAction(event -> {
                FXMLLoader loader ;
                // Mendapatkan nama file FXML yang terkait dengan tombol
                String fxmlFile = button.getId() + ".fxml";

                try {
                    if(button.getId().equals("login"))
                    {
                        loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/" + fxmlFile));
                    }
                    else
                    {
                        loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/" + fxmlFile));

                    }Parent root = loader.load();

                    // Membuat stage baru dan mengatur scene dengan root dari file FXML
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Renzone");
                    StageManager.setPrimaryStage(stage);
                    // Menampilkan stage baru dalam mode fullscreen
                    stage.setFullScreen(true);

                    // Menampilkan stage baru
                    stage.show();

                    // Menutup stage saat ini (jika diperlukan)
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Menangani kesalahan saat memuat file FXML
                }
            });
        }
    }
    @FXML
    protected void rfSbClr()
    {
        btnHolder.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setStyle("-fx-background-color:rgb(180, 128, 42);");
            }
        });
    }
   /* private void loadDate() {
        refreshTable();
        kodeObatCol.setCellValueFactory(new PropertyValueFactory<>("kodeObat"));
        namaObatCol.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        merkCol.setCellValueFactory(new PropertyValueFactory<>("merk"));
        kemasanCol.setCellValueFactory(new PropertyValueFactory<>("kemasan"));

        curPlayTableView.setItems(StudentList);
    }
    public void refreshTable() {
        try {
            StudentList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM ms_game";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                StudentList.add(new curPlay(
                    connection.result.getString("id_game"),
                    connection.result.getString("nama_game"),
                    connection.result.getString("deskripsi_game"),
                    connection.result.getString("tanggal_rilis")));
                curPlayTableView.setItems(StudentList);
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }*/
    @FXML
    private Circle fprofil;
    private void setImageFromDatabase(Circle circle) {
        Connection connection = new Connection();
        try {
            // Query untuk mendapatkan gambar dari database
            String query = "SELECT * FROM login";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Mendapatkan gambar dari hasil query
                InputStream inputStream = resultSet.getBinaryStream("foto_profil");
                Image image = new Image(inputStream);

                // Mengatur gambar pada lingkaran
                circle.setFill(new ImagePattern(image));
                Tooltip tooltip = new Tooltip(resultSet.getString("nama_karyawan"));
                circle.setOnMouseEntered(event -> {
                    tooltip.show(circle, event.getScreenX(), event.getScreenY());
                });
                circle.setOnMouseExited(event -> {
                    tooltip.hide();
                });
            }


            // Menutup resultSet, preparedStatement, dan connection
            resultSet.close();
            preparedStatement.close();
            connection.close();
            if(circle.getFill() == null)
            {
                circle.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

