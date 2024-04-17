package com.example.renzone.controller;

import com.example.renzone.StageManager;
import com.example.renzone.models.Member;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.renzone.models.TrsPeminjaman;
import com.example.renzone.DBConnection.Connection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class trsController implements Initializable {

    @FXML
    private Pane rbContainer;

    @FXML
    private TextField txtidpinjam;

    @FXML
    private Label lblValidasi;

    @FXML
    private Label lblpengembalian;

    @FXML
    private Label lblkaryawan;

    @FXML
    private TextField txtTgl;
    @FXML
    private ChoiceBox cbstatus;

    @FXML
    private TextField denda;
    @FXML
    private TextField bayar;
    @FXML
    private TextField kembali;

    @FXML
    private AnchorPane Pct;

    @FXML
    TableView<TrsPeminjaman> tblTrs;
    @FXML
    private AnchorPane btnHolder;
    @FXML
    TableColumn<TrsPeminjaman, String> idP;
    @FXML
    TableColumn<TrsPeminjaman, String> tglP;
    @FXML
    TableColumn<TrsPeminjaman, String> idK;
    @FXML
    TableColumn<TrsPeminjaman, String> tglPG;
    @FXML
    TableColumn<TrsPeminjaman, String> biayaCol;
    @FXML
    TableColumn<TrsPeminjaman, String> idMember;
    @FXML
    TableColumn<TrsPeminjaman, String> rincian;

    @FXML
    private TextField searchBar;

    String id, no, des, jns, nama,tgl;

    String query = null;

    static Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    TrsPeminjaman TrsPeminjaman = null;
    private Stage stage;
    private ObservableList<TrsPeminjaman> pjmList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataMem();
        setImageFromDatabase(fprofil);
        lblkaryawan.setText(generateKarID());

        bayar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            bayar.setText(formattedText);
            calculateKembalian();
        });

        denda.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            denda.setText(formattedText);
            bayar.setDisable(false); kembali.setDisable(false);
        });
        Pct.setVisible(true);
        try {
            lblpengembalian.setText(generateAutoId());
            cbstatus.getItems().add("On Time");
            cbstatus.getItems().add("Overdue");
            cbstatus.setValue("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        formattedText = formattedText.replaceAll("Rp", "Rp. ");

        return formattedText;
    }
    private void calculateKembalian() {
        String bayarFormatted = bayar.getText().replaceAll("[^\\d]", "");
        String totalFormatted = denda.getText().replaceAll("[^\\d]", "");

        if (!bayarFormatted.isEmpty() && !totalFormatted.isEmpty()) {
            int txtBayar = Integer.parseInt(bayarFormatted);
            int txtTotal = Integer.parseInt(totalFormatted);

            int kembalian = txtBayar - txtTotal;

            if (kembalian >= 0) {
                kembali.setText(formatRupiah(String.valueOf(kembalian)));
            } else {
                String formattedText = formatRupiah(String.valueOf(Math.abs(kembalian)));
                kembali.setText( formattedText.replaceAll("Rp. ", "Rp. -"));
            }
        } else {
            kembali.setText(""); // Atur ke teks kosong jika salah satu nilai kosong
        }
    }

    public  String generateKarID() {
        String autoId = null;

        String lastId = null;
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_karyawan FROM login";
            connection.result = connection.stat.executeQuery(query);
            // Mendapatkan ID game terakhir
            if (connection.result.next()) {
                lastId = connection.result.getString("id_karyawan");
                System.out.println(lastId);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_pengembalian FROM trs_pengembalian p WHERE p.tanggal_pengembalian  = CONVERT(VARCHAR(8), GETDATE(), 112) ORDER BY id_pengembalian DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_pengembalian");
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
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);
        String id;
        if (lastId == null) {
            id = "BCK" + formattedDate + "001";
            return id;
        } else {
            int lastNumber = Integer.parseInt(lastId.substring(11));
            int nextNumber = lastNumber + 1;
            String nextId = String.format("%03d", nextNumber);
            return "BCK" + formattedDate + nextId;
        }
    }

    public void clear()
        {
            txtTgl.clear();
            cbstatus.setValue(null);
            rbContainer.getChildren().clear();
            denda.clear();
            bayar.clear();
            kembali.clear();
            searchBar.clear();
            bayar.setDisable(true); kembali.setDisable(true);
        }

    private void highlightDatePicker(DatePicker datePicker) {//TRANSISI PAS EDIT ICON DI KLIK
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), datePicker);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        fadeTransition.setOnFinished(event -> datePicker.setStyle("-fx-text-fill: black;"));

        fadeTransition.play();
    }

    public void refreshTable() {//BUAT LOAD TABLE DARI DATABASE PAKE 2 METHOD refreshTable & loadDate
        try {
            pjmList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT p.[id_peminjaman], p.[tanggal_peminjaman], p.[tanggal_pengembalian], p.[biaya], p.[id_member], p.[id_karyawan],"
                    + " SUBSTRING("
                    + "    ("
                    + "        SELECT ', ' + a.[nama_aksesoris]"
                    + "        FROM dtl_pinjamaksesoris da"
                    + "        INNER JOIN ms_aksesoris a ON da.[id_aksesoris] = a.[id_aksesoris]"
                    + "        WHERE da.[id_peminjaman] = p.[id_peminjaman]"
                    + "        FOR XML PATH('')"
                    + "    ), 2, 1000) AS rincian_aksesoris,"
                    + " SUBSTRING("
                    + "    ("
                    + "        SELECT ', ' + ps.[no_seri]"
                    + "        FROM dtl_peminjamanps dps"
                    + "        INNER JOIN ms_ps ps ON dps.[id_ps] = ps.[id_ps]"
                    + "        WHERE dps.[id_peminjaman] = p.[id_peminjaman]"
                    + "        FOR XML PATH('')"
                    + "    ), 2, 1000) AS rincian_ps"
                    + " FROM trs_peminjaman p WHERE id_peminjaman NOT IN (SELECT id_peminjaman FROM trs_pengembalian)";

            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                String idPeminjaman = connection.result.getString("id_peminjaman");
                String tanggalPeminjaman = connection.result.getString("tanggal_peminjaman");
                String tanggalPengembalian = connection.result.getString("tanggal_pengembalian");
                double biaya = connection.result.getDouble("biaya");
                String idMember = connection.result.getString("id_member");
                String idKaryawan = connection.result.getString("id_karyawan");
                String rincianAksesoris = connection.result.getString("rincian_aksesoris");
                String rincianPs = connection.result.getString("rincian_ps");
                String rincian= "";
                // Menggabungkan rincian aksesoris dan ps menjadi satu string
                if(rincianAksesoris== null && rincianPs != null)
                {
                    rincian = rincianPs;
                }
                else if(rincianAksesoris!= null && rincianPs== null)
                {
                    rincian = rincianAksesoris;
                }
                else
                {
                     rincian = rincianAksesoris + ", " + rincianPs;
                }


                // Tambahkan ke dalam list atau lakukan operasi lain sesuai kebutuhan
                pjmList.add(new TrsPeminjaman(idPeminjaman, tanggalPeminjaman, tanggalPengembalian, biaya, idMember, idKaryawan, rincian));
            }

            tblTrs.setItems(pjmList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    private void loadDataMem() {//LOAD DATANYA
        refreshTable();
        idP.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));
        tglP.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));
        tglPG.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));
        biayaCol.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        idK.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        idMember.setCellValueFactory(new PropertyValueFactory<>("idMember"));
        rincian.setCellValueFactory(new PropertyValueFactory<>("rincian"));
        //add cell of button edit
        tblTrs.setItems(pjmList);

        tblTrs.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                TrsPeminjaman selectedMember = tblTrs.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    txtidpinjam.setText(selectedMember.getIdPeminjaman());
                    try {
                        rbPick();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    String tanggalPengembalianStr = selectedMember.getTanggalPengembalian();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate tanggalPengembalian = LocalDate.parse(tanggalPengembalianStr, formatter);
                    LocalDate currentDate = LocalDate.now();
                    txtTgl.setText(currentDate.toString());
                    if (tanggalPengembalian.isBefore(currentDate)) {
                        cbstatus.setValue("Overdue");
                    } else {
                        cbstatus.setValue("On Time");
                    }
                    denda.clear();
                    bayar.clear();
                    kembali.clear();
                }
            }
        });


        //Filtered
        FilteredList<TrsPeminjaman> filteredData = new FilteredList<>(pjmList, b-> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(TrsPeminjaman ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (TrsPeminjaman.getIdMember() != null && TrsPeminjaman.getIdMember().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (TrsPeminjaman.getIdPeminjaman() != null && TrsPeminjaman.getIdPeminjaman().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (TrsPeminjaman.getTanggalPeminjaman() != null && TrsPeminjaman.getTanggalPeminjaman().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (TrsPeminjaman.getTanggalPengembalian() != null && TrsPeminjaman.getTanggalPengembalian().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }   else if (TrsPeminjaman.getRincian() != null && TrsPeminjaman.getRincian().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else if (TrsPeminjaman.getBayar() != 0) {
                    return true;
                } else if (TrsPeminjaman.getIdKaryawan() != null && TrsPeminjaman.getIdKaryawan().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else {
                    return false;
                }
            });
        });
        SortedList<TrsPeminjaman> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblTrs.comparatorProperty());
        tblTrs.setItems(sortedData);
        tblTrs.setItems(sortedData);
    }
    private void rbPick() throws SQLException {
        try (java.sql.Connection con = Connection.getConnect()) {
            // Mendapatkan data aksesoris dari tabel ms_aksesoris
            List<String> aksesorisList = getDataFromDatabase(con, "ms_aksesoris", getNamaAksesorisPeminjaman(con, txtidpinjam.getText()));

            // Mendapatkan data aksesoris dari tabel ms_ps
            List<String> psList = getDataFromDatabase(con, "ms_ps", getNamaAksesorisPeminjaman(con, txtidpinjam.getText()));

            // Menggabungkan kedua list aksesoris
            List<String> combinedList = new ArrayList<>();
            combinedList.addAll(aksesorisList);
            combinedList.addAll(psList);

            // Membersihkan panel sebelum menambahkan radio button
            rbContainer.getChildren().clear();

            // Membuat radio button untuk setiap aksesoris


            for (String aksesoris : combinedList) {
                Label label = new Label(aksesoris);
                label.setFont(Font.font(16)); // Atur ukuran font menjadi 16
                RadioButton radioGood = new RadioButton("Good");
                RadioButton radioBroke = new RadioButton("Broke");
                ToggleGroup toggleGroup = new ToggleGroup(); // Buat instance ToggleGroup
                // Mengatur toggle group untuk radio buttons
                radioGood.setToggleGroup(toggleGroup);
                radioBroke.setToggleGroup(toggleGroup);

                // Mengatur tata letak radio button dan label
                VBox vbox = new VBox(label, radioGood, radioBroke);
                vbox.setSpacing(10);

                // Menambahkan radio button ke dalam panel
                rbContainer.getChildren().addAll(vbox, new Pane());
            }

            // Mengatur tata letak rbContainer menggunakan FlowPane
            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(10);
            flowPane.setVgap(10);
            flowPane.getChildren().addAll(rbContainer.getChildren());
            rbContainer.getChildren().setAll(flowPane);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> getDataFromDatabase(java.sql.Connection con, String tableName, String aks) throws SQLException {
        List<String> aksesorisList = new ArrayList<>();

        try {
            // Mengambil data aksesoris dari tabel yang ditentukan
            if (tableName.equals("ms_ps")) {
                String[] aksesorisArray = aks.split(",");
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                for (String aksesoris : aksesorisArray) {
                    joiner.add("'" + aksesoris.trim() + "'");
                }
                String query = "SELECT no_seri FROM " + tableName + " WHERE no_seri IN " + joiner.toString();
                try (PreparedStatement stmt = con.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String noSeri = rs.getString("no_seri");
                        aksesorisList.add(noSeri);
                    }
                }
            } else {
                String[] aksesorisArray = aks.split(",");
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                for (String aksesoris : aksesorisArray) {
                    joiner.add("'" + aksesoris.trim() + "'");
                }
                String query = "SELECT nama_aksesoris FROM " + tableName + " WHERE nama_aksesoris IN " + joiner.toString();
                try (PreparedStatement stmt = con.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String namaAksesoris = rs.getString("nama_aksesoris");
                        aksesorisList.add(namaAksesoris);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < aksesorisList.size(); i++) {
            System.out.println(aksesorisList.get(i));
        }

        return aksesorisList;
    }

    private String getNamaAksesorisPeminjaman(java.sql.Connection con, String idPeminjaman) throws SQLException {
        StringBuilder sb = new StringBuilder();

        // Query untuk mencari nama aksesoris dari tabel ms_aksesoris
        String queryAksesoris = "SELECT nama_aksesoris FROM ms_aksesoris WHERE id_aksesoris IN (SELECT id_aksesoris FROM dtl_pinjamaksesoris WHERE id_peminjaman = ?)";
        try (PreparedStatement stmtAksesoris = con.prepareStatement(queryAksesoris)) {
            stmtAksesoris.setString(1, idPeminjaman);
            try (ResultSet rsAksesoris = stmtAksesoris.executeQuery()) {
                while (rsAksesoris.next()) {
                    String namaAksesoris = rsAksesoris.getString("nama_aksesoris");
                    sb.append(namaAksesoris).append(", ");
                }
            }
        }

        // Query untuk mencari nama PS dari tabel ms_ps
        String queryPs = "SELECT no_seri FROM ms_ps WHERE id_ps IN (SELECT id_ps FROM dtl_peminjamanps WHERE id_peminjaman = ?)";
        try (PreparedStatement stmtPs = con.prepareStatement(queryPs)) {
            stmtPs.setString(1, idPeminjaman);
            try (ResultSet rsPs = stmtPs.executeQuery()) {
                while (rsPs.next()) {
                    String namaPs = rsPs.getString("no_seri");
                    sb.append(namaPs).append(", ");
                }
            }
        }

        // Menghapus koma terakhir
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 2);
        }

        return sb.toString();
    }
    public static void vibrate(Pane anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    public static void vibrate(TextField anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
        
    }
    int dd, byr;
    @FXML
    protected void simpanOnClick() throws SQLException {
        Boolean aman = false;
        String idT = lblpengembalian.getText();
        String tgl = txtTgl.getText();
        String idk =lblkaryawan.getText();

         dd = (denda.getText().isEmpty()) ? 0 : convertCurrencyToInteger(denda.getText());
         byr = (bayar.getText().isEmpty()) ? 0 : convertCurrencyToInteger(bayar.getText());
        for (Node node : rbContainer.getChildren()) {
            if (node instanceof FlowPane) {
                FlowPane flowPane = (FlowPane) node;
                for (Node vboxNode : flowPane.getChildren()) {
                    if (vboxNode instanceof VBox) {
                        VBox vbox = (VBox) vboxNode;

                        for (Node vboxChildNode : vbox.getChildren()) {
                            if (vboxChildNode instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) vboxChildNode;
                                if (radioButton.isSelected()) {
                                    aman = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!aman)
        {
            vibrate(rbContainer);
            return;
        }
        if(((kembali.getText().isEmpty()) ? 0 : convertCurrencyToInteger(kembali.getText()))<0)
        {
            vibrate(kembali);
            vibrate(bayar);
            kembali.setLayoutX(272);
            bayar.setLayoutX(272);
            return;
        }
        Connection con = new Connection();
        try {
            // ID does not exist, perform an insert
            String insertGameQuery = "INSERT INTO trs_pengembalian VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
            insertGameStatement.setString(1, idT);
            insertGameStatement.setString(2, tgl);
            insertGameStatement.setInt(3, dd);
            insertGameStatement.setString(4, idk);
            insertGameStatement.setInt(5, byr);
            insertGameStatement.setString(6, txtidpinjam.getText());
            insertGameStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exportAsset();
        updAks();
        clear();
        refreshTable();
        loadDataMem();
    }
    public void updAks()
    {
        Connection con = new Connection();
        for (Node node : rbContainer.getChildren()) {
            if (node instanceof FlowPane) {
                FlowPane flowPane = (FlowPane) node;
                for (Node vboxNode : flowPane.getChildren()) {
                    if (vboxNode instanceof VBox) {
                        VBox vbox = (VBox) vboxNode;
                        RadioButton selectedRadioButton = null;
                        for (Node vboxChildNode : vbox.getChildren()) {
                            if (vboxChildNode instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) vboxChildNode;
                                if (radioButton.isSelected()) {
                                    selectedRadioButton = radioButton;
                                    break;
                                }
                            }
                        }
                        if (selectedRadioButton != null) {
                            String labelText = ((Label) vbox.getChildren().get(0)).getText();
                            System.out.println("Radio button dipilih untuk: " + labelText);
                            String querydt = "UPDATE dtl_pinjamaksesoris SET kondisi_pengembalian = ? WHERE id_peminjaman = ? AND id_aksesoris = (SELECT id_aksesoris FROM ms_aksesoris WHERE nama_aksesoris = ?)";
                            try (PreparedStatement insertGameStatement = con.conn.prepareStatement(querydt);) {
                                insertGameStatement.setString(1, selectedRadioButton.getText());
                                insertGameStatement.setString(2, txtidpinjam.getText());
                                insertGameStatement.setString(3, labelText);
                                insertGameStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            String querydtps = "UPDATE dtl_peminjamanps SET kondisi_pengembalian = ? WHERE id_peminjaman = ? AND id_ps = (SELECT id_ps FROM ms_ps WHERE no_seri = ?)";
                            try (PreparedStatement insertGameStatement = con.conn.prepareStatement(querydtps);) {
                                insertGameStatement.setString(1, selectedRadioButton.getText());
                                insertGameStatement.setString(2, txtidpinjam.getText());
                                insertGameStatement.setString(3, labelText);
                                insertGameStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            String query = "UPDATE ms_aksesoris SET kondisi_aksesoris = ? WHERE nama_aksesoris = ?";
                            try (PreparedStatement insertGameStatement = con.conn.prepareStatement(query);) {
                                insertGameStatement.setString(1, selectedRadioButton.getText());
                                insertGameStatement.setString(2, labelText);
                                insertGameStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            String queryPS = "UPDATE ms_ps SET kondisi_ps = ? WHERE no_seri = ?";
                            try (PreparedStatement insertGameStatement = con.conn.prepareStatement(queryPS);) {
                                insertGameStatement.setString(1, selectedRadioButton.getText());
                                insertGameStatement.setString(2, labelText);
                                insertGameStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML void cncl() throws SQLException {
        clear();
    }
    @FXML
    private Circle fprofil;
    private void setImageFromDatabase(Circle circle) {
        Connection connection = new Connection();
        try {
            // Query untuk mendapatkan gambar dari database
            String query = "SELECT foto_profil FROM login";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Mendapatkan gambar dari hasil query
                InputStream inputStream = resultSet.getBinaryStream("foto_profil");
                Image image = new Image(inputStream);

                // Mengatur gambar pada lingkaran
                circle.setFill(new ImagePattern(image));
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
    @FXML
    private Button bckToUtama;
    @FXML
    protected void toUtama(ActionEvent event) throws IOException {// Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardC.fxml"));
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
    }
    @FXML
    protected void exportAsset() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("id_transaksi", txtidpinjam.getText());
                    parameters.put("id_karyawan", lblkaryawan.getText());
                    parameters.put("Total", String.valueOf(dd));
                    parameters.put("Cash", String.valueOf(byr));
                    parameters.put("Change", String.valueOf(byr-dd));
                    // Membuat koneksi ke SQL Server
                    java.sql.Connection conn = Connection.getConnect();

                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\trReturn.jrxml";
                    JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                    // Export laporan ke format yang diinginkan (misalnya PDF)
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    // Mengatur zoom level ke 75%
                    viewer.setZoomRatio(0.50F);
                    viewer.setVisible(true);
                    Stage reportStage = new Stage();
                    reportStage.initOwner(lblkaryawan.getScene().getWindow());

                    // Menampilkan Stage
                    reportStage.showAndWait();

                    // Menutup koneksi setelah selesai
                    conn.close();
                } catch (SQLException | JRException e) {
                    e.printStackTrace();
                }
                return null;

            }
        };

        // Menjalankan tugas di thread terpisah
        Thread thread = new Thread(task);
        thread.start();
    }
    public int convertCurrencyToInteger(String currencyText) {
        // Hapus karakter non-digit dari teks (misalnya "Rp. 10.000" menjadi "10000")
        String numericText = currencyText.replaceAll("[^\\d]", "");

        // Konversi teks numerik menjadi angka integer
        int intValue = Integer.parseInt(numericText);

        return intValue;
    }


}






