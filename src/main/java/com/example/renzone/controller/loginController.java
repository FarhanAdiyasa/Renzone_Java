package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import io.github.gleidsonmt.gncontrols.controls.GNIconButton;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class loginController {
    @FXML
    private Label emailValid;
    @FXML
    private Label loginValid;
    @FXML
    private Label showPass;
    @FXML
    private TextField showPasse;
    @FXML
    private TextField txtemail;
    @FXML
    private GNIconButton viewBtn;
    @FXML
    private PasswordField txtpswd;
    @FXML
    private TextField viewpswd;
    private boolean passwordVisible = false;
    String email, pswd;
    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML
    protected void validateEmail() {
/*        email = txtemail.getText();

        // Pattern untuk validasi email
        String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

        if (email.matches(emailPattern) || txtemail.getText().equals("")) {
            emailValid.setText("");
            emailValid.getStyleClass().removeAll("invalid"); // Menghapus kelas gaya "invalid" jika ada
            emailValid.getStyleClass().add("valid"); // Menambahkan kelas gaya "valid"
        } else if ((!email.matches(emailPattern)) && (!txtemail.getText().equals(""))) {
            emailValid.setText("Email isn't valid");
            emailValid.getStyleClass().removeAll("valid"); // Menghapus kelas gaya "valid" jika ada
            emailValid.getStyleClass().add("invalid"); // Menambahkan kelas gaya "invalid"
        }*/
    }
    @FXML
    void passwordFieldKeyTyped(KeyEvent event) {
        showPass.textProperty().bind(Bindings.concat(txtpswd.getText()));
    }

    @FXML
    public void togglePasswordVisibility() {
        if (!showPasse.isVisible()) {
            showPasse.setVisible(true);
            txtpswd.setVisible(false);
            showPass.textProperty().bind(Bindings.concat(txtpswd.getText()));
            viewBtn.setIcon(Icons.VIEWER_OFF);
            showPasse.setText(showPass.getText());
            showPasse.textProperty().addListener((observable, oldValue, newValue) -> {
                txtpswd.setText(newValue);
            });
        } else {
            txtpswd.setVisible(true);
            showPasse.setVisible(false);
            viewBtn.setIcon(Icons.VIEWER);
        }
    }

    @FXML
    protected void btnMskClick(ActionEvent event) throws IOException {
        email = txtemail.getText();
        pswd = txtpswd.getText().trim(); // Menghapus spasi yang tidak diinginkan
        java.sql.Connection connection = Connection.getConnect();
        try {
            String query = "SELECT * FROM ms_karyawan WHERE nama_pengguna = ? AND kata_sandi = ? AND isDeleted is null;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pswd);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if(resultSet.getString("id_karyawan").equals("OWNER"))
                {
                    switchToDashboardOwn(event);
                }
                else {
                    switchToDashboard(event, resultSet.getString("nama_karyawan"));
                    executeStoredProcedure( resultSet.getString("id_karyawan"));
                }

            } else {
                // Email atau password tidak sesuai
                loginValid.setText("Login Gagal, Email atau password salah");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void switchToDashboard(ActionEvent event, String name) throws IOException {
        // Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardUtama.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Inisialisasi stage sebelum penggunaannya
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(scene);
        StageManager.setPrimaryStage(stage);
//        dsbUtamaController dashboardController = loader.getController();
        stage.show();
      /*dashboardController.refreshDashboard(event);*/
    }
    @FXML
    private void switchToDashboardOwn(ActionEvent event) throws IOException {
        // Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Owner/dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Inisialisasi stage sebelum penggunaannya
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(scene);
        StageManager.setPrimaryStage(stage);
/*        dsbCustController dashboardController = loader.getController();
        dashboardController.setName(name);*/

        stage.show();
        autoUpdateMemberStatus();
    }

    private void autoUpdateMemberStatus() {
        java.sql.Connection connection = Connection.getConnect();
        try {
            // Query untuk melakukan pembaruan pada data member yang sudah expired
            String query = "UPDATE ms_member\n" +
                    "SET status_anggota = 'Inactive'\n" +
                    "WHERE GETDATE() > DATEADD(month, 1, tgl_daftar)\n";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void executeStoredProcedure(String id) {
        java.sql.Connection connection = Connection.getConnect();
        try {
            // Query untuk menghapus data dengan ID tertentu dari tabel login
            String deleteQuery = "DELETE FROM login";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

            // Eksekusi query penghapusan
            int rowsDeleted = deleteStatement.executeUpdate();

            // Jika ada data yang dihapus, maka lanjutkan dengan mengambil data dari tabel ms_karyawan

                String insertQuery = "INSERT INTO login (id_karyawan, foto_profil, nama_karyawan)\n" +
                        "SELECT id_karyawan, foto_profil, nama_karyawan\n" +
                        "FROM ms_karyawan\n" +
                        "WHERE id_karyawan = ?";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, id);

                // Eksekusi query pengisian data
                insertStatement.executeUpdate();
                insertStatement.close();


            deleteStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
