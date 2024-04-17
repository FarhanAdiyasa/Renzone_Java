package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class uaOwner implements Initializable {
    @FXML
    TextField txtUsnUpd;
    @FXML TextField txtOldPw;
    @FXML TextField txtPwUpd;
    @FXML TextField txtTAUpd;
    @FXML Pane vldOldPw;
    @FXML
    Pane vldPwUpd;
    @FXML
    Pane vldUsnUpd;
    @FXML
    Pane vldLength;
    @FXML
    Button btnCancel;
    @FXML Button btnSave;
    String idmember, namamember;
    @FXML
    private void cekOldPw(Event event) {
        String enteredPassword = txtOldPw.getText();
        String storedPassword = getPasswordFromDatabase("OWNER");

        if (!enteredPassword.equals(storedPassword)) {
            vldOldPw.setVisible(true);
        } else {
            vldOldPw.setVisible(false);
        }
    }
    public String getPasswordFromDatabase(String id) {
        String password = null;

        try {
            Connection connection = new Connection(); // Assuming Connection class is implemented correctly
            String query = "SELECT kata_sandi FROM ms_karyawan WHERE id_karyawan = ? AND isDeleted is null";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is a result and retrieve the password
            if (resultSet.next()) {
                password = resultSet.getString("kata_sandi");
            }

            // Close the resources properly
            resultSet.close();
            preparedStatement.close();
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }
    public String getUsnFromDatabase() {
        String password = null;

        try {
            Connection connection = new Connection(); // Assuming Connection class is implemented correctly
            String query = "SELECT nama_pengguna FROM ms_karyawan WHERE id_karyawan = 'OWNER' AND isDeleted is null";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is a result and retrieve the password
            if (resultSet.next()) {
                password = resultSet.getString("nama_pengguna");
            }

            // Close the resources properly
            resultSet.close();
            preparedStatement.close();
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        old = password;
        return password;
    }

    @FXML
    private void cekPwUpd(Event event) {
        String enteredPassword = txtTAUpd.getText();
        String storedPassword = txtPwUpd.getText();
        if (!enteredPassword.equals(storedPassword)) {
            vldPwUpd.setVisible(true);
        } else {
            vldPwUpd.setVisible(false);
        }
    }
    String old = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtUsnUpd.setText(getUsnFromDatabase());
        txtUsnUpd.textProperty().addListener((obs, oldValue, newValue) -> {

            if(newValue.length()<5)
            {
                vldLength.setVisible(true);
            }
            else
            {
                vldLength.setVisible(false);
            }
            isUsernameExists(newValue,old);
        });
        txtPwUpd.textProperty().addListener((obs, oldValue, newValue) -> {

            if(newValue.length()<5)
            {
                vldLength.setVisible(true);
            }
            else
            {
                vldLength.setVisible(false);
            }
        });
    }
    public static void vibrate(TextField anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        idmember = txtUsnUpd.getText();
        namamember = txtPwUpd.getText();

        if (vldOldPw.isVisible() || vldPwUpd.isVisible() || vldLength.isVisible() || txtTAUpd.getText().isEmpty()) {
            vibrate(txtOldPw);
            vibrate(txtPwUpd);
            vibrate(txtTAUpd);
            return;
        }

        Connection con = new Connection();
        try {
            String insertmember = "UPDATE ms_karyawan SET nama_pengguna = ?, kata_sandi = ? WHERE id_karyawan = 'OWNER'";
            PreparedStatement insertmemberStatement = con.conn.prepareStatement(insertmember);
            insertmemberStatement.setString(1, idmember);
            insertmemberStatement.setString(2, namamember);

            insertmemberStatement.executeUpdate();
            Stage stages = (Stage) btnSave.getScene().getWindow();
            stages.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Owner/dashboard.fxml"));
            Parent parent = loader.load();

            // Get the primary stage and set the refreshed parent form
            Stage primaryStage = StageManager.getPrimaryStage();
            primaryStage.setScene(new Scene(parent));
            primaryStage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions or show an error message
            System.out.println("Failed to load and refresh parent form: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void btnCancelClick(ActionEvent event){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        // Menutup stage (form)
        stage.close();
    }
    public void isUsernameExists(String username, String old) {
        boolean exists = false;
        try {
            Connection connection = new Connection(); // Sesuaikan dengan cara Anda mendapatkan koneksi ke database
            String query = "SELECT COUNT(*) AS count FROM ms_karyawan WHERE nama_pengguna = ? AND nama_pengguna<> ? AND isDeleted is null;";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, old);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                exists = count > 0;
            }

            // Close the resources properly
            resultSet.close();
            preparedStatement.close();
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
            if (exists) {
                vldUsnUpd.setVisible(true);
            } else {
                vldUsnUpd.setVisible(false);
            }
    }
}

