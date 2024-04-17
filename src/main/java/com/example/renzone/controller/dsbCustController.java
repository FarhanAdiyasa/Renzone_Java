package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class dsbCustController implements Initializable {
    @FXML
    private Image playPsImg;
    @FXML
    private Button trPs;
    @FXML
    private Button Transaksi;
    @FXML
    private Button trBorrow;
    @FXML
    private Button trsPengembalian;
    @FXML
    private Button bckdsh;
    @FXML
    private AnchorPane btnHolder;
    @FXML
    private TextField profileTip;
    @FXML
    private TextField loutTip;
    @FXML Label name;
    @FXML
    public void setName(String nama)
    {
        name.setText("Welcome " +nama+ "!!");
    }
    @FXML
    protected void enterPtip()
    {
        profileTip.setVisible(true);
    }
    @FXML
    protected void exitPtip()
    {
        profileTip.setVisible(false);
    }
    @FXML
    protected void enterLtip()
    {
        loutTip.setVisible(true);
    }
    @FXML
    protected void exitLtip()
    {
        loutTip.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageFromDatabase(fprofil);

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

                    }
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Renzone");
                    // Menampilkan stage baru dalam mode fullscreen
                    stage.setFullScreen(true);
                    StageManager.setPrimaryStage(stage);
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
        bckdsh.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardUtama.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Renzone");
            // Menampilkan stage baru dalam mode fullscreen
            stage.setFullScreen(true);
            StageManager.setPrimaryStage(stage);
            // Menampilkan stage baru
            stage.show();
            // Menutup stage saat ini (jika diperlukan)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });

    }
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
