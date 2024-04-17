package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Layanan;
import com.example.renzone.models.Member;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.github.gleidsonmt.gncontrols.GNButtonBase;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;

import java.io.FileOutputStream;
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

public class trRentalController implements Initializable {
    @FXML
    private Pane pnMember;
    @FXML
    private Pane pnTgl;
    @FXML
    private Pane pnPs;
    @FXML
    private Pane pnByr;
    @FXML
    private Pane pnTr;

    @FXML
    private Button btnMem;
    @FXML
    private Button btnNon;
    @FXML
    private Button btnSimpan;
    @FXML
    private AnchorPane entrName;
    @FXML
    private TextField inputNama;
    @FXML
    private AnchorPane apMem;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField searchBarLay;
    @FXML
    private Label lbWarn;
    @FXML
    private Label lblPs;
    @FXML
    private Label lblMeja;
    @FXML
    private Label lblLay;
    @FXML
    private Label idTr;
    @FXML
    private Label idKar;
    GNButtonBase lastClickedButton = null;
    RadioButton lasChecked = null;
    @FXML
    TableView<Member> tbMem;
    @FXML
    TableColumn<Member, String> idCol;
    @FXML
    TableColumn<Member, String> nmCol;
    ObservableList<Member> gameList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane rbContainer;
    @FXML
    private AnchorPane apImgPs;



    @FXML
    private TextField txtIdMem;
    @FXML
    private TextField txtNm;
    @FXML
    private TextField txtLay;
    @FXML
    private TextField txtMeja;
    @FXML
    private TextField txtTgl;
    @FXML
    private TextField txtSls;
    @FXML
    private TextField txtHarga;
    @FXML
    private TextField txtByr;
    @FXML
    private TextField txtKmbl;
    String id, nm, ps="";
    @FXML
    TableView<Layanan> tbLay;
    @FXML
    TableColumn<Layanan, String> pktCol;
    @FXML
    TableColumn<Layanan, Integer> hrgCol;
    @FXML
    TableColumn<Layanan, String> jmlCol;
    int total = 0, ttlDur = 0;
    ObservableList<Layanan> pktList = FXCollections.observableArrayList();
    Layanan lynn = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    String query = null;
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
    @FXML
    private void onBtnMemberClick()
    {
        clear();
        lbWarn.setVisible(false);
        btnNon.setStyle("-fx-background-color:rgb(45, 47, 152);");
        btnMem.setStyle("-fx-background-color:rgb(30, 31, 101);");
        entrName.setVisible(false);
        apMem.setVisible(true);
        loadDataMem();
    }
    @FXML
    private void onBtnNonClick()
    {
        clear();
        lbWarn.setVisible(false);
        btnMem.setStyle("-fx-background-color:rgb(45, 47, 152);");
        btnNon.setStyle("-fx-background-color:rgb(30, 31, 101);");
        apMem.setVisible(false);
        entrName.setVisible(true);
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
                        txtNm.setText(selectedMember.getNamaMember());
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

    private void clear() {
        txtIdMem.clear();
        txtNm.clear();
        txtLay.clear();
        txtMeja.clear();
        txtTgl.clear();
        txtHarga.clear();
        txtSls.clear();
        setImageFromDatabase(fprofil);
        idTr.setText(generateAutoId());
        idKar.setText(generateKarID());


    }
    public static void vibrate(Pane anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    @FXML
    protected void onClickNext() {
        if (pnMember.isVisible()) {
            if (entrName.isVisible()) {
                if (inputNama.getText().equals("")) {
                    vibrate(inputNama);
                    return;
                }
                txtNm.setText(inputNama.getText());
                txtIdMem.setText("non-member");
            }
            if (txtIdMem.getText().equals("") || txtNm.getText().equals("")) {
                vibrate(txtIdMem);
                vibrate(txtNm);
                return;
            }
            pnMember.setVisible(false);
            pnTgl.setVisible(true);
            List<GNButtonBase> buttons = apImgPs.getChildren().stream()
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
        } else if (pnTgl.isVisible()) {
            boolean plihPs = false;
            for (Node node : apImgPs.getChildren()) {
                if (node instanceof GNButtonBase) {
                    GNButtonBase gButtonBase = (GNButtonBase) node;
                    if (gButtonBase.getStyle().equals("-fx-background-color: rgb(45, 47, 152);")) {
                        plihPs = true;
                    }
                }
            }
            if (!plihPs) {
                lblPs.setVisible(true);
                return;
            }
            System.out.println(plihPs);
            pnPs.setVisible(true);
            pnTgl.setVisible(false);
            if (!rbContainer.getChildren().isEmpty()) {
                for (Node node : rbContainer.getChildren()) {
                    if (node instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) node;
                        if (radioButton.isSelected()) {
                            lasChecked = radioButton;
                        }
                    }
                }
                rbContainer.getChildren().clear();
                cbPick();
            } else {
                cbPick();
            }

        } else if (pnPs.isVisible()) {
            boolean gas = false;
            for (Node node : rbContainer.getChildren()) {
                if (node instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) node;
                    if (radioButton.isSelected()) {
                        txtMeja.setText(radioButton.getText());
                        gas = true;
                    }
                }
            }
            if (gas) {
                pnPs.setVisible(false);
                pnByr.setVisible(true);
                loadDataLay();
            } else {
                lblMeja.setVisible(true);
            }
        } else if (pnByr.isVisible()) {
            pnByr.setVisible(false);
            pnTr.setVisible(true);
            txtByr.textProperty().addListener((observable, oldValue, newValue) -> {
                // Menghapus semua karakter kecuali angka
                String cleanedText = newValue.replaceAll("[^\\d]", "");

                // Mengubah string angka menjadi format mata uang rupiah
                String formattedText = formatRupiah(cleanedText);

                // Mengatur teks yang diformat ke TextField
                txtByr.setText(formattedText);
                calculateKembalian();
            });
        }
    }

    private void calculateKembalian() {
        String bayarFormatted = txtByr.getText().replaceAll("[^\\d]", "");
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

    @FXML
    protected void onClickPrev()
    {
        if(pnTgl.isVisible())
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
            pnPs.setVisible(true);
            pnByr.setVisible(false);
        }
    }
    public void cbPick() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            // Mendapatkan data PS berdasarkan jenis PS yang dipilih
            String psQuery = "SELECT * FROM ms_ps WHERE jenis_ps = '" + ps + "'";
            connection.result = connection.stat.executeQuery(psQuery);

            // Mendapatkan data meja
            String mejaQuery = "SELECT * FROM ms_meja where id_meja NOT IN (SELECT id_meja FROM trs_paket WHERE GETDATE() < tanggal_selesai) AND isDeleted is null;";
            ResultSet mejaResult = connection.stat.executeQuery(mejaQuery);

            // Initialize column and row counters
            int columnCount = 0;
            int rowCount = 0;
            // Initialize column and row counters
            double xOffset = 15.0; // X offset for the first checkbox
            double yOffset = 20.0; // Y offset for the first checkbox

            ToggleGroup toggleGroup = new ToggleGroup(); // Create a ToggleGroup

            while (mejaResult.next()) {
                String id = mejaResult.getString("id_meja");
                String name = mejaResult.getString("merkTV");

                RadioButton checkBox = new RadioButton(name);
                checkBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white");
                checkBox.setToggleGroup(toggleGroup); // Assign the RadioButton to the ToggleGroup

                // Set the position of the checkbox in AnchorPane
                AnchorPane.setTopAnchor(checkBox, yOffset + (rowCount * 90.0)); // Adjust the vertical position as needed
                AnchorPane.setLeftAnchor(checkBox, xOffset + (columnCount * 380 / 3)); // Adjust the horizontal position as needed

                // Disable RadioButton for incompatible meja
                if (!isMejaCompatible(id)) {
                    checkBox.setDisable(true);
                }

                // Add the checkbox to the AnchorPane
                rbContainer.getChildren().add(checkBox);

                // Update column and row counters
                columnCount++;
                if (columnCount == 3) {
                    columnCount = 0;
                    rowCount++;
                }
            }

        } catch (Exception e) {
            System.out.println("Terjadi error saat load data game: " + e);
        }
    }

    private boolean isMejaCompatible(String mejaId) {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            String query = "SELECT * FROM ms_meja WHERE id_meja = '" + mejaId + "' AND id_ps IN (SELECT id_ps FROM ms_ps WHERE jenis_ps = '" + ps + "') AND  isDeleted is null";
            ResultSet result = connection.stat.executeQuery(query);

            return result.next(); // Jika ada hasil, meja kompatibel; jika tidak, meja tidak kompatibel

        } catch (Exception e) {
            System.out.println("Terjadi error saat memeriksa kompatibilitas meja: " + e);
            return false; // Jika terjadi error, asumsikan meja tidak kompatibel
        }
    }
    public void refreshTableLay() {//BUAT LOAD TABLE DARI DATABASE PAKE 2 METHOD refreshTable & loadDate
        try {
            pktList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String tipe = "Member";
            if(txtIdMem.equals("non-member"))
            {
                tipe = "All";
            }
            String query = "SELECT * from ms_paketlayanan where  jenis_ps = '"+ ps + "' AND Tipe_paket = '" + tipe + "' AND isDeleted is null;";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                pktList.add(new Layanan(
                        connection.result.getString("nama_paket"),
                        connection.result.getString("deskripsi"),
                        connection.result.getInt("durasi"),
                        connection.result.getInt("biaya")
                ));
            }
            tbLay.setItems(pktList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    private void loadDataLay() {//LOAD DATANYA
        refreshTableLay();

        pktCol.setCellValueFactory(new PropertyValueFactory<>("namaPaket"));
        hrgCol.setCellValueFactory(new PropertyValueFactory<>("biaya"));
        Callback<TableColumn<Layanan, String>, TableCell<Layanan, String>> cellFoctory = (TableColumn<Layanan, String> param) -> {
            // make cell containing buttons
            final TableCell<Layanan, String> cell = new TableCell<Layanan, String>() {
                private final HBox managebtn = new HBox(); // HBox untuk ikon
                private final Tooltip tooltip = new Tooltip(); // Tooltip untuk detail
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        SVGPath tambah = new SVGPath();
                        SVGPath kurang = new SVGPath();
                        tambah.setContent("M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm6.5 4.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3a.5.5 0 0 1 1 0z");
                        kurang.setContent("M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm2.5 7.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1 0-1z");
                        tambah.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:black;"
                        );
                        kurang.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:black;"
                        );
                        tambah.setOnMouseClicked((MouseEvent event) -> {
                            System.out.println(ps);
                                lynn = tbLay.getSelectionModel().getSelectedItem();
                                total += lynn.getBiaya();
                                txtHarga.setText(Integer.toString(total));
                                ttlDur += lynn.getDurasi();
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            LocalDateTime newDateTime = currentDateTime.plusHours(ttlDur);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            String formattedDateTime = currentDateTime.format(formatter);
                            String formattedEst = newDateTime.format(formatter);
                            txtTgl.setText(formattedDateTime);
                            txtSls.setText(formattedEst);
                                if(txtLay.getText().equals("")) {
                                    txtLay.appendText(lynn.getNamaPaket());
                                }
                                else {
                                    txtLay.appendText(", " + lynn.getNamaPaket());
                                }
                            highlightTextField(txtLay);
                            highlightTextField(txtTgl);
                            highlightTextField(txtSls);
                        });
                        tambah.setOnMouseEntered(event -> {
                            tooltip.setText("Tambah kuantitas"); // Detail saat mengarahkan kursor ke ikon delete
                            Window window = getScene().getWindow(); // Mengambil window dari TableCell
                            tooltip.show(window, event.getScreenX(), event.getScreenY() + 10);
                        });

                        tambah.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                        kurang.setOnMouseClicked((MouseEvent event) -> {
                            Layanan selectedGame = tbLay.getSelectionModel().getSelectedItem();

                            if (selectedGame != null) {
                                String textToRemove = selectedGame.getNamaPaket();
                                String currentText = txtLay.getText();

                                if (currentText.contains(textToRemove)) {
                                    // Find the index of the first occurrence of the text to remove
                                    int index = currentText.indexOf(textToRemove);

                                    // Find the index of the comma before the first occurrence
                                    int commaIndex = currentText.lastIndexOf(",", index);
                                    if (commaIndex == -1) {
                                        commaIndex = 0;
                                    }

                                    // Find the index of the comma after the first occurrence
                                    int nextCommaIndex = currentText.indexOf(",", index);
                                    if (nextCommaIndex == -1) {
                                        nextCommaIndex = currentText.length();
                                    }

                                    // Remove the text between the two commas (including spaces)
                                    String updatedText = currentText.substring(0, commaIndex) + currentText.substring(nextCommaIndex);
                                    txtLay.setText(updatedText.trim());
                                    total -=selectedGame.getBiaya();
                                    ttlDur -= selectedGame.getDurasi();
                                    LocalDateTime currentDateTime = LocalDateTime.now();
                                    LocalDateTime newDateTime = currentDateTime.plusHours(ttlDur);
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    String formattedDateTime = currentDateTime.format(formatter);
                                    String formattedEst = newDateTime.format(formatter);
                                    txtTgl.setText(formattedDateTime);
                                    txtSls.setText(formattedEst);
                                    txtHarga.setText(Integer.toString(total));
                                    highlightTextField(txtLay);
                                    highlightTextField(txtTgl);
                                    highlightTextField(txtSls);
                                }
                            }
                        });

                        kurang.setOnMouseEntered(event -> {
                            tooltip.setText("Kurangi kuantitas"); // Detail saat mengarahkan kursor ke ikon edit
                            Window window = getScene().getWindow(); // Mengambil window dari TableCell
                            tooltip.show(window, event.getScreenX(), event.getScreenY() + 10);
                        });

                        kurang.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                        if(managebtn.getChildren().size() < 2)
                        {
                            managebtn.getChildren().addAll(tambah, kurang);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(tambah, new Insets(2, 2, 0, 3));
                            HBox.setMargin(kurang, new Insets(2, 3, 0, 2));

                        }
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

            };

            return cell;
        };
        //add cell of button edit
        jmlCol.setCellFactory(cellFoctory);
        tbLay.setItems(pktList);
        //Filtered
        FilteredList<Layanan> filteredData = new FilteredList<>(pktList, b-> true);
        searchBarLay.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(Layanan ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (Layanan.getNamaPaket() != null && Layanan.getNamaPaket().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Layanan.getBiaya() != 0 && String.valueOf(Layanan.getBiaya()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Layanan> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbLay.comparatorProperty());
        tbLay.setItems(sortedData);
        tbLay.setItems(sortedData);
    }
    private void highlightTextField(TextField textField) {//TRANSISI PAS EDIT ICON DI KLIK
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), textField);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        fadeTransition.setOnFinished(event -> textField.setStyle("-fx-text-fill: black;"));

        fadeTransition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setImageFromDatabase(fprofil);
        idTr.setText(generateAutoId());
        idKar.setText(generateKarID());

        txtByr.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtByr.setText(formattedText);
        });
        txtHarga.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtHarga.setText(formattedText);
        });


    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_pembelian FROM trs_paket p WHERE p.tanggal_peminjaman  = CONVERT(VARCHAR(8), GETDATE(), 112) ORDER BY id_pembelian DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_pembelian");
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
    public  String getIdMeja(String nmMeja) {
        String autoId = null;

        String lastId = null;
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_meja FROM ms_meja where merkTv ='" + nmMeja + "' AND isDeleted is null";
            connection.result = connection.stat.executeQuery(query);
            // Mendapatkan ID game terakhir
            if (connection.result.next()) {
                lastId = connection.result.getString("id_meja");
                System.out.println(lastId);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    private static String generateNextId(String lastId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);
        String id;
        if (lastId == null) {
            id = "RTL" + formattedDate + "001";
            // Jika belum ada data game dalam tabel, set ID otomatis pertama
            return id;
        } else {
            // Mendapatkan angka dari ID game terakhir
            int lastNumber = Integer.parseInt(lastId.substring(11));

            // Tingkatkan angka ID game terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            String nextId = String.format("%03d", nextNumber);

            return "RTL" + formattedDate + nextId;
        }
    }
    int bayar, kmbl;
    public static void vibrate(TextField anchorPane) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), anchorPane);
        transition.setFromX(0);
        transition.setToX(10);
        transition.setCycleCount(5);
        transition.setAutoReverse(true);
        transition.play();
    }
    @FXML
    protected void simpanOnClick() throws SQLException {
        String idT = idTr.getText();
        String tgl = txtTgl.getText();
        String idK = idKar.getText();
        String idM =null;
        String nama = txtNm.getText();
        if(txtIdMem.getText().equals("non-member"))
        {
            idM = null;
        }
        else {
            idM= txtIdMem.getText();
        }

        String idMj = getIdMeja(txtMeja.getText());
        total = convertCurrencyToInteger(txtHarga.getText());
         bayar = convertCurrencyToInteger(txtByr.getText());
         kmbl = convertCurrencyToInteger(txtKmbl.getText());
        String pkt = txtLay.getText();
        String sls = txtSls.getText();
        if(convertCurrencyToInteger(txtByr.getText())<convertCurrencyToInteger(txtHarga.getText()) || txtByr.getText().isEmpty())
        {
            vibrate(txtByr);
            vibrate(txtKmbl);
            return;
        }

        List<String> categoryList = Arrays.asList(pkt.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);

        Connection con = new Connection();

        try {
                // ID does not exist, perform an insert
                String insertGameQuery = "INSERT INTO trs_paket VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
                insertGameStatement.setString(1, idT);
                insertGameStatement.setString(2, tgl);
                insertGameStatement.setString(3, idK);
                insertGameStatement.setString(4, idM);
                insertGameStatement.setString(5, nama);
                insertGameStatement.setString(6, idMj);
                insertGameStatement.setInt(7, total);
                insertGameStatement.setInt(8, bayar);
                insertGameStatement.setString(9, sls);
                insertGameStatement.executeUpdate();
            // Insert data into dtl_game table
            String insertDetailQuery = "MERGE INTO dtl_layanan AS Target " +
                    "USING (SELECT ?, ?) AS Source (id_paket, id_pembelian) " +
                    "ON (Target.id_paket = Source.id_paket AND Target.id_pembelian = Source.id_pembelian) " +
                    "WHEN MATCHED THEN " +
                    "  UPDATE SET Target.quantity = Target.quantity + 1 " +
                    "WHEN NOT MATCHED THEN " +
                    "  INSERT (id_paket, id_pembelian, quantity) VALUES (Source.id_paket, Source.id_pembelian, 1);";

            PreparedStatement insertDetailStatement = con.conn.prepareStatement(insertDetailQuery);
            for (String categoryId : categoryIds) {
                insertDetailStatement.setString(1, categoryId);
                insertDetailStatement.setString(2, idT);
                insertDetailStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        exportAsset();
        clear();
        pnMember.setVisible(true);
        pnTr.setVisible(false);
        apMem.setVisible(false);
        entrName.setVisible(false);
        btnNon.setStyle("-fx-background-color:rgb(45, 47, 152);");
        btnMem.setStyle("-fx-background-color:rgb(45, 47, 152);");
    }
    public static List<String> getCategoryIds(List<String> categories) throws SQLException {
        List<String> categoryIds = new ArrayList<>();
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT id_paket FROM ms_paketlayanan WHERE nama_paket = ? AND isDeleted is null;";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        // Initialize column and row counters
        for (String category : categories) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Memeriksa apakah ada hasil dari query
            if (resultSet.next()) {
                String categoryId = resultSet.getString("id_paket");
                categoryIds.add(categoryId);
            } else {
                // Kategori tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
                System.out.println("Kategori tidak ditemukan: " + category);
            }
            resultSet.close();
        }
        return categoryIds;
    }
    public void exportTableToPDF(String tableName, String filePath) {
        /*try {
            // Konfigurasi koneksi database
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM trs_pembelian where id_trs = '"+ id+"'";
            connection.result = connection.stat.executeQuery(query);

            // Membuat dokumen PDF
            PdfWriter writer =new PdfWriter();
            PdfDocument document = new PdfDocument(writer);
            Document content = new Document(document);

            // Menambahkan judul
            Paragraph title = new Paragraph(tableName);
            content.add(title);

            // Membuat tabel
            PdfPTable table = new PdfPTable(connection.result.getMetaData().getColumnCount());
            table.setWidthPercentage(100);

            // Menambahkan header tabel
            for (int i = 1; i <= connection.result.getMetaData().getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Paragraph(connection.result.getMetaData().getColumnLabel(i)));
                table.addCell(cell);
            }

            // Menambahkan baris data
            while (connection.result.next()) {
                for (int i = 1; i <= connection.result.getMetaData().getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(new Paragraph(connection.result.getString(i)));
                    table.addCell(cell);
                }
            }

            // Menambahkan tabel ke dokumen PDF
            content.add(table);

            // Menutup dokumen PDF
            content.close();
            document.close();

            System.out.println("PDF created successfully!");

            // Menutup koneksi database
            connection.result.close();
            connection.stat.close();
            connection.conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
                    parameters.put("Total", String.valueOf(total));
                    parameters.put("Cash", String.valueOf(bayar));
                    parameters.put("Change", String.valueOf(kmbl));
                    // Membuat koneksi ke SQL Server
                    java.sql.Connection conn = Connection.getConnect();

                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\trsPaket.jrxml";
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
