package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Rental;
import com.example.renzone.models.curPlay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class dsbUtamaController implements Initializable {
    @FXML
    private Button btnCrud;
    @FXML
    private Button btnTr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageFromDatabase(fprofil);
        loadDate();
        dateLoad();
        showMemberData();
        showActiveMember();
        showEmpty();
        btnCrud.setOnAction(event -> {
            try {
                // Membuat FXMLLoader dan memuat file FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardK.fxml"));
                Parent root = loader.load();
                // Membuat stage baru dan mengatur scene dengan root dari file FXML
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
        btnTr.setOnAction(event -> {
            try {
                // Membuat FXMLLoader dan memuat file FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardC.fxml"));
                Parent root = loader.load();

                // Membuat stage baru dan mengatur scene dengan root dari file FXML
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
    @FXML
    private Circle fprofil;
    private void setImageFromDatabase(Circle circle) {
        try {
            java.sql.Connection connection = Connection.getConnect();
            connection.close();
            connection =  Connection.getConnect();// Menggunakan metode getConnect() untuk mendapatkan instance koneksi
            // Query untuk mendapatkan gambar dari database
            String query = "SELECT foto_profil, nama_karyawan FROM login";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Mendapatkan gambar dari hasil query
                InputStream inputStream = resultSet.getBinaryStream("foto_profil");
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    circle.setFill(new ImagePattern(image));
                    Tooltip tooltip = new Tooltip(resultSet.getString("nama_karyawan"));
                    System.out.println(resultSet.getString("nama_karyawan"));
                    circle.setOnMouseEntered(event -> {
                        tooltip.show(circle, event.getScreenX(), event.getScreenY());
                    });
                    circle.setOnMouseExited(event -> {
                        tooltip.hide();
                    });
                } else {
                    // Jika foto_profil adalah NULL, maka atur lingkaran menjadi tidak terlihat
                    circle.setVisible(false);
                }
            }

            // Menutup resultSet dan preparedStatement (connection tidak ditutup)
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void refreshDashboard(ActionEvent event) throws IOException {
        // Load the dashboard scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardUtama.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Create a new stage for the dashboard
        Stage dashboardStage = new Stage();
        dashboardStage.setFullScreen(true);
        dashboardStage.setScene(scene);

        // Show the dashboard stage
        dashboardStage.show();

        // Set the primary stage for the StageManager
        StageManager.setPrimaryStage(dashboardStage);

        // Close the current stage (login stage)
        ((Node) event.getSource()).getScene().getWindow().hide();
    }



    @FXML
    TableView<Rental> curRentTableView;
    @FXML
    TableColumn<Rental, String> IDAK;
    @FXML
    TableColumn<Rental, String> IDM;
    @FXML
    TableColumn<Rental, String> NAMAAK;
    @FXML
    TableColumn<Rental, String> JENISAK;


    @FXML
    TableView<curPlay> curPlayTableView;
    @FXML
    TableColumn<curPlay, String> kodeObatCol;
    @FXML
    TableColumn<curPlay, String> namaObatCol;
    @FXML
    TableColumn<curPlay, String> kemasanCol;
    @FXML
    TableColumn<curPlay, String> detCol;
    String query = null;
    java.sql.Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    curPlay cPlay = null;
    Rental rental = null;

    ObservableList<curPlay> StudentList = FXCollections.observableArrayList();
    ObservableList<Rental> rentalList = FXCollections.observableArrayList();
    private void loadDate() {
        refreshTable();
        kodeObatCol.setCellValueFactory(new PropertyValueFactory<>("kodeObat"));
        namaObatCol.setCellValueFactory(new PropertyValueFactory<>("namaObat"));
        kemasanCol.setCellValueFactory(new PropertyValueFactory<>("kemasan"));
        detCol.setCellValueFactory(new PropertyValueFactory<>("a"));

        curPlayTableView.setItems(StudentList);
    }


    public void refreshTable() {
        try {
            StudentList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM current_play";

            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {


                StudentList.add(new curPlay(
                        connection.result.getString("id_meja"),
                        connection.result.getString("nama_pembeli"),
                        connection.result.getString("tanggal_pembelian"),
                        connection.result.getString("tanggal_selesai")

                ));
                curPlayTableView.setItems(StudentList);
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    private String showNama(String id)
    {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT nama_member from  ms_member where id_member = '" + id + "'";
            connection.result = connection.stat.executeQuery(query);
            String count = "";
            if (connection.result.next()) {
                count = connection.result.getString(1);
            }
            return count;
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
        return null;
    }
    private String showNamaAk(String id)
    {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT nama_asset from  asset where id_asset = '" + id + "'";
            connection.result = connection.stat.executeQuery(query);
            String count = "";
            if (connection.result.next()) {
                count = connection.result.getString(1);
            }
            return count;
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
        return null;
    }

    private String showJenisAk(String id)
    {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT jenis_asset from  asset where id_asset = '" + id + "'";
            connection.result = connection.stat.executeQuery(query);
            String count = "";
            if (connection.result.next()) {
                count = connection.result.getString(1);
            }
            return count;
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
        return null;
    }
    private void dateLoad() {
        refreshTableAk();
        IDAK.setCellValueFactory(new PropertyValueFactory<>("idAK"));
        IDM.setCellValueFactory(new PropertyValueFactory<>("idM"));
        JENISAK.setCellValueFactory(new PropertyValueFactory<>("Jenis"));
        NAMAAK.setCellValueFactory(new PropertyValueFactory<>("namaAK"));



        curRentTableView.setItems(rentalList);
    }
    public void refreshTableAk() {
        try {
            rentalList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM asset_return WHERE id_peminjaman NOT IN (SELECT id_peminjaman from trs_pengembalian)";

            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {


                rentalList.add(new Rental(
                        showNamaAk(connection.result.getString("id_asset")),
                        showNama(connection.result.getString("id_member")),
                        showJenisAk(connection.result.getString("id_asset")),
                        connection.result.getString("tanggal_pengembalian"))
                );
                curRentTableView.setItems(rentalList);
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    @FXML
    private Label lblMember;

    @FXML
    private Label lblEmpty;
    @FXML
    private Label lblActive;
    private void showMemberData() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM ms_member WHERE isDeleted is null";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            while (connection.result.next()) {
                count++;
            }
            String cat = Integer.toString(count);
            lblMember.setText(cat);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

    private void showActiveMember() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM ms_member WHERE status_anggota = 'active'";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            String cat = Integer.toString(count);
            lblActive.setText(cat);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

    private void showEmpty() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            // Menghitung jumlah meja yang ada di tabel 'meja'
            String queryMeja = "SELECT COUNT(*) FROM ms_meja WHERE isDeleted is null";
            ResultSet resultMeja = connection.stat.executeQuery(queryMeja);
            int totalMeja = 0;
            if (resultMeja.next()) {
                totalMeja = resultMeja.getInt(1);
            }

            // Menghitung jumlah meja yang ada di tabel 'transaksi_layanan'
            String queryTransaksi = "SELECT COUNT(DISTINCT id_meja) FROM trs_paket";
            ResultSet resultTransaksi = connection.stat.executeQuery(queryTransaksi);
            int mejaTerisi = 0;
            if (resultTransaksi.next()) {
                mejaTerisi = resultTransaksi.getInt(1);
            }

            // Menghitung jumlah meja kosong
            int mejaKosong = totalMeja - mejaTerisi;

            String cat = Integer.toString(mejaKosong);
            lblEmpty.setText(cat);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    @FXML
    private void switchLogout(ActionEvent event) throws IOException {
        // Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Inisialisasi stage sebelum penggunaannya
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(scene);
        StageManager.setPrimaryStage(stage);

/*        dsbCustController dashboardController = loader.getController();
        dashboardController.setName(name);*/
        stage.show();
    }

}
