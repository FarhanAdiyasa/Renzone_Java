package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class insertMController implements Initializable {
    @FXML
    TextField txtid;
    @FXML TextField txtnama;
    @FXML TextField txtalamat;
    @FXML TextField txtnotelepon;
    @FXML TextField txttanggal;
    @FXML
    Label lbValidasi;
    @FXML
    private ChoiceBox cmbStatus;
    @FXML
    Button btnCancel;
    @FXML Button btnSave;
    String idmember, namamember, alamatmember, noteleponmember, tanggalmember, statusmember;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtid.setText(generateAutoId());
        cmbStatus.setValue("Active");
        cmbStatus.setDisable(true);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txttanggal.setText(currentDateTime.format(formatter));
        txtnama.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Memastikan hanya huruf yang dapat diinputkan
                if (!newValue.matches("[a-zA-Z\\s]*")) {
                    txtnama.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
                }
            }
        });
        txtnotelepon.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtnotelepon.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 13) {
                    txtnotelepon.setText(newValue.substring(0, 13));
                }
            }
        });
    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_member FROM ms_member ORDER BY id_member DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_member");
            }

            // Generate ID otomatis berdasarkan ID game terakhir
            autoId = generateNextId(lastId);

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autoId;
    }
    private static String generateNextId(String lastId) {
        if (lastId == null) {
            // Jika belum ada data game dalam tabel, set ID otomatis pertama
            return "MBR001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("MBR%03d", nextNumber);
        }
    }
    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        idmember = txtid.getText();
        namamember = txtnama.getText();
        alamatmember = txtalamat.getText();
        noteleponmember = txtnotelepon.getText();
        tanggalmember = txttanggal.getText();

        statusmember = cmbStatus.getValue().toString();
        if (idmember.isEmpty() || namamember.isEmpty() || alamatmember.isEmpty() || noteleponmember.isEmpty() || tanggalmember.isEmpty()) {
            lbValidasi.setVisible(true);
            return;
        }

        Connection con = new Connection();
        try {
            String insertmember = "INSERT INTO bntu_member VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertmemberStatement = con.conn.prepareStatement(insertmember);
            insertmemberStatement.setString(1, idmember);
            insertmemberStatement.setString(2, namamember);
            insertmemberStatement.setString(3, alamatmember);
            insertmemberStatement.setString(4, noteleponmember);
            insertmemberStatement.setString(5, tanggalmember);
            insertmemberStatement.setString(6, statusmember);

            insertmemberStatement.executeUpdate();
            Stage stages = (Stage) btnSave.getScene().getWindow();
            stages.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/transaksi.fxml"));
            Parent parent = loader.load();
            Transaksi_Controller tc = loader.getController();
            tc.loadDataFromBntuMemberTable();

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
    protected void insertToDB()
    {

    }
}

