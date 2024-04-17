package com.example.renzone.controller;
import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.bAksesoris;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

public class AksesorisController implements Initializable {
        @FXML
        TextField txtIdAk;
        @FXML TextField txtNamaAk;
        @FXML ComboBox cmbJenis;
        @FXML ComboBox cbKondisiAk;
        @FXML TextField txtHargaAk;
        @FXML
        Button btnCancel;
        @FXML Button btnSave;
        @FXML TextField txtSearch;
    @FXML
    AnchorPane btnHolder;
    private static final Pattern VALID_INPUT_PATTERN = Pattern.compile("\\d*");
        @FXML Label lbValidasi;
        @FXML TableView<bAksesoris> aksesorisTableView;
        @FXML TableColumn<bAksesoris, String> idAkCol;
        @FXML TableColumn<bAksesoris, String> namaAkCol;
        @FXML TableColumn<bAksesoris, String> hargaSewaCol;
        @FXML TableColumn<bAksesoris, String> jenisAkCol;
        @FXML TableColumn<bAksesoris, String> kondisiAkCol;
        @FXML TableColumn<bAksesoris, String> ubhCol;
        //TABLE TAIL
        //CONNECTION HEAD
        String query = null;
        java.sql.Connection connection = null ;
        PreparedStatement preparedStatement = null ;
        ResultSet resultSet = null ;
        bAksesoris aksesoris = null;
        private Stage stage;
        ObservableList<bAksesoris> AksesorisList = FXCollections.observableArrayList();
        String idA, namaA, kondA, jenis;
        int hrgSewaA;
    @FXML
    private Button crudAksesoris;

        public void initialize(URL url, ResourceBundle resourceBundle) {
            crudAksesoris.setStyle(
                    "-fx-background-color: rgb(152, 107, 34);"
            );
            txtHargaAk.textProperty().addListener((observable, oldValue, newValue) -> {
                // Menghapus semua karakter kecuali angka
                String cleanedText = newValue.replaceAll("[^\\d]", "");

                // Mengubah string angka menjadi format mata uang rupiah
                String formattedText = formatRupiah(cleanedText);

                // Mengatur teks yang diformat ke TextField
                txtHargaAk.setText(formattedText);
            });
            txtIdAk.setText(generateAutoId());
            loadDate();
            cmbJenis.getItems().add("HDMI Cable");
            cmbJenis.getItems().add("Controller");
            cmbJenis.getItems().add("Adaptor");
            cbKondisiAk.getItems().add("Good");
            cbKondisiAk.getItems().add("Broke");
            cbKondisiAk.setValue("Good");
            cbKondisiAk.setDisable(true);
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

                        }Parent root = loader.load();

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

    @FXML
    protected void btnCancelClick(ActionEvent event){
            clear();
    }
    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        idA = txtIdAk.getText();
        namaA = txtNamaAk.getText();
        jenis = cmbJenis.getValue().toString();
        kondA = cbKondisiAk.getValue().toString();

        String hargaAkText = txtHargaAk.getText().trim();

        if (idA.isEmpty() || namaA.isEmpty() || jenis == null || kondA.isEmpty() || hargaAkText.isEmpty()) {
            lbValidasi.setVisible(true);
            return;
        }

        try {
            hrgSewaA = convertCurrencyToInteger(hargaAkText);
        } catch (NumberFormatException e) {
            lbValidasi.setVisible(true);
            return;
        }

        /*List<String> categoryList = Arrays.asList(kat.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);*/

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_aksesoris WHERE id_aksesoris = ?";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, idA);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateAksesorisQuery = "UPDATE ms_aksesoris SET nama_aksesoris = ?, harga_sewa = ?, jenis_aksesoris = ?, kondisi_aksesoris = ? WHERE id_aksesoris = ?";
                PreparedStatement updateAksesorisStatement = con.conn.prepareStatement(updateAksesorisQuery);
                updateAksesorisStatement.setString(1, namaA);
                updateAksesorisStatement.setInt(2, hrgSewaA);
                updateAksesorisStatement.setString(3, jenis);
                updateAksesorisStatement.setString(4, kondA);
                updateAksesorisStatement.setString(5, idA);
                updateAksesorisStatement.executeUpdate();

            } else {
                // ID does not exist, perform an insert
                String insertAksesorisQuery = "INSERT INTO ms_aksesoris VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertAksesorisStatement = con.conn.prepareStatement(insertAksesorisQuery);
                insertAksesorisStatement.setString(1, idA);
                insertAksesorisStatement.setString(2, namaA);
                insertAksesorisStatement.setInt(3, hrgSewaA);
                insertAksesorisStatement.setString(4, jenis);
                insertAksesorisStatement.setString(5, kondA);

                insertAksesorisStatement.executeUpdate();
            }
            clear();
            txtIdAk.setText(generateAutoId());
            refreshTable();
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_aksesoris FROM ms_aksesoris ORDER BY id_aksesoris DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_aksesoris");
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
            return "ACS001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("ACS%03d", nextNumber);
        }
    }
    public void clear(){
        txtIdAk.clear();
        txtNamaAk.clear();
        txtHargaAk.clear();
        cmbJenis.setValue("");
        cbKondisiAk.setValue("Good");
        cbKondisiAk.setDisable(true);
        lbValidasi.setVisible(false);
        txtIdAk.setText(generateAutoId());
    }
    private void loadDate() {
        refreshTable();
        idAkCol.setCellValueFactory(new PropertyValueFactory<>("idAk"));
        namaAkCol.setCellValueFactory(new PropertyValueFactory<>("namaAk"));
        hargaSewaCol.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        jenisAkCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        kondisiAkCol.setCellValueFactory(new PropertyValueFactory<>("kondisi"));

        //add cell of button edit
        Callback<TableColumn<bAksesoris, String>, TableCell<bAksesoris, String>> cellFoctory = (TableColumn<bAksesoris, String> param) -> {
            // make cell containing buttons
            final TableCell<bAksesoris, String> cell = new TableCell<bAksesoris, String>() {
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
                                aksesoris = (bAksesoris) aksesorisTableView.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(aksesorisTableView.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Aksesoris");
                                confirmationDialog.setContentText("Are you sure you want to delete this accessories?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_aksesoris WHERE id_aksesoris = '" + aksesoris.getIdAk()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    loadDate();
                                    clear();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(AksesorisController.class.getName()).log(Level.SEVERE, null, ex);
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
                            bAksesoris selectedAksesoris = (bAksesoris) aksesorisTableView.getSelectionModel().getSelectedItem();

                            if (selectedAksesoris != null) {
                                txtIdAk.setText(selectedAksesoris.getIdAk());
                                txtNamaAk.setText(selectedAksesoris.getNamaAk());
                                cmbJenis.setValue(selectedAksesoris.getJenis());
                                cbKondisiAk.setValue(selectedAksesoris.getKondisi());
                                txtHargaAk.setText(trimLastThreeCharacters(selectedAksesoris.getHargaSewa()));
                                highlightTextField(txtIdAk);
                                highlightTextField(txtNamaAk);
                                highlightTextField(txtHargaAk);
                                highlightComboBox(cmbJenis);
                                highlightComboBox(cbKondisiAk);
                                cbKondisiAk.setDisable(false);
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
        aksesorisTableView.setItems(AksesorisList);

        //Filtered
        FilteredList<bAksesoris> filteredData = new FilteredList<>(AksesorisList, b-> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(bAksesoris ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (bAksesoris.getIdAk() != null && String.valueOf(bAksesoris.getIdAk()).indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bAksesoris.getNamaAk() != null && bAksesoris.getNamaAk().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bAksesoris.getHargaSewa() != null && String.valueOf(bAksesoris.getHargaSewa()).indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bAksesoris.getKondisi() != null && bAksesoris.getKondisi().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else if (bAksesoris.getJenis() != null && bAksesoris.getJenis().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<bAksesoris> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(aksesorisTableView.comparatorProperty());
        aksesorisTableView.setItems(sortedData);
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
    private void highlightComboBox(ComboBox<String> comboBox) {
        TextField textField = comboBox.getEditor();

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), textField);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        fadeTransition.setOnFinished(event -> textField.setStyle("-fx-text-fill: black;"));

        fadeTransition.play();
    }


    public void refreshTable() {
        try {
            AksesorisList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_aksesoris], [nama_aksesoris], [harga_sewa], [jenis_aksesoris], [kondisi_aksesoris] FROM [ms_aksesoris]";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                AksesorisList.add(new bAksesoris(
                        connection.result.getString("id_aksesoris"),
                        connection.result.getString("nama_aksesoris"),
                        convertToCurrency(connection.result.getInt("harga_sewa")),
                        connection.result.getString("jenis_aksesoris"),
                        connection.result.getString("kondisi_aksesoris")));
            }
            aksesorisTableView.setItems(AksesorisList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data aksesoris: " + e);
        }
    }
    public String convertToCurrency(int number) {
        NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("id", "ID"));
        String currency = format.format(number);
        return currency.replaceAll("Rp", "Rp. ");
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

    public  String trimLastThreeCharacters(String currencyString) {
        if (currencyString.length() <= 3) {
            return currencyString;
        } else {
            return currencyString.substring(0, currencyString.length() - 3);
        }
    }


}
