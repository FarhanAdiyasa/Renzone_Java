package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.bLayanan;

import com.example.renzone.DBConnection.Connection;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class bLayananController implements Initializable {
    @FXML TextField txtId;
    @FXML TextField txtNama;
    @FXML
    ComboBox cmbTipe;
    @FXML
    AnchorPane btnHolder;
    @FXML TextField txtdeskripsi;
    @FXML TextField txtDurasi;
    @FXML TextField txtBiaya;
    @FXML ComboBox cmbJenis;
    @FXML Button btnsimpan;
    @FXML Button btnbatal;
    @FXML TextField txtCari;

    @FXML Label lbValidasi;
    @FXML TableView<bLayanan> layananTableView;
    @FXML TableColumn<bLayanan,String> idlayanan;
    @FXML TableColumn<bLayanan, String> namapaket;
    @FXML TableColumn<bLayanan,String> tipepaket;
    @FXML TableColumn<bLayanan, String> deskripsi;
    @FXML TableColumn<bLayanan, Double> durasi;
    @FXML TableColumn<bLayanan, Double> biaya;
    @FXML TableColumn<bLayanan, String> jenis;
    @FXML TableColumn<bLayanan, String> ubhCol;

    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    bLayanan paket = null;
    private Stage stage;
    ObservableList<bLayanan> Paketlist = FXCollections.observableArrayList();
    String IdPaket, NamaPaket,Deskripsi,Tpe,Jeniss;
    int Durasii, Biayaa;
    @FXML
    private Button CRUDPaket;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        CRUDPaket.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        txtBiaya.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtBiaya.setText(formattedText);
        });
        loadDate();
        txtId.setText(generateAutoId());
        cmbTipe.getItems().add("Member");
        cmbTipe.getItems().add("All");
        cmbJenis.getItems().add("Playstation 2");
        cmbJenis.getItems().add("Playstation 3");
        cmbJenis.getItems().add("Playstation 4");
        cmbJenis.getItems().add("Playstation 5");

        cmbJenis.setValue("");
        cmbTipe.setValue("");
        loadDate();
        txtDurasi.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtDurasi.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        // Mendapatkan daftar tombol dari btnHolder
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

    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_paket FROM ms_paketlayanan ORDER BY id_paket DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_paket");
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
            return "SVC001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("SVC%03d", nextNumber);
        }
    }
    @FXML
    protected void btnBatalClicked(ActionEvent event) {
        clear();
    }
    @FXML
    private void btnSaveClicked(ActionEvent event) throws SQLException {
        IdPaket = txtId.getText();
        NamaPaket = txtNama.getText();
        Tpe = cmbTipe.getValue().toString();

        Deskripsi = txtdeskripsi.getText();
        Durasii = (txtDurasi.getText().isEmpty()) ? 0 : convertCurrencyToInteger(txtDurasi.getText());

        Biayaa =(txtBiaya.getText().isEmpty()) ? 0 : convertCurrencyToInteger(txtBiaya.getText());

        Jeniss = cmbJenis.getValue().toString();

        if (IdPaket.isEmpty() || NamaPaket.isEmpty() || Tpe.isEmpty() || Deskripsi.isEmpty() || Durasii == 0|| Biayaa == 0 || Jeniss.isEmpty()) {
            // Salah satu atau lebih field kosong
            lbValidasi.setVisible(true);
            return;
        }

        /*List<String> categoryList = Arrays.asList(kat.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);*/

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_paketlayanan WHERE id_paket = ? AND isDeleted is null;";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, IdPaket);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);




            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateAksesorisQuery = "UPDATE ms_paketlayanan SET nama_paket = ?, Tipe_paket = ?, deskripsi = ?, durasi = ?, biaya = ?, Jenis_ps = ? WHERE id_paket = ?";
                PreparedStatement updateAksesorisStatement = con.conn.prepareStatement(updateAksesorisQuery);
                updateAksesorisStatement.setString(1, NamaPaket);
                updateAksesorisStatement.setString(2, Tpe);
                updateAksesorisStatement.setString(3, Deskripsi);
                updateAksesorisStatement.setInt(4, Durasii);
                updateAksesorisStatement.setInt(5, Biayaa);
                updateAksesorisStatement.setString(6, Jeniss);
                updateAksesorisStatement.setString(7, IdPaket);

                updateAksesorisStatement.executeUpdate();

            } else {
                // ID does not exist, perform an insert
                String insertLayananQuery = "INSERT INTO ms_paketlayanan VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertAksesorisStatement = con.conn.prepareStatement(insertLayananQuery);
                insertAksesorisStatement.setString(1, IdPaket);
                insertAksesorisStatement.setString(2, NamaPaket);
                insertAksesorisStatement.setString(3, Tpe);
                insertAksesorisStatement.setString(4, Deskripsi);
                insertAksesorisStatement.setInt(5, Durasii);
                insertAksesorisStatement.setInt(6, Biayaa);
                insertAksesorisStatement.setString(7, Jeniss);
                insertAksesorisStatement.setString(8, null);
                insertAksesorisStatement.executeUpdate();
            }
            clear();
            txtId.setText(generateAutoId());
            refreshTable();
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear(){
        txtNama.clear();
        txtdeskripsi.clear();
        txtDurasi.clear();
        cmbTipe.setValue("");
        txtBiaya.clear();
        cmbJenis.setValue("");
        lbValidasi.setVisible(false);
        txtId.setText(generateAutoId());
    }
    private void loadDate() {
        refreshTable();
        idlayanan.setCellValueFactory(new PropertyValueFactory<>("Id"));
        namapaket.setCellValueFactory(new PropertyValueFactory<>("Nama"));
        tipepaket.setCellValueFactory(new PropertyValueFactory<>("Tipe"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("Deskripsi"));
        durasi.setCellValueFactory(new PropertyValueFactory<>("Durasi"));
        biaya.setCellValueFactory(new PropertyValueFactory<>("Biaya"));
        jenis.setCellValueFactory(new PropertyValueFactory<>("JenisP"));


        //add cell of button edit
        Callback<TableColumn<bLayanan, String>, TableCell<bLayanan, String>> cellFoctory = (TableColumn<bLayanan, String> param) -> {
            // make cell containing buttons
            final TableCell<bLayanan, String> cell = new TableCell<bLayanan, String>() {
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

                        SVGPath deleteIcon = new SVGPath();
                        SVGPath editIcon = new SVGPath();
                        deleteIcon.setContent("M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z");
                        editIcon.setContent("M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z");
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                paket = layananTableView.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(layananTableView.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Game");
                                confirmationDialog.setContentText("Are you sure you want to delete this game?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_paketlayanan WHERE id_paket = '" + paket.getId()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    clear();
                                    refreshTable();
                                    loadDate();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(bLayananController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        deleteIcon.setOnMouseEntered(event -> {
                            tooltip.setText("Hapus Data"); // Detail saat mengarahkan kursor ke ikon delete
                            Window window = getScene().getWindow(); // Mengambil window dari TableCell
                            tooltip.show(window, event.getScreenX(), event.getScreenY() + 10);
                        });

                        deleteIcon.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            bLayanan selectedpaket = layananTableView.getSelectionModel().getSelectedItem();

                            if (selectedpaket != null) {

                                txtId.setText(selectedpaket.getId());
                                txtNama.setText(selectedpaket.getNama());
                                cmbTipe.setValue(selectedpaket.getTipe());
                                txtdeskripsi.setText(selectedpaket.getDeskripsi());
                                txtDurasi.setText(Integer.toString(selectedpaket.getDurasi()));
                                txtBiaya.setText(trimLastThreeCharacters(selectedpaket.getBiaya()));
                                cmbJenis.setValue(selectedpaket.getJenisP());


                                highlightTextField(txtId);
                                highlightTextField(txtNama);
                                highlightComboBox(cmbTipe);
                                highlightTextField(txtdeskripsi);
                                highlightTextField(txtDurasi);
                                highlightTextField(txtBiaya);
                                highlightComboBox(cmbJenis);
                            }
                        });
                        editIcon.setOnMouseEntered(event -> {
                            tooltip.setText("Ubah Data"); // Detail saat mengarahkan kursor ke ikon edit
                            Window window = getScene().getWindow(); // Mengambil window dari TableCell
                            tooltip.show(window, event.getScreenX(), event.getScreenY() + 10);
                        });

                        editIcon.setOnMouseExited(event -> {
                            tooltip.hide();
                        });
                        if(managebtn.getChildren().size() < 2)
                        {
                            managebtn.getChildren().addAll(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        }
                        setGraphic(managebtn);
                        setText(null);

                    }
                }

            };

            return cell;
        };
        ubhCol.setCellFactory(cellFoctory);
        layananTableView.setItems(Paketlist);

        //Filtered
        FilteredList<bLayanan> filteredData = new FilteredList<>(Paketlist, b-> true);
        txtCari.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(paket ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (paket.getNama() != null && paket.getNama().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (paket.getTipe() != null && paket.getTipe().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (paket.getId() != null && paket.getId().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }
                else if (paket.getDeskripsi() != null && paket.getDeskripsi().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (paket.getDurasi() != 0 && String.valueOf(paket.getDurasi()).toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (paket.getBiaya() != null && paket.getBiaya().toLowerCase().indexOf(searchKeyword) > -1)  {
                    return true;
                }
                else if (paket.getJenisP() != null && paket.getJenisP().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<bLayanan> sortedData = new SortedList<bLayanan>(filteredData);
        sortedData.comparatorProperty().bind(layananTableView.comparatorProperty());
        layananTableView.setItems(sortedData);
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
    private void highlightComboBox(ComboBox<String> comboBox) {//TRANSISI PAS EDIT ICON DI KLIK
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), comboBox);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        // Set the style when the fade transition finishes
        fadeTransition.setOnFinished(event -> comboBox.setStyle("-fx-text-fill: black;"));

        // Play the fade transition
        fadeTransition.play();
    }

    public void refreshTable() {
        try {
            Paketlist.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_paket],[nama_paket],[Tipe_paket],[deskripsi],[durasi],[biaya],[Jenis_ps] FROM [ms_paketlayanan] WHERE isDeleted is null;";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                Paketlist.add(new bLayanan(
                        connection.result.getString("id_paket"),
                        connection.result.getString("nama_paket"),
                        connection.result.getString("Tipe_paket"),
                        connection.result.getString("deskripsi"),
                        connection.result.getInt("durasi"),
                        convertToCurrency(connection.result.getInt("biaya")),
                        connection.result.getString("Jenis_ps")));
                layananTableView.setItems(Paketlist);
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }
    public String convertToCurrency(int number) {
        NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("id", "ID"));
        String currency = format.format(number);
        return currency.replaceAll("Rp", "Rp. ");
    }
    public int convertCurrencyToInteger(String currencyText) {
        // Hapus karakter non-digit dari teks (misalnya "Rp. 10.000" menjadi "10000")
        String numericText = currencyText.replaceAll("[^\\d]", "");

        // Konversi teks numerik menjadi angka integer
        int intValue = Integer.parseInt(numericText);

        return intValue;
    }

    public  String trimLastThreeCharacters(String currencyString) {
        if (currencyString.length() <= 3) {
            return currencyString;
        } else {
            return currencyString.substring(0, currencyString.length() - 3);
        }
    }



}