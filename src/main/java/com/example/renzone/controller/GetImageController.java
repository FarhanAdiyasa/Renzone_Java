package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class GetImageController implements Initializable {
    public Image ktp;
    @FXML protected Button btnClear;
    @FXML  Button btnOk;
    @FXML protected ImageView ftoKtp;
    @FXML protected SVGPath svg;
    private FileInputStream fis;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getImg();
        if(ktp != null)
        {
            svg.setVisible(false);
            ftoKtp.setOnMouseClicked(null);
        }
        ftoKtp.setOnDragOver(this::onDragOver);
        ftoKtp.setOnDragDropped(this::onDragDropped);
    }
    private void onDragOver(DragEvent event) {
        // Check if the dragged content has files
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    private void onDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;

        if (dragboard.hasFiles()) {
            File file = dragboard.getFiles().get(0); // Assuming only one file is dropped

            // Load and display the dropped image
            Image image = new Image(file.toURI().toString());
            ftoKtp.setImage(image);

            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
    @FXML
    protected void pickImg(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File Gambar", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Semua File", "*.*"));
        fileChooser.setTitle("Pilih Gambar");

        // Get the stage from the existing fotoProf ImageView
        Stage stage = (Stage) ftoKtp.getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);
        file = selectedFile;
        if (selectedFile != null) {
            String filePath = selectedFile.toURI().toString();
            Image image = new Image(filePath);
            ftoKtp.setImage(image);
            svg.setVisible(false);
            ftoKtp.setOnMouseClicked(null);
        }
    }
    @FXML
    protected void clear() {
        if (ftoKtp != null) {
            ftoKtp.setImage(null);
        }
        if (svg != null) {
            svg.setVisible(true);
        }
        if (ftoKtp != null) {
            ftoKtp.setOnMouseClicked(this::pickImg);
        }
    }
    @FXML
    protected void ok() throws IOException {
        ktp = ftoKtp.getImage();
        Stage stages = (Stage) btnOk.getScene().getWindow();
        stages.close();
        dltToimg();
        insertToimg(file);
        stages.close();
    }
    protected void insertToimg(File file)
    {
        try {
            Connection connection = new Connection();
            // Prepare the SQL query to insert data from ms_member to bntu_member
            String insertQuery = "INSERT INTO temp_Img VALUES (?)";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(insertQuery);

            FileInputStream fis = new FileInputStream(file);
            preparedStatement.setBinaryStream(1, fis, file.length());

            // Execute the insert query
            preparedStatement.executeUpdate();

            // Close the FileInputStream
            fis.close();

            System.out.println("Data gambar berhasil disimpan ke database.");
        } catch (Exception e) {
            System.out.println("Error inserting data from ms_member to bntu_member: " + e);
        }
    }
    protected void dltToimg()
    {
        try {
            Connection connection = new Connection();
            // Prepare the SQL query to insert data from ms_member to bntu_member
            String insertQuery = "DELETE FROM temp_Img";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(insertQuery);
            // Execute the insert query
            preparedStatement.executeUpdate();
            System.out.println("Data gambar berhasil didelete dari database.");
        } catch (Exception e) {
            System.out.println("Error Deleting Data " + e);
        }

    }
    protected void getImg() {
        try {
            Connection connection = new Connection();
            String query = "SELECT ktp FROM temp_Img"; // Sesuaikan dengan struktur tabel Anda
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Ambil data gambar dari hasil query sebagai array byte
                byte[] imageData = resultSet.getBytes("ktp");

                // Konversi array byte menjadi objek Image di JavaFX
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = new Image(bis);
                ktp = image;
                ftoKtp.setImage(ktp);
            } else {
                // Jika data gambar tidak ditemukan
                System.out.println("Data gambar tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat mengambil gambar dari database: " + e);
        }
    }
}
