package com.example.renzone.controller;
import com.example.renzone.StageManager;
import com.example.renzone.models.bMember;
import com.example.renzone.DBConnection.Connection;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.text.*;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
public class Transaksi_Controller implements Initializable {
    @FXML Label lbltransaksi;
    @FXML Label lblidkaryawan;
    @FXML TextField txtdibayar;
    @FXML TextField txtkembalian;

    @FXML TextField txtidtransaksi;
    @FXML TextField txtid;
    @FXML TextField txtnama;
    @FXML TextField txtalamat;
    @FXML TextField txtnotelepon;
    @FXML TextField txttanggal;
    @FXML ComboBox cmbstatus;
    @FXML TextField txttotal;
    @FXML TextField txtmulai;
    @FXML TextField txtselesai;
    @FXML Button newBtn;
    @FXML Button exstBtn;
    @FXML
    AnchorPane pnBtn;
    private Image ktp;
    private ComboBox<String> cmbkaryawan;
    public boolean afterMember = false;

    public boolean isAfterMember() {
        return afterMember;
    }

    public void setAfterMember(boolean afterMember) {
        this.afterMember = afterMember;
    }

    private ObservableList<String> statusOptions = FXCollections.observableArrayList();

    @FXML Label lbValidasi;
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    bMember member = null;
    private Stage stage;
    ObservableList<bMember> MemberList = FXCollections.observableArrayList();
    String idmember, namamember, alamatmember, noteleponmember, tanggalmember, statusmember, idtransaksi, tanggalmulai,tanggalselesai,statusbayar,idkaryawan;
    int totalbiaya;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //loadDate();

        txttotal.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txttotal.setText(formattedText);
        });
        txtdibayar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtdibayar.setText(formattedText);
        });
        txttotal.setText("Rp.10.000");
        cmbstatus.getItems().add("Active");
        cmbstatus.getItems().add("Inactive");
        setDates();
        lbltransaksi.setText(generateAutoId());
        loadkaryawan();
        loadDataFromBntuMemberTable();
        cmbstatus.setDisable(true);
        txtdibayar.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateKembalian();
        });
    }
    public void setDates() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Add 1 month to the current date to get the end date
        LocalDate endDate = currentDate.plusMonths(1);

        // Format the dates as strings in the format "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = currentDate.format(formatter);
        String endDateString = endDate.format(formatter);

        // Set the text in the text fields
        txtmulai.setText(startDateString);
        txtselesai.setText(endDateString);
        txttanggal.setText(startDateString);
    }

    /*private void loadIDKaryawan() {
        Connection con = new Connection();
        try {
            String query = "SELECT * FROM ms_karyawan";
            PreparedStatement statement = con.conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id_member");


                txtid.setText(id);
                txtnama.setText(nama);
                txtalamat.setText(alamat);
                txtnotelepon.setText(noTelepon);
                txttanggal.setText(tanggal);
                cmbstatus.setValue(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public void loadDataFromBntuMemberTable() {
        Connection con = new Connection();
        try {
            String query = "SELECT * FROM bntu_member";
            PreparedStatement statement = con.conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("id_member");
                String nama = resultSet.getString("nama_member");
                String alamat = resultSet.getString("alamat");
                String noTelepon = resultSet.getString("no_telp");
                String tanggal = resultSet.getString("tgl_daftar");
                String status = resultSet.getString("status_anggota");

                txtid.setText(id);
                txtnama.setText(nama);
                txtalamat.setText(alamat);
                txtnotelepon.setText(noTelepon);
                txttanggal.setText(tanggal);
                cmbstatus.setValue("Active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadkaryawan() {
        Connection con = new Connection();
        try {
            String query = "SELECT id_karyawan FROM login";
            PreparedStatement statement = con.conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id_karyawan = resultSet.getString("id_karyawan");
                lblidkaryawan.setText(id_karyawan);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_trsmember FROM trs_member ORDER BY id_trsmember DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_trsmember");
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
            return "TRM001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("TRM%03d", nextNumber);
        }
    }
    protected void onDibayarChanged(KeyEvent event) {
        calculateKembalian();
    }
    private void calculateKembalian() {
        String bayarFormatted = txtdibayar.getText().replaceAll("[^\\d]", "");
        String totalFormatted = txttotal.getText().replaceAll("[^\\d]", "");

        if (!bayarFormatted.isEmpty() && !totalFormatted.isEmpty()) {
            int txtBayar = Integer.parseInt(bayarFormatted);
            int txtTotal = Integer.parseInt(totalFormatted);

            int kembalian = txtBayar - txtTotal;

            if (kembalian >= 0) {
                txtkembalian.setText(formatRupiah(String.valueOf(kembalian)));
            } else {
                String formattedText = formatRupiah(String.valueOf(Math.abs(kembalian)));
                System.out.println(formattedText.replaceAll("Rp.", "Rp.-"));
                txtkembalian.setText(formattedText.replaceAll("Rp.", "Rp.-"));
            }
        } else {
            txtkembalian.setText(""); // Atur ke teks kosong jika salah satu nilai kosong
        }
    }

    String sls = "", mli = "";
    @FXML
    protected void btnSaveTransaksiClicked(ActionEvent event) throws SQLException {
        calculateKembalian();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date tanggalMulai;
        java.util.Date tanggalSelesai;

        try {
            tanggalMulai = dateFormat.parse(txtmulai.getText());
            tanggalSelesai = dateFormat.parse(txtselesai.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Date sqlTanggalMulai = new Date(tanggalMulai.getTime());
        Date sqlTanggalSelesai = new Date(tanggalSelesai.getTime());
        sls = txtselesai.getText();
        mli = txttanggal.getText();
        idmember = txtid.getText();
        namamember = txtnama.getText();
        alamatmember = txtalamat.getText();
        noteleponmember = txtnotelepon.getText();
        tanggalmember = txttanggal.getText();
        statusmember = cmbstatus.getValue().toString();

        idtransaksi = lbltransaksi.getText();
        String totalFormatted  = txttotal.getText().replaceAll("[^\\d]", "");
        totalbiaya = convertCurrencyToInteger(txttotal.getText());
        statusbayar = "Aktif";
        idkaryawan = lblidkaryawan.getText();

        if (idmember.isEmpty() || namamember.isEmpty() || alamatmember.isEmpty() || noteleponmember.isEmpty() || tanggalmember.isEmpty() || txtdibayar.getText().isEmpty() || txtkembalian.getText().isEmpty()) {
            lbValidasi.setVisible(true);
            return;
        }
        String bayarFormatted = txtkembalian.getText().replaceAll("[^\\d]", "");
        if(convertCurrencyToInteger(txtdibayar.getText())<convertCurrencyToInteger(txttotal.getText()))
        {
            vibrate(txtdibayar);
            vibrate(txtkembalian);
            return;
        }

        Connection con = new Connection();

        try {
            String checkIdQuery = "SELECT COUNT(*) FROM ms_member WHERE id_member = ? AND isDeleted is null";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, idmember);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                String insertMemberQuery = "INSERT INTO ms_member VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertMemberStatement = con.conn.prepareStatement(insertMemberQuery);
                insertMemberStatement.setString(1, idmember);
                insertMemberStatement.setString(2, namamember);
                insertMemberStatement.setString(3, alamatmember);
                insertMemberStatement.setString(4, noteleponmember);
                insertMemberStatement.setString(5, tanggalmember);
                insertMemberStatement.setString(6, statusmember);
                insertMemberStatement.setString(7, null);
                insertMemberStatement.executeUpdate();
            }
            else {
                if(afterMember) {
                    String insertMemberQuery = "UPDATE ms_member\n" +
                            "SET\n" +
                            "    ms_member.nama_member = bntu_member.nama_member,\n" +
                            "    ms_member.alamat = bntu_member.alamat,\n" +
                            "    ms_member.no_telp = bntu_member.no_telp,\n" +
                            "    ms_member.tgl_daftar = '" + tanggalmember + "',\n" +
                            "    ms_member.status_anggota = bntu_member.status_anggota\n" +
                            "FROM\n" +
                            "    ms_member\n" +
                            "    INNER JOIN bntu_member ON ms_member.id_member = bntu_member.id_member";
                    PreparedStatement insertMemberStatement = con.conn.prepareStatement(insertMemberQuery);
                    insertMemberStatement.executeUpdate();
                }
            }
            String insertTransaksiQuery = "INSERT INTO trs_member (id_trsmember, total_biaya, tanggal_mulai, tanggal_akhir, id_member, id_karyawan) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertTransaksiStatement = con.conn.prepareStatement(insertTransaksiQuery);
            insertTransaksiStatement.setString(1, idtransaksi);
            insertTransaksiStatement.setInt(2, totalbiaya);
            insertTransaksiStatement.setDate(3, sqlTanggalMulai);
            insertTransaksiStatement.setDate(4, sqlTanggalSelesai);
            insertTransaksiStatement.setString(5, idmember);
            insertTransaksiStatement.setString(6, idkaryawan);

            insertTransaksiStatement.executeUpdate();
            String deleteBntuMemberQuery = "DELETE FROM bntu_member";
            PreparedStatement deleteBntuMemberStatement = con.conn.prepareStatement(deleteBntuMemberQuery);
            deleteBntuMemberStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exportAsset();
        clear();
        if (!pnBtn.isVisible()) {
            // Membuat ScaleTransition untuk efek kedip-kedip pada pnBtn
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), mainPane);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(event1 -> openNextForm(event));

            // Memulai animasi transisi
            fadeTransition.play();
        }

        txtid.setText(generateAutoId());
    }
    public static void vibrate(TextField anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    private void openNextForm(ActionEvent event) {
        // Mendapatkan nama file FXML yang terkait dengan form lain
        String fxmlFile = "/com/example/renzone/FXML/Karyawan/crudMember.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            // Membuat FXMLLoader dan memuat file FXML
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
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear()  {
        txtmulai.clear();
        txtselesai.clear();
        cmbstatus.setValue(null);
        txtid.clear();
        txtnama.clear();
        txtalamat.clear();
        txtnotelepon.clear();
        txttanggal.clear();
        txtkembalian.clear();
        txtdibayar.clear();
        cmbstatus.setDisable(true);
    }
    @FXML protected AnchorPane mainPane;
    @FXML
    protected void btnCancelClicked(ActionEvent event) throws SQLException {
        Connection con = new Connection();
        String deleteBntuMemberQuery = "DELETE FROM bntu_member";
        PreparedStatement deleteBntuMemberStatement = con.conn.prepareStatement(deleteBntuMemberQuery);
        deleteBntuMemberStatement.executeUpdate();
        clear();
        pnBtn.setVisible(true);
        if(afterMember)
        {
            // Membuat ScaleTransition untuk efek kedip-kedip pada pnBtn
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), mainPane);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setOnFinished(event1 -> openNextForm(event));

            // Memulai animasi transisi
            fadeTransition.play();
        }
    }
    @FXML
    protected void btnNewClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/insertMember.fxml"));
            Parent parent = loader.load();

            // Create a new stage with the loaded parent container
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);

            // Set the owner of the popup stage as the primary stage (fullscreen form)
            stage.initOwner(StageManager.getPrimaryStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnExstClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/pickFromTb.fxml"));
            Parent parent = loader.load();

            // Create a new stage with the loaded parent container
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);

            // Set the owner of the popup stage as the primary stage (fullscreen form)
            stage.initOwner(StageManager.getPrimaryStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void toUtama(ActionEvent event) throws IOException {// Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardC.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Renzone");
        // Menampilkan stage baru dalam mode fullscreen
        stage.setFullScreen(true);
        StageManager.setPrimaryStage(stage);
        // Menampilkan stage baru
        stage.show();
    }
    @FXML
    protected void exportAsset() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("id_transaksi", lbltransaksi.getText());
                    parameters.put("id_karyawan", lblidkaryawan.getText());
                    parameters.put("Cash", txtdibayar.getText());
                    parameters.put("Change", txtkembalian.getText());
                    parameters.put("tanggal_selesai", sls);
                    parameters.put("tanggal_mulai", mli);
                    parameters.put("id_member", txtid.getText());

                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\trMember.jrxml";
                    JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

                    // Export laporan ke format yang diinginkan (misalnya PDF)
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    // Mengatur zoom level ke 75%
                    viewer.setZoomRatio(0.50F);
                    viewer.setVisible(true);
                    Stage reportStage = new Stage();
                    reportStage.initOwner(txtkembalian.getScene().getWindow());
                    reportStage.setFullScreen(false);

                    // Menampilkan Stage
                    reportStage.showAndWait();
                } catch (JRException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Menjalankan tugas di thread terpisah
        Thread thread = new Thread(task);
        thread.start();
    }
    private String formatRupiah(String angka) {
        if (angka.isEmpty()) {
            return "";
        }

        // Mengubah string angka menjadi tipe long
        long value = Long.parseLong(angka);

        // Mengatur format angka dengan dua digit desimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setMaximumFractionDigits(0);
        decimalFormat.setParseBigDecimal(true);

        // Mengformat angka sebagai mata uang rupiah
        String formattedText = decimalFormat.format(value);

        // Menambahkan titik setelah simbol mata uang "Rp"
        formattedText = formattedText.replaceAll("Rp", "Rp.");

        return formattedText;
    }
    public int convertCurrencyToInteger(String currencyText) {
        // Hapus karakter non-digit dari teks (misalnya "Rp. 10.000" menjadi "10000")
        String numericText = currencyText.replaceAll("[^\\d]", "");

        // Konversi teks numerik menjadi angka integer
        int intValue = Integer.parseInt(numericText);

        return intValue;
    }



}
