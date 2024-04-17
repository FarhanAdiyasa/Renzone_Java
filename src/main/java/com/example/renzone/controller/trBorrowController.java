package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Member;
import io.github.gleidsonmt.gncontrols.GNButtonBase;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class trBorrowController implements Initializable {
    @FXML
    private Pane pnMember;
    @FXML
    private Pane pnTgl;
    @FXML
    private Pane pnPs;
    @FXML
    private Pane pnByr;
    @FXML
    private AnchorPane pnlPs;
    @FXML
    private AnchorPane pnlAk;
    @FXML
    private Pane pnPlhM;
    @FXML
    private AnchorPane cbContainer;
    @FXML
    private Button btnMem;
    @FXML
    private Button btnNon;
    @FXML
    private Button addMore;
    @FXML
    private Button save;
    @FXML
    private ChoiceBox cbJml;
    @FXML
    private AnchorPane pnPSAK;
    @FXML
    private TextField tbJbyr;
    @FXML
    private TextField txtKmbl;
    @FXML
    private TextField txtIdMem;
    @FXML
    private TextField txtRinci;
    @FXML
    private TextField txtTgl;
    @FXML
    private TextField txtSls;
    @FXML
    private TextField txtHarga;
    @FXML
    private Label lbWarn;
    @FXML
    private Label lblSaveVld;

    @FXML
    private Label lblPs;
    @FXML
    private Label lblMeja;
    @FXML
    private Label idTr;
    @FXML
    private Label idKar;
    @FXML Label fotoKtpLb;
    @FXML public Button btnUp;
    File fKtp;
     Image ktp;
    ArrayList<CheckBox> checkedCheckboxes = new ArrayList<>();
    @FXML
    TableView<Member> tbMem;
    @FXML
    TableColumn<Member, String> idCol;
    @FXML
    TableColumn<Member, String> nmCol;
    ObservableList<Member> gameList = FXCollections.observableArrayList();
    @FXML
    private TextField searchBar;
    private GNButtonBase lastClickedButton;
    String pilih ="", ps = "", ak = "";
    String mejaQuery, getS = "", ckPs = "", ckAk ="", jenis, bfr="";
    boolean clicked;
    @FXML  private Label lbWarn1;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataMem();
        dltToimg();
        setImageFromDatabase(fprofil);

        txtHarga.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtHarga.setText(formattedText);
        });
        tbJbyr.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            tbJbyr.setText(formattedText);
        });
        idTr.setText(generateAutoId());
        idKar.setText(generateKarID());
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txtTgl.setText(currentDateTime.format(formatter));
        cbJml.getItems().add("12 Jam");
        cbJml.getItems().add("24 Jam");
        cbJml.getItems().add("48 Jam");
        cbJml.getItems().add("72 Jam");


        cbJml.setOnAction(event -> {
            LocalDateTime now = LocalDateTime.now();
            String selectedJml = cbJml.getSelectionModel().getSelectedItem().toString();

            int selectedJam = Integer.parseInt(selectedJml.split(" ")[0]);
            hitungTotalSewa(selectedJam);
            LocalDateTime selesai = now.plusHours(selectedJam);

            String formattedSelesai = selesai.format(formatter);

            txtSls.setText(formattedSelesai);
        });
    }

    private void clear() {
        txtIdMem.clear();
        txtRinci.clear();
        txtSls.setText("");
        txtTgl.clear();
        txtHarga.setText("");
        tbJbyr.clear();
        txtKmbl.clear();
        cbJml.setValue(null);
        pilih =""; ps = ""; ak = "";
        getS = "";ckPs = ""; ckAk ="";  bfr="";
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txtTgl.setText(currentDateTime.format(formatter));
        fotoKtpLb.setText("No Foto Uploaded");
        idTr.setText(generateAutoId());
        idKar.setText(generateKarID());
        checkedCheckboxes.clear();
    }
    @FXML
    private void onBtnMemberClick()
    {
        lbWarn.setVisible(false);
        btnNon.setStyle("-fx-background-color:rgb(45, 47, 152);");
        btnMem.setStyle("-fx-background-color:rgb(30, 31, 101);");
        pilih = "ps";
    }
    @FXML
    private void onBtnNonClick()
    {
        lbWarn.setVisible(false);
        btnMem.setStyle("-fx-background-color:rgb(45, 47, 152);");
        btnNon.setStyle("-fx-background-color:rgb(30, 31, 101);");
        pilih = "aks";
    }
    @FXML
    protected void onClickPrev()
    {
        if(pnMember.isVisible())
        {
            pnPlhM.setVisible(true);
            pnMember.setVisible(false);
        }
        else if(pnTgl.isVisible())
        {
            pnMember.setVisible(true);
            pnTgl.setVisible(false);
        }
        else if(pnPs.isVisible())
        {
            pnTgl.setVisible(true);
            pnPs.setVisible(false);
        }
        else if(pnByr.isVisible())
        {
            for (Node node : cbContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {

                        if(bfr.isEmpty())
                        {
                            bfr = checkBox.getText();
                        }
                        else {
                            bfr = bfr + ", " +checkBox.getText();
                        }
                    }
                }
            }
            String currentText = txtRinci.getText();
            String updatedText = currentText.replaceAll(bfr, "");
            txtRinci.setText(updatedText);
            bfr = "";
            for (Node node : cbContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                        checkBox.setSelected(false);
                }
            }
            pnPs.setVisible(true);
            pnByr.setVisible(false);
        }
    }
    @FXML
    protected void onClickNext() {
        if(pnPlhM.isVisible())
        {
            if(txtIdMem.getText().isEmpty())
            {
                lbWarn1.setVisible(true);
                return;
            }
            pnMember.setVisible(true);
            pnPlhM.setVisible(false);
        }
        else if (pnMember.isVisible()) {
            if (pilih.isEmpty()) {
                lbWarn.setVisible(true);
                return;
            }
            pnMember.setVisible(false);
            pnTgl.setVisible(true);
            if(pilih == "ps")
            {
                pnlPs.setVisible(true);
                pnlAk.setVisible(false);
                List<GNButtonBase> buttons = pnlPs.getChildren().stream()
                        .filter(node -> node instanceof GNButtonBase)
                        .map(node -> (GNButtonBase) node)
                        .collect(Collectors.toList());
                if (!ps.equals("")) {
                    for (GNButtonBase button : buttons) {
                        if (button.getId().equals(ps)) {
                            button.setStyle("-fx-background-color: rgb(30, 33, 86);");
                        }
                    }
                } else {
                    for (GNButtonBase button : buttons) {
                        button.setStyle("-fx-background-color: rgb(45, 47, 152);");

                        Tooltip tooltip = new Tooltip(button.getId().toString().replace("Playstation", "Playstation "));
                        button.setTooltip(tooltip);

                        button.setOnAction(event -> {
                            // Jika ada tombol sebelumnya yang diklik, kembalikan warnanya ke semula
                            if (lastClickedButton != null) {
                                lastClickedButton.setStyle("-fx-background-color: rgb(45, 47, 152);");
                            }

                            // Update tombol terakhir yang diklik
                            lastClickedButton = button;

                            // Set warna latar belakang tombol yang baru diklik
                            button.setStyle("-fx-background-color: rgb(30, 33, 86);");

                            ps = button.getId().toString();
                            ps = ps.replace("Playstation", "Playstation ");
                        });

                        button.setOnMouseEntered(event -> {
                            tooltip.show(button, event.getScreenX(), event.getScreenY() + 10);
                        });

                        button.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                    }

                }
            }
            else  if(pilih == "aks")
            {
                pnlAk.setVisible(true);
                pnlPs.setVisible(false);
                List<GNButtonBase> buttons = pnlAk.getChildren().stream()
                        .filter(node -> node instanceof GNButtonBase)
                        .map(node -> (GNButtonBase) node)
                        .collect(Collectors.toList());
                if (!ak.equals("")) {
                    for (GNButtonBase button : buttons) {
                        if (button.getId().equals(ak)) {
                            button.setStyle("-fx-background-color: rgb(30, 33, 86);");
                        }
                    }
                } else {
                    for (GNButtonBase button : buttons) {
                        button.setStyle("-fx-background-color: rgb(45, 47, 152);");

                        Tooltip tooltip = new Tooltip(button.getId());
                        button.setTooltip(tooltip);

                        button.setOnAction(event -> {
                            // Jika ada tombol sebelumnya yang diklik, kembalikan warnanya ke semula
                            if (lastClickedButton != null) {
                                lastClickedButton.setStyle("-fx-background-color: rgb(45, 47, 152);");
                            }

                            // Update tombol terakhir yang diklik
                            lastClickedButton = button;

                            // Set warna latar belakang tombol yang baru diklik
                            button.setStyle("-fx-background-color: rgb(30, 33, 86);");

                            ak = button.getId().toString();
                        });

                        button.setOnMouseEntered(event -> {
                            tooltip.show(button, event.getScreenX(), event.getScreenY() + 10);
                        });

                        button.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                    }
                }
            }
        } else if (pnTgl.isVisible()) {
            boolean plihPs = false;
            if (pilih.equals("ps")) {
                for (Node node : pnlPs.getChildren()) {
                    if (node instanceof GNButtonBase) {
                        GNButtonBase gButtonBase = (GNButtonBase) node;
                        if (gButtonBase.getStyle().equals("-fx-background-color: rgb(30, 33, 86);")) {
                            plihPs = true;
                            break; // Hentikan pencarian jika sudah menemukan yang terpilih
                        }
                    }
                }
            } else if (pilih.equals("aks")) {
                for (Node node : pnlAk.getChildren()) {
                    if (node instanceof GNButtonBase) {
                        GNButtonBase gButtonBase = (GNButtonBase) node;
                        if (gButtonBase.getStyle().equals("-fx-background-color: rgb(30, 33, 86);")) {
                            plihPs = true;
                            break; // Hentikan pencarian jika sudah menemukan yang terpilih
                        }
                    }
                }
            }
            if (!plihPs) {
                lblPs.setVisible(true);
                return;
            }
            pnPs.setVisible(true);
            pnTgl.setVisible(false);
            if (!cbContainer.getChildren().isEmpty()) {
                // Bersihkan list dari data sebelumnya (jika diperlukan)
                checkedCheckboxes.clear();
                // Cek satu per satu checkbox dan tambahkan ke dalam ArrayList jika checkbox tersebut dicentang
                for (Node node : cbContainer.getChildren()) {
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkedCheckboxes.add(checkBox);
                        }
                    }
                }
                // Bersihkan container dari semua checkbox
                cbContainer.getChildren().clear();
                // Panggil metode cbPick() untuk menambahkan kembali checkbox yang baru
                cbPick();
                System.out.println();
                // Setel ulang status dari checkbox berdasarkan data di dalam ArrayList checkedCheckboxes
                for (Node node : cbContainer.getChildren()) {
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        if (checkedCheckboxes.contains(checkBox) && (!checkBox.isDisable())) {
                            checkBox.setSelected(true);
                        }
                    }
                }
            } else {
                // Jika container kosong, panggil metode cbPick() untuk menambahkan checkbox awal
                cbPick();
            }
        }else if (pnPs.isVisible()) {
            boolean gas = false;

            StringBuilder selectedMeja = new StringBuilder();
            for (Node node : cbContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {

                        if (selectedMeja.length() > 0) {
                            selectedMeja.append(", ");
                        }
                        selectedMeja.append(checkBox.getText());
                        gas = true;
                    }
                }
            }// Setel teks txtMeja dengan data yang dipilih dari CheckBox
            if (gas) {
                if(pilih == "ps")
                {
                    if(ckPs.isEmpty())
                    {
                        ckPs = selectedMeja.toString();
                    }
                    else {
                        ckPs = ckPs + ", " +selectedMeja.toString();
                    }
                }
                else {
                    if(ckAk.isEmpty())
                    {
                        ckAk = selectedMeja.toString();
                    }
                    else {
                        ckAk = ckAk + ", " +selectedMeja.toString();
                    }
                }
                if(txtRinci.getText().isEmpty())
                {
                    txtRinci.appendText(selectedMeja.toString());
                }
                else {
                    txtRinci.appendText(", " + selectedMeja.toString());
                }
                pnPs.setVisible(false);
                pnByr.setVisible(true);
                tbJbyr.textProperty().addListener((obs, oldValue, newValue) -> {
                    calculateKembalian();
                });
                addMore.setOnAction(event -> {
                    pnByr.setVisible(false);
                    pnMember.setVisible(true);
                    for (Node node : pnPSAK.getChildren()) {
                        if (node instanceof Button) {
                            Button checkBox = (Button) node;
                            checkBox.setStyle("-fx-background-color: rgb(45, 47, 152)");
                        }
                    }
                    lastClickedButton.setStyle("-fx-background-color: rgb(45, 47, 152);");;
                    for (Node node : pnlAk.getChildren()) {
                        if (node instanceof GNButtonBase) {
                            GNButtonBase checkBox = (GNButtonBase) node;
                            if (checkBox.getStyle().equals("-fx-background-color: rgb(30, 33, 86)")) {
                                checkBox.setStyle("-fx-background-color: rgb(30, 33, 86)");
                            }
                        }
                    }
                    lastClickedButton.setStyle("-fx-background-color: rgb(45, 47, 152);");;
                    for (Node node : pnlPs.getChildren()) {
                        if (node instanceof GNButtonBase) {
                            GNButtonBase checkBox = (GNButtonBase) node;
                            if (checkBox.getStyle().equals("-fx-background-color: rgb(30, 33, 86)")) {
                                checkBox.setStyle("-fx-background-color: rgb(30, 33, 86)");
                            }
                        }
                    }
                    lastClickedButton.setStyle("-fx-background-color: rgb(45, 47, 152);");;
                    checkedCheckboxes.clear();
                    cbJml.setValue(null);
                    txtSls.clear();
                    txtHarga.clear();
                    tbJbyr.clear();
                    txtKmbl.clear();
                    ps = "";
                    pilih = "";
                    lbWarn1.setVisible(false);
                    lbWarn.setVisible(false);
                    lblMeja.setVisible(false);
                    lblPs.setVisible(false);
                    lblSaveVld.setVisible(false);
                });
            } else {
                lblMeja.setVisible(true);
            }
        }
    }

    private void calculateKembalian() {
        String bayarFormatted = tbJbyr.getText().replaceAll("[^\\d]", "");
        String totalFormatted = txtHarga.getText().replaceAll("[^\\d]", "");

        if (!bayarFormatted.isEmpty() && !totalFormatted.isEmpty()) {
            int txtBayar = Integer.parseInt(bayarFormatted);
            int txtTotal = Integer.parseInt(totalFormatted);

            int kembalian = txtBayar - txtTotal;

            if (kembalian >= 0) {
                txtKmbl.setText(formatRupiah(String.valueOf(kembalian)));
            } else {
                String formattedText = formatRupiah(String.valueOf(Math.abs(kembalian)));
                txtKmbl.setText( formattedText.replaceAll("Rp. ", "Rp. -"));
            }
        } else {
            txtKmbl.setText(""); // Atur ke teks kosong jika salah satu nilai kosong
        }
    }

    public void cbPick() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            if(pilih == "ps")
            {
                mejaQuery = "SELECT *\n" +
                        "FROM ms_ps\n" +
                        "WHERE id_ps NOT IN (\n" +
                        "    SELECT id_ps\n" +
                        "    FROM dtl_peminjamanps\n" +
                        "    WHERE id_peminjaman IN (\n" +
                        "        SELECT id_peminjaman\n" +
                        "        FROM trs_peminjaman\n" +
                        "        WHERE id_peminjaman NOT IN(\n" +
                        "\t\tSELECT id_peminjaman FROM trs_pengembalian\n" +
                        "\t\t)\n" +
                        "    )\n" +
                        ") AND jenis_ps = '" + ps + "' AND id_ps NOT IN ( SELECT id_ps FROM ms_meja WHERE isDeleted is null) AND kondisi_ps = 'Good'" ;
                getS = "no_seri";
            }
            else if(pilih == "aks")
            {
                 mejaQuery = "SELECT *\n" +
                         "FROM ms_aksesoris\n" +
                         "WHERE id_aksesoris NOT IN (\n" +
                         "    SELECT id_aksesoris\n" +
                         "    FROM dtl_pinjamaksesoris\n" +
                         "    WHERE id_peminjaman IN (\n" +
                         "        SELECT id_peminjaman\n" +
                         "        FROM trs_peminjaman\n" +
                         "        WHERE id_peminjaman NOT IN(\n" +
                         "\t\tSELECT id_peminjaman FROM trs_pengembalian\n" +
                         "\t\t)\n" +
                         "    )\n" +
                         ")\n AND jenis_aksesoris ='"+ ak + "'AND kondisi_aksesoris = 'Good'";
                getS = "nama_aksesoris";
            }

            // Mendapatkan data meja

            ResultSet mejaResult = connection.stat.executeQuery(mejaQuery);

            // Initialize column and row counters
            int columnCount = 0;
            int rowCount = 0;
            // Initialize column and row counters
            double xOffset = 15.0; // X offset for the first checkbox
            double yOffset = 20.0; // Y offset for the first checkbox


            while (mejaResult.next()) {
                String name = mejaResult.getString(getS);

                CheckBox checkBox = new CheckBox(name);
                checkBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white");

                // Set the position of the checkbox in AnchorPane
                AnchorPane.setTopAnchor(checkBox, yOffset + (rowCount * 90.0)); // Adjust the vertical position as needed
                AnchorPane.setLeftAnchor(checkBox, xOffset + (columnCount * 380 / 3)); // Adjust the horizontal position as needed

                // Disable RadioButton for incompatible meja

                // Add the checkbox to the AnchorPane
                cbContainer.getChildren().add(checkBox);
               for(CheckBox checkBox1 : checkedCheckboxes)
                {
                  if(checkBox.getText().equals(checkBox1.getText()))
                  {
                      checkBox.setDisable(true);
                  }
                }
                // Update column and row counters
                columnCount++;
                if (columnCount == 1) {
                    columnCount = 0;
                    rowCount++;
                }
            }

        } catch (Exception e) {
            System.out.println("Terjadi error saat load data game: " + e);
        }
    }
    public void refreshTableMem() {//BUAT LOAD TABLE DARI DATABASE PAKE 2 METHOD refreshTable & loadDate
        try {
            gameList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_member, nama_member from ms_member where status_anggota = 'Active' AND isDeleted is null";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                gameList.add(new Member(
                        connection.result.getString("id_member"),
                        connection.result.getString("nama_member")
                ));
            }
            tbMem.setItems(gameList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    private void loadDataMem() {//LOAD DATANYA
        refreshTableMem();
        idCol.setCellValueFactory(new PropertyValueFactory<>("idMember"));
        nmCol.setCellValueFactory(new PropertyValueFactory<>("namaMember"));
        //add cell of button edit
        tbMem.setItems(gameList);

        tbMem.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Member selectedMember = tbMem.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    txtIdMem.setText(selectedMember.getIdMember());
                }
            }
        });

        //Filtered
        FilteredList<Member> filteredData = new FilteredList<>(gameList, b-> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(Member ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (Member.getIdMember() != null && Member.getIdMember().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Member.getNamaMember() != null && Member.getNamaMember().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Member> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbMem.comparatorProperty());
        tbMem.setItems(sortedData);
        tbMem.setItems(sortedData);
    }

    private void hitungTotalSewa(int selected) {
        int totalSewa = 0;
        StringBuilder selectedMeja = new StringBuilder();

        // Setel teks txtMeja dengan data yang dipilih dari CheckBox
        selectedMeja.append(txtRinci.getText());
        System.out.println(ckAk);
        System.out.println(ckPs);

        try {
            Connection connection = new Connection(); // Menggunakan metode getConnection() dari kelas Connection
            String[] mejaArray = selectedMeja.toString().split(", "); // Memisahkan nama-nama meja dengan tanda koma

            // Membuat placeholder "?" sebanyak jumlah meja yang dipilih
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < mejaArray.length; i++) {
                if (i > 0) {
                    placeholders.append(", ");
                }
                placeholders.append("?");
            }

            String query = "SELECT harga_sewa FROM ms_aksesoris WHERE nama_aksesoris IN (" + placeholders.toString() + ")";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

            // Mengatur nilai-nilai parameter
            for (int i = 0; i < mejaArray.length; i++) {
                preparedStatement.setString(i + 1, mejaArray[i]);
            }
            System.out.println(mejaArray.length);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int hargaSewa = resultSet.getInt("harga_sewa");
                totalSewa += hargaSewa;
            }
             query = "SELECT jenis_ps FROM ms_ps WHERE no_seri IN (" + placeholders.toString() + ")";
             preparedStatement = connection.conn.prepareStatement(query);

            // Mengatur nilai-nilai parameter
            for (int i = 0; i < mejaArray.length; i++) {
                preparedStatement.setString(i + 1, mejaArray[i]);
            }

             resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String hargaSewa = resultSet.getString("jenis_ps");
                if(hargaSewa.equals("Playstation 2"))
                {
                    totalSewa += 100000;
                }
                else if(hargaSewa.equals("Playstation 3"))
                {
                    totalSewa += 150000;
                }
                else if(hargaSewa.equals("Playstation 4"))
                {
                    totalSewa += 200000;
                }
                else if(hargaSewa.equals("Playstation 5"))
                {
                    totalSewa += 300000;
                }
            }

            // Tutup koneksi dan statement
            resultSet.close();
            preparedStatement.close();
            connection.close();
            totalSewa *= selected/12;
            // Tampilkan total sewa
            txtHarga.setText(String.valueOf(totalSewa));
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error jika terjadi masalah dalam mengakses database
        }
    }
    public static void vibrate(TextField anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    int total, bayar;
    @FXML
    protected void onSave() throws SQLException {
        String idT = idTr.getText();
        String tgl = txtTgl.getText();
        String kembali = txtSls.getText();
        String idK = idKar.getText();
        String idM =txtIdMem.getText();
        if (idT.isEmpty() || tgl.isEmpty() || kembali.isEmpty() || idK.isEmpty() || idM.isEmpty() ||fotoKtpLb.getText().equals("No Foto Uploaded")) {
            // Jika ada input yang kosong, tampilkan pesan kesalahan atau lakukan tindakan yang sesuai.
            lblSaveVld.setVisible(true);
            return;
        }
        total = ((txtHarga.getText().isEmpty()) ? 0 : convertCurrencyToInteger(txtHarga.getText()));
        bayar = ((tbJbyr.getText().isEmpty()) ? 0 : convertCurrencyToInteger(tbJbyr.getText()));
        if(bayar<total)
        {
            vibrate(tbJbyr);
            vibrate(txtKmbl);
            return;
        }

        if (idT.isEmpty() || tgl.isEmpty() || kembali.isEmpty() || idK.isEmpty() || idM.isEmpty() || fotoKtpLb.getText().equals("No Foto Uploaded")) {
            // Jika ada input yang kosong, tampilkan pesan kesalahan atau lakukan tindakan yang sesuai.
            lblSaveVld.setVisible(true);
            return;
        }

        List<String> categoryList = Arrays.asList(ckPs.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);
        System.out.println(ckAk);
        System.out.println(ckPs);
        List<String> aksList = Arrays.asList(ckAk.split(",\\s*"));
        List<String> aksyIds = getAksIds(aksList);

        Connection con = new Connection();

        try {
            // ID does not exist, perform an insert
            String insertGameQuery = "INSERT INTO trs_peminjaman VALUES (?, ?, ?, ?, ?, ?, ?, null)";
            PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
            insertGameStatement.setString(1, idT);
            insertGameStatement.setString(2, tgl);
            insertGameStatement.setString(3, kembali);
            insertGameStatement.setInt(4, total);
            insertGameStatement.setString(5, idM);
            insertGameStatement.setString(6, idK);
            insertGameStatement.setInt(7, bayar);
            insertGameStatement.executeUpdate();
            String upd = "UPDATE trs_peminjaman SET foto_ktp = (SELECT ktp FROM temp_Img) where id_peminjaman = '" + idT + "'";
            PreparedStatement updS = con.conn.prepareStatement(upd);
            updS.executeUpdate();
            // Insert data into dtl_game table
            String insertDetailQuery = "INSERT INTO dtl_peminjamanps VALUES (?, ?, ?)";
            PreparedStatement insertDetailStatement = con.conn.prepareStatement(insertDetailQuery);
            for (String categoryId : categoryIds) {
                insertDetailStatement.setString(1, idT);
                insertDetailStatement.setString(2, categoryId);
                insertDetailStatement.setString(3, null);
                insertDetailStatement.executeUpdate();
            }
            String insertDetailQuery2= "INSERT INTO dtl_pinjamaksesoris VALUES (?, ?, ?)";
            PreparedStatement insertDetailStatement2 = con.conn.prepareStatement(insertDetailQuery2);
            for (String categoryId : aksyIds) {
                insertDetailStatement2.setString(1, idT);
                insertDetailStatement2.setString(2, categoryId);
                insertDetailStatement2.setString(3, null);
                insertDetailStatement2.executeUpdate();
            }
            dltToimg();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exportAsset();
        clear();
        pnPlhM.setVisible(true);
        pnByr.setVisible(false);
        txtSls.clear();
        txtHarga.clear();
        //idTr.setText(generateAutoId());
    }
    public static List<String> getCategoryIds(List<String> categories) throws SQLException {
        List<String> categoryIds = new ArrayList<>();
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT id_ps FROM ms_ps WHERE no_seri = ?";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        // Initialize column and row counters
        for (String category : categories) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Memeriksa apakah ada hasil dari query
            if (resultSet.next()) {
                String categoryId = resultSet.getString("id_ps");
                categoryIds.add(categoryId);
            } else {
                // Kategori tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
                System.out.println("Kategori tidak ditemukan: " + category);
            }
            resultSet.close();
        }
        return categoryIds;
    }
    public static List<String> getAksIds(List<String> categories) throws SQLException {
        List<String> categoryIds = new ArrayList<>();
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT id_aksesoris FROM ms_aksesoris WHERE nama_aksesoris = ?";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        // Initialize column and row counters
        for (String category : categories) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Memeriksa apakah ada hasil dari query
            if (resultSet.next()) {
                String categoryId = resultSet.getString("id_aksesoris");
                categoryIds.add(categoryId);
            } else {
                // Kategori tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
                System.out.println("Kategori tidak ditemukan: " + category);
            }
            resultSet.close();
        }
        return categoryIds;
    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_peminjaman FROM trs_peminjaman p WHERE p.tanggal_peminjaman  = CONVERT(VARCHAR(8), GETDATE(), 112) ORDER BY id_peminjaman DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_peminjaman");
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
            id = "BRW" + formattedDate + "001";
            // Jika belum ada data game dalam tabel, set ID otomatis pertama
            return id;
        } else {
            if(lastId.substring(3, 11).equals(formattedDate))
            {
                int lastNumber = Integer.parseInt(lastId.substring(11));

                // Tingkatkan angka ID game terakhir
                int nextNumber = lastNumber + 1;

                // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
                String nextId = String.format("%03d", nextNumber);

                return "BRW" + formattedDate + nextId;
            }
           else {
                id = "BRW" + formattedDate + "001";
                // Jika belum ada data game dalam tabel, set ID otomatis pertama
                return id;
            }
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
    @FXML
    protected void btnUpClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/getImage.fxml"));
            Parent parent = loader.load();
            // Create a new stage with the loaded parent container
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            GetImageController gt = loader.getController();
            // Set the owner of the popup stage as the primary stage (fullscreen form)
            stage.initOwner(StageManager.getPrimaryStage());
            stage.showAndWait();
            getImg();
        } catch (IOException e) {
            e.printStackTrace();
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
                refreshImg();
                refreshLabel();
            } else {
                // Jika data gambar tidak ditemukan
                System.out.println("Data gambar tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat mengambil gambar dari database: " + e);
        }
    }
    protected void refreshImg()
    {
        if(ktp != null)
        {
            fotoKtpLb.setText(txtIdMem.getText() + "_ktp");
        }
        else {
            fotoKtpLb.setText("No Foto Uploaded");
        }

    }
    protected void refreshLabel()
    {
        if(!fotoKtpLb.getText().equals("No Foto Uploaded"))
        {
            fotoKtpLb.setStyle("-fx-underline: false; -fx-font-style: italic;");
            fotoKtpLb.setOnMouseEntered(event -> {
                fotoKtpLb.setStyle("-fx-underline: true; -fx-font-style: italic;");
            });

            fotoKtpLb.setOnMouseExited(event -> {
                fotoKtpLb.setStyle("-fx-underline: false; -fx-font-style: italic;");
            });
            fotoKtpLb.setOnMouseClicked(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/getImage.fxml"));
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Create a new stage with the loaded parent container
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                GetImageController gt = loader.getController();
                // Set the owner of the popup stage as the primary stage (fullscreen form)
                stage.initOwner(StageManager.getPrimaryStage());
                stage.showAndWait();
                getImg();
            });
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
    protected void toUtama(ActionEvent event) throws IOException {// Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/dashboardC.fxml"));
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
    @FXML
    protected void exportAsset() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("id_transaksi", idTr.getText());
                    parameters.put("id_karyawan", idKar.getText());
                    parameters.put("Total", "" + total);
                    parameters.put("Cash", "" + bayar);
                    parameters.put("Change","" + (bayar-total));
                    parameters.put("until", txtSls.getText());
                    // Membuat koneksi ke SQL Server
                    java.sql.Connection conn = Connection.getConnect();

                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\trsBorrow.jrxml";
                    JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                    // Export laporan ke format yang diinginkan (misalnya PDF)
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    // Mengatur zoom level ke 75%
                    viewer.setZoomRatio(0.50F);
                    viewer.setVisible(true);
                    Stage reportStage = new Stage();
                    reportStage.initOwner(pnByr.getScene().getWindow());

                    // Menampilkan Stage
                    reportStage.show();

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
