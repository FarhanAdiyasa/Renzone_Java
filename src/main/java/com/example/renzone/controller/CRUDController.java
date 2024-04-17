package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.baKategori;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.stream.Collectors;

public class CRUDController implements Initializable {

    @FXML
    TextField txtID;

    @FXML
    TextField txtNama;
    @FXML
    AnchorPane btnHolder;
    @FXML
    Button btnBatal;
    @FXML
    Button btnSave;

    @FXML
    TextField txtCari;

    @FXML TableColumn<baKategori, String> ubhCol;

    @FXML
    TableView<baKategori> kategoriTableView;

    @FXML
    TableColumn<baKategori, String> IDKategori;
    @FXML
    TableColumn<baKategori, String> NamaKategori;

     @FXML Label lbValidasi;

    String query = null;
    java.sql.Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    baKategori kategori = null;
    private Stage stage;
    ObservableList<baKategori> KategoriList = FXCollections.observableArrayList();
    String idKG, namaKG;
    @FXML
    private Button das;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        das.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        txtID.setText(generateAutoId());
        loadDate();
        txtNama.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Memastikan hanya huruf yang dapat diinputkan
                if (!newValue.matches("[a-zA-Z\\s]*")) {
                    txtNama.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
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
    }

    public static String generateAutoId() {
        String autoId = null;

        try {
           Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_kategori FROM ms_kategorigame ORDER BY id_kategori DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_Kategori");
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
            return "CG001";
        } else {
            // Mendapatkan angka dari ID Kategori terakhir
            int lastNumber = Integer.parseInt(lastId.substring(2));

            // Tingkatkan angka ID Kategori terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("CG%03d", nextNumber);
        }
    }

    @FXML
    protected void btnBatalClicked(ActionEvent event) {
        clear();
    }

    public void clear() {
       // txtID.clear();
        txtNama.clear();
        lbValidasi.setVisible(false);
        txtID.setText(generateAutoId());
    }

    @FXML
    private void btnSaveClicked(ActionEvent event) throws SQLException {
        idKG = txtID.getText();
        namaKG = txtNama.getText();

        if (idKG.isEmpty() || namaKG.isEmpty()) {
            // Salah satu atau lebih field kosong
            lbValidasi.setVisible(true);
            return;
        }

        /*List<String> categoryList = Arrays.asList(kat.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);*/

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_kategorigame WHERE id_kategori = ?";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, idKG);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateKategoriQuery = "UPDATE ms_kategorigame SET nama_kategori = ? WHERE id_kategori = ?";
                PreparedStatement updateKategoriStatement = con.conn.prepareStatement(updateKategoriQuery);
                updateKategoriStatement.setString(1, namaKG);
                updateKategoriStatement.setString(2, idKG);
                updateKategoriStatement.executeUpdate();

            } else {
                // ID does not exist, perform an insert
                String insertKategoriQuery = "INSERT INTO ms_kategorigame VALUES (?,?)";
                PreparedStatement insertKategoriStatement = con.conn.prepareStatement(insertKategoriQuery);
                insertKategoriStatement.setString(1, idKG);
                insertKategoriStatement.setString(2, namaKG);

                insertKategoriStatement.executeUpdate();
            }
            clear();
            txtID.setText(generateAutoId());
            refreshTable();
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDate() {
        refreshTable();
        IDKategori.setCellValueFactory(new PropertyValueFactory<>("idK"));
        NamaKategori.setCellValueFactory(new PropertyValueFactory<>("namaK"));

        kategoriTableView.setItems(KategoriList);

        //add cell of button edit
        Callback<TableColumn<baKategori, String>, TableCell<baKategori, String>> cellFoctory = (TableColumn<baKategori, String> param) -> {
            // make cell containing buttons
            final TableCell<baKategori, String> cell = new TableCell<baKategori, String>() {
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
                                kategori = (baKategori) kategoriTableView.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(kategoriTableView.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Kategori");
                                confirmationDialog.setContentText("Are you sure you want to delete this game?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_kategorigame WHERE id_kategori = '" + kategori.getIdK() + "'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    loadDate();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(CRUDController.class.getName()).log(Level.SEVERE, null, ex);
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
                            baKategori selectedKategori = (baKategori) kategoriTableView.getSelectionModel().getSelectedItem();

                            if (selectedKategori != null) {
                                txtID.setText(selectedKategori.getIdK());
                                txtNama.setText(selectedKategori.getNamaK());


                                highlightTextField(txtID);
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
                        if (managebtn.getChildren().size() < 2) {
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
        kategoriTableView.setItems(KategoriList);

        //Filtered
        FilteredList<baKategori> filteredData = new FilteredList<>(KategoriList, b-> true);
        txtCari.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(baKategori ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (baKategori.getNamaK() != null && baKategori.getNamaK().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }

                return false;
            });
        });

        SortedList<baKategori> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(kategoriTableView.comparatorProperty());
        kategoriTableView.setItems(sortedData);
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
    public void refreshTable() {
        try {
            KategoriList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_kategori], [nama_kategori] FROM [ms_kategorigame]";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                KategoriList.add(new baKategori(
                        connection.result.getString("id_kategori"),
                        connection.result.getString("nama_kategori")
                        ));
                kategoriTableView.setItems(KategoriList);
            }
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

}


