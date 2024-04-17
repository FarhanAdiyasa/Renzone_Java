package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Game;
import com.example.renzone.models.Ps;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class bPsController implements Initializable {

    @FXML
    private AnchorPane cbContainer;

    @FXML
    private AnchorPane pct;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNo;
    @FXML
    private TextField txtDes;
    @FXML
    private TextField txtHarga;
    @FXML
    private ChoiceBox<String> cbKondisi;
    @FXML
    private ChoiceBox<String> cbJenis;



    @FXML
    private Label lblGame;
    @FXML
    private TextField txtNama;
    @FXML
    private Label lbKosong;

    @FXML
    TableView<Ps> tblPs;
    @FXML
    private AnchorPane btnHolder;
    @FXML
    TableColumn<Ps, String> idCol;
    @FXML
    TableColumn<Ps, String> nmCol;
    @FXML
    TableColumn<Ps, String> desCol;
    @FXML
    TableColumn<Ps, String> tglCol;
    @FXML
    TableColumn<Ps, String> katCol;
    @FXML
    TableColumn<Ps, String> konCol;
    @FXML
    TableColumn<Ps, String> ubhCol;
    @FXML
    TableColumn<Ps, String> ubhCol1;
    @FXML
    private TextField searchBar;
    @FXML
    private Button boardPs;
    String id, no, des, jns,nama,kond;
    int hrg;
    String query = null;

    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Ps ps = null;
    private Stage stage;
    //CONNECTION HEAD



    ObservableList<Ps> psList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardPs.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        cbJenis.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbPick();
        });

        pct.setVisible(false);
        txtId.setText(generateAutoId());
        cbJenis.getItems().add("Playstation 2");
        cbJenis.getItems().add("Playstation 3");
        cbJenis.getItems().add("Playstation 4");
        cbJenis.getItems().add("Playstation 5");
        cbKondisi.getItems().add("Good");
        cbKondisi.getItems().add("Broke");
        cbKondisi.setDisable(true);


// Menetapkan item default yang dipilih
        cbJenis.setValue("");
        cbKondisi.setValue("Good");
        loadDate();
        // force the field to be numeric only
        txtNo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("-?\\d*")) {
                    txtNo.setText(newValue.replaceAll("[^-\\d]", ""));
                }
            }
        });
        cbJenis.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Playstation 2")) {
                txtHarga.setText("Rp. 100.000");
            }else if (newValue.equals("Playstation 3")) {
                txtHarga.setText("Rp. 150.000");
            }else if (newValue.equals("Playstation 4")) {
                txtHarga.setText("Rp. 200.000");
            }else if (newValue.equals("Playstation 5")) {
                txtHarga.setText("Rp. 250.000");
            }
        });
        txtHarga.textProperty().addListener((observable, oldValue, newValue) -> {
            // Menghapus semua karakter kecuali angka
            String cleanedText = newValue.replaceAll("[^\\d]", "");

            // Mengubah string angka menjadi format mata uang rupiah
            String formattedText = formatRupiah(cleanedText);

            // Mengatur teks yang diformat ke TextField
            txtHarga.setText(formattedText);
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
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Menangani kesalahan saat memuat file FXML
                }
            });
        }
    }
    public int convertToNumber(String text) {
        String numberString = text.replaceAll("[^0-9]", "");
        int number = Integer.parseInt(numberString);
        return number;
    }

    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_ps FROM ms_ps ORDER BY id_ps DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_ps");
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
            return "PS001";
        } else {
            // Mendapatkan angka dari ID game terakhir
            int lastNumber = Integer.parseInt(lastId.substring(2));

            // Tingkatkan angka ID game terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("PS%03d", nextNumber);
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
        formattedText = formattedText.replaceAll("Rp", "Rp.");

        return formattedText;
    }

    public void cbPick() {
        try {
            Connection connection = new Connection(); // Membuat objek koneksi
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM ms_game where jenis_ps = '" +cbJenis.getValue().toString()  + "'";
            connection.result = connection.stat.executeQuery(query);
            cbContainer.getChildren().clear();
            // Initialize column and row counters
            int columnCount = 0;
            int rowCount = 0;

            // Initialize offsets
            double xOffset = 5.0; // X offset for the first checkbox
            double yOffset = 5.0; // Y offset for the first checkbox

            while (connection.result.next()) {
                String id = connection.result.getString("id_game");
                String name = connection.result.getString("nama_game");
                String des = connection.result.getString("deskripsi_game");
                String tgl = connection.result.getString("tanggal_rilis");

                CheckBox checkBox = new CheckBox(name);

                // Set the position of the checkbox in AnchorPane
                AnchorPane.setTopAnchor(checkBox, yOffset + (rowCount * 50.0)); // Adjust the vertical position as needed
                AnchorPane.setLeftAnchor(checkBox, xOffset + (columnCount * 280 / 3)); // Adjust the horizontal position as needed

                // Step 5: Update the database when the checkboxes are changed
                /* checkBox.setOnAction(event -> updateCheckboxStatus(id, isCheckedProperty.get())); */

                // Add the checkbox to the AnchorPane
                cbContainer.getChildren().add(checkBox);

                // Update column and row counters
                columnCount++;
                if (columnCount == 3) {
                    columnCount = 0;
                    rowCount++;
                }
            }

            // Close the database connection
            connection.result.close();
            connection.stat.close();
            connection.conn.close();

        } catch (Exception e) {
            System.out.println("Terjadi error saat load data game: " + e);
        }
    }

    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        id = txtId.getText();
        no = txtNo.getText();
        des = txtDes.getText();
        String hargaText = txtHarga.getText();
        hargaText = hargaText.replaceAll("\\s+", ""); // Menghapus semua spasi putih
        int hrg = convertToNumber(hargaText);
        jns = cbJenis.getValue().toString();
        kond = cbKondisi.getValue().toString();
        nama = "";
        for (Node node : cbContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    String teksCheckbox = checkBox.getText();
                    nama += teksCheckbox + ",";
                }
            }
        }

// Menghapus koma terakhir jika ada
        if (nama.length() > 0) {
            nama = nama.substring(0, nama.length() - 1);
        }
        if (id.isEmpty() || no.isEmpty() || des.isEmpty() || hrg==0 || jns.isEmpty() || nama.isEmpty() || kond.isEmpty()) {
            // Salah satu atau lebih field kosong
            lbKosong.setVisible(true);
            return;
        }
        List<String> gameList = Arrays.asList(nama.split(",\\s*"));
        List<String> gameIds = getgameIds(gameList);
        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_ps WHERE id_ps = ?";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, id);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateGameQuery = "UPDATE ms_ps SET no_seri = ?, deskripsi = ?, harga_sewa = ?, jenis_ps = ?, kondisi_ps=? WHERE id_ps = ?";
                PreparedStatement updateGameStatement = con.conn.prepareStatement(updateGameQuery);
                updateGameStatement.setString(1, no);
                updateGameStatement.setString(2, des);
                updateGameStatement.setInt(3, hrg);
                updateGameStatement.setString(4, jns);
                updateGameStatement.setString(5, kond);
                updateGameStatement.setString(6, id);
                updateGameStatement.executeUpdate();

                String deleteDetailQuery = "DELETE FROM dtl_ps WHERE id_ps = ?";
                PreparedStatement deleteDetailStatement = con.conn.prepareStatement(deleteDetailQuery);
                deleteDetailStatement.setString(1, id);
                deleteDetailStatement.executeUpdate();

            } else {
                // ID does not exist, perform an insert
                String insertGameQuery = "INSERT INTO ms_ps VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
                insertGameStatement.setString(1, id);
                insertGameStatement.setString(2, no);
                insertGameStatement.setString(3, des);
                insertGameStatement.setInt(4, hrg);
                insertGameStatement.setString(5, jns);
                insertGameStatement.setString(6, kond);
                insertGameStatement.executeUpdate();

            }
            String insertDetailQuery = "INSERT INTO dtl_ps VALUES (?, ?)";
            PreparedStatement insertDetailStatement = con.conn.prepareStatement(insertDetailQuery);

            for (String categoryId : gameIds) {
                insertDetailStatement.setString(2, id);
                insertDetailStatement.setString(1, categoryId);
                insertDetailStatement.executeUpdate();
            }
            clear();
            txtId.setText(generateAutoId());
            refreshTable();
            loadDate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static List<String> getgameIds(List<String> games) throws SQLException {
        List<String> gameIds = new ArrayList<>();
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT id_game FROM ms_game WHERE nama_game = ?";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        // Initialize column and row counters
        for (String game : games) {
            preparedStatement.setString(1, game);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Memeriksa apakah ada hasil dari query
            if (resultSet.next()) {
                String gameId = resultSet.getString("id_game");
                gameIds.add(gameId);
            } else {
                System.out.println("game tidak ditemukan: " + game);
            }
            resultSet.close();
        }
        return gameIds;
    }

    @FXML
    protected void btnClearClick(ActionEvent event) {
        clear();
    }
    public void clear()
    {
        txtId.setText(generateAutoId());
        txtNo.clear();
        txtDes.clear();
        txtHarga.clear();
        cbJenis.setValue("");
        txtNama.clear();
        cbKondisi.setValue("Good");
        cbKondisi.setDisable(true);
        deselectAllCheckboxes();
        lbKosong.setVisible(false);
    }

    @FXML
    public void onClickCtg() {//BUTTON ADD YG POPUP CHECKBOX
        if(pct.isVisible())
        {
            pct.setVisible(false);
        }
        else {
            pct.setVisible(true);
        }
    }

    @FXML
    protected void handleOkButtonClick(){
        int count = 0;
        for (Node node : cbContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    count++;
                }
            }
        }

        String cat = Integer.toString(count);

        if (cat.equals("0")) {
            lblGame.setVisible(true);
            return;
        }

        txtNama.setText(cat + "    game");
        pct.setVisible(false);
    }


    @FXML
    protected void handleBtlButtonClick() {
        pct.setVisible(false);
    }
    //BUTTON CHECK BOX TAIL

    private void loadDate() {//LOAD DATANYA
        refreshTable();
        idCol.setCellValueFactory(new PropertyValueFactory<>("idPs"));
        nmCol.setCellValueFactory(new PropertyValueFactory<>("noSeri"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        tglCol.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        katCol.setCellValueFactory(new PropertyValueFactory<>("jenisPs"));
        konCol.setCellValueFactory(new PropertyValueFactory<>("kondisi"));
        ubhCol.setCellValueFactory(new PropertyValueFactory<>("totalGame"));
        //add cell of button edit
        Callback<TableColumn<Ps, String>, TableCell<Ps, String>> cellFoctory = (TableColumn<Ps, String> param) -> {
            // make cell containing buttons
            // make cell containing buttons
            final TableCell<Ps, String> cell = new TableCell<Ps, String>() {
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
                                ps = tblPs.getSelectionModel().getSelectedItem();
                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(tblPs.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Game");
                                confirmationDialog.setContentText("Are you sure you want to delete this ps?");
                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_ps WHERE id_ps = '" + ps.getIdPs()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    loadDate();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(bPsController.class.getName()).log(Level.SEVERE, null, ex);
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
                            Ps selectedGame = tblPs.getSelectionModel().getSelectedItem();

                            if (selectedGame != null) {
                                txtId.setText(selectedGame.getIdPs());
                                txtNo.setText(selectedGame.getNoSeri());
                                txtDes.setText(selectedGame.getDeskripsi());
                                txtHarga.setText(selectedGame.getHargaSewa());
                                cbJenis.setValue(selectedGame.getJenisPs());
                                cbKondisi.setValue(selectedGame.getKondisi());
                                cbKondisi.setDisable(false);
                                txtNama.setText(String.valueOf(selectedGame.getTotalGame() + " game"));
                                deselectAllCheckboxes();
                                selectCheckboxes(selectedGame.getIdPs());
                                highlightTextField(txtId);
                                highlightTextField(txtNo);
                                highlightTextField(txtDes);
                                highlightTextField(txtHarga);
                                highlightTextField(cbJenis);
                                highlightTextField(cbKondisi);
                                highlightTextField(txtNama);
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
        ubhCol1.setCellFactory(cellFoctory);
        tblPs.setItems(psList);

        //Filtered
        FilteredList<Ps> filteredData = new FilteredList<>(psList, b-> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(Ps ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (Ps.getNoSeri() != null && Ps.getNoSeri().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Ps.getDeskripsi() != null && Ps.getDeskripsi().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Ps.getIdPs() != null && Ps.getIdPs().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else if (Ps.getKondisi() != null && Ps.getKondisi().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }
                else if (Ps.getHargaSewa() != null && Ps.getHargaSewa().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Ps.getJenisPs() != null && Ps.getJenisPs().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Ps> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblPs.comparatorProperty());
        tblPs.setItems(sortedData);
    }
    public int convertCurrencyToInteger(String currencyText) {
        // Hapus karakter non-digit dari teks (misalnya "Rp. 10.000" menjadi "10000")
        String numericText = currencyText.replaceAll("[^\\d]", "");

        // Konversi teks numerik menjadi angka integer
        int intValue = Integer.parseInt(numericText);

        return intValue;
    }

    private void highlightTextField(ChoiceBox cbJenis) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), cbJenis);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        fadeTransition.setOnFinished(event -> cbJenis.setStyle("-fx-text-fill: black;"));

        fadeTransition.play();
    }

    public void refreshTable() {//BUAT LOAD TABLE DARI DATABASE PAKE 2 METHOD refreshTable & loadDate
        try {
            psList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT ps.id_ps, ps.no_seri, ps.deskripsi, ps.harga_sewa, ps.jenis_ps, ps.kondisi_ps, COUNT(dg.id_game) AS jumlah_game "
                    + "FROM ms_ps ps "
                    + "LEFT JOIN dtl_ps dg ON ps.id_ps = dg.id_ps "
                    + "GROUP BY ps.id_ps, ps.no_seri, ps.deskripsi, ps.harga_sewa, ps.jenis_ps, ps.kondisi_ps";

            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                psList.add(new Ps(
                        connection.result.getString("id_ps"),
                        connection.result.getString("no_seri"),
                        connection.result.getString("deskripsi"),
                        convertToCurrency(connection.result.getInt("harga_sewa")),
                        connection.result.getString("jenis_ps"),
                        connection.result.getString("kondisi_ps"),
                        connection.result.getInt("jumlah_game")
                ));
            }

            tblPs.setItems(psList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
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
    public String convertToCurrency(int number) {
        NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("id", "ID"));
        String currency = format.format(number);
        return currency.replaceAll("Rp", "Rp. ");
    }
    private void deselectAllCheckboxes() {
        for (Node node : cbContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }
    private void selectCheckboxes(String idG) {
        List<String> categories = getAllCategories(idG);

        for (Node node : cbContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                // Memeriksa kecocokan teks checkbox dengan kategori
                if (categories.contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }
            }
        }
    }

    public List<String> getAllCategories(String idG) {
        List<String> categories = new ArrayList<>();

        try {
            // Establish connection to the database
            Connection conn = new Connection();

            // Create SQL statement
            String sql = "SELECT nama_game FROM ms_game WHERE id_game IN (SELECT id_game FROM dtl_ps WHERE id_ps = '" + idG + "')";
            conn.stat = conn.conn.createStatement();

            // Execute the query
            conn.result = conn.stat.executeQuery(sql);

            // Retrieve category names from the result set
            while (conn.result.next()) {
                String categoryName = conn.result.getString("nama_game");
                categories.add(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

}
