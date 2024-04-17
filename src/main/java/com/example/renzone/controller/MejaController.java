package com.example.renzone.controller;

import com.example.renzone.StageManager;
import com.example.renzone.models.bMeja;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import com.example.renzone.DBConnection.Connection;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MejaController implements Initializable {
    @FXML TextField txtIdT;
    @FXML TextField txtIdPS;
    @FXML TextField txtTV;
    @FXML TextField txtDesk;
    @FXML Button btnCancel;
    @FXML Button btnSave;
    @FXML TextField txtSearch;
    @FXML
    AnchorPane btnHolder;
    @FXML
    Label lbValidasi;
    @FXML
    Label lbValidasiPS;
    @FXML
    TableView<bMeja> tblMeja;
    @FXML TableColumn<bMeja, String> idTabelCol;
    @FXML TableColumn<bMeja, String> psCol;
    @FXML TableColumn<bMeja, String> tvCol;
    @FXML TableColumn<bMeja, String> deskCol;
    @FXML TableColumn<bMeja, String> ubhCol;
    @FXML
    private AnchorPane cbContainer;
    @FXML
    private AnchorPane Pct;
    //TABLE TAIL
    //CONNECTION HEAD
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    bMeja meja = null;
    private Stage stage;
    ObservableList<bMeja> MejaList = FXCollections.observableArrayList();
    String idM, idP, TV, deskripsi;
    @FXML
    private Button crudMeja;
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        crudMeja.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        txtTV.setEditable(true);
        txtDesk.setEditable(true);
        txtIdT.setText(generateAutoId());
        loadDate();
        cbPick();
        lbValidasi.setVisible(false);
        Pct.setVisible(false);
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
                    // Menampilkan stage baru dalam mode fullscreen
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
    public void onClickCtg() {//BUTTON ADD YG POPUP CHECKBOX
        Pct.setVisible(true);
    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_meja FROM ms_meja ORDER BY id_meja DESC ";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_meja");
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
            return "TB001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(2));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("TB%03d", nextNumber);
        }
    }
    @FXML
    protected void handleOkButtonClick(){
        String ps = "";
        for (Node node : cbContainer.getChildren()) {
            if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                if (radioButton.isSelected()) {
                    String noResi = radioButton.getText();
                    ps += noResi + ", ";
                }
            }
        }

        // Remove the trailing comma and whitespace, if any
        if (!ps.isEmpty()) {
            ps = ps.substring(0, ps.length() - 2);
        }else {
            lbValidasiPS.setVisible(true);
            return;
        }
        txtIdPS.setText(ps);
        Pct.setVisible(false);
    }
    @FXML
    protected void handleBtlButtonClick() {
        Pct.setVisible(false);
    }
    private void loadDate() {
        refreshTable();
        idTabelCol.setCellValueFactory(new PropertyValueFactory<>("idMeja"));
        psCol.setCellValueFactory(new PropertyValueFactory<>("idPS"));
        tvCol.setCellValueFactory(new PropertyValueFactory<>("namaTv"));
        deskCol.setCellValueFactory(new PropertyValueFactory<>("desk"));

        //add cell of button edit
        Callback<TableColumn<bMeja, String>, TableCell<bMeja, String>> cellFoctory = (TableColumn<bMeja, String> param) -> {
            // make cell containing buttons
            final TableCell<bMeja, String> cell = new TableCell<bMeja, String>() {
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
                                meja = (bMeja) tblMeja.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(tblMeja.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Table");
                                confirmationDialog.setContentText("Are you sure you want to delete this Table?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_meja WHERE id_meja = '" + meja.getIdMeja()+"'";
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
                            bMeja selectedAksesoris = (bMeja) tblMeja.getSelectionModel().getSelectedItem();

                            if (selectedAksesoris != null) {
                                txtIdT.setText(selectedAksesoris.getIdMeja());
                                String idPS = selectedAksesoris.getIdPS();
                                try {
                                    String noSeri = getNoSeriFromId(idPS); // Mengambil nomor seri berdasarkan idPS
                                    txtIdPS.setText(noSeri);
                                } catch (SQLException e) {
                                    System.out.println("Terjadi kesalahan saat mengambil nomor seri: " + e.getMessage());
                                }
                                txtTV.setText(selectedAksesoris.getNamaTv());
                                txtDesk.setText(selectedAksesoris.getDesk());

                                highlightTextField(txtIdT);
                                highlightTextField(txtIdPS);
                                highlightTextField(txtTV);
                                highlightTextField(txtDesk);

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
        tblMeja.setItems(MejaList);

        //Filtered
        FilteredList<bMeja> filteredData = new FilteredList<>(MejaList, b-> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(bMeja ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (bMeja.getIdMeja() != null && bMeja.getIdMeja().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMeja.getIdPS() != null && bMeja.getIdPS().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMeja.getNamaTv() != null && bMeja.getNamaTv().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMeja.getDesk() != null && bMeja.getDesk().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<bMeja> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblMeja.comparatorProperty());
        tblMeja.setItems(sortedData);
    }

    private String getNoSeriFromId(String idPS) throws SQLException{
        String noSeri = null;
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT no_seri FROM ms_ps WHERE id_ps = ?";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        preparedStatement.setString(1, idPS);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Memeriksa apakah ada hasil dari query
        if (resultSet.next()) {
            noSeri = resultSet.getString("no_seri");
        } else {
            // ID PS tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
            System.out.println("ID PS tidak ditemukan: " + idPS);
        }
        resultSet.close();
        return noSeri;
    }

    private void clear() {
        txtIdT.setText(generateAutoId());
        txtIdPS.clear();
        txtTV.clear();
        txtDesk.clear();
        cbContainer.getChildren().clear();
        cbPick();
        lbValidasi.setVisible(false);
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
            MejaList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_meja], [id_ps], [merkTV], [deskripsi] FROM [ms_meja] WHERE isDeleted is null";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                MejaList.add(new bMeja(
                        connection.result.getString("id_meja"),
                        connection.result.getString("id_ps"),
                        connection.result.getString("merkTV"),
                        connection.result.getString("deskripsi")));
            }
            tblMeja.setItems(MejaList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data aksesoris: " + e);
        }
    }
    @FXML
    protected void btnCancelClick(ActionEvent event) {
        clear();
    }

    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        idM = txtIdT.getText();
        idP = txtIdPS.getText();
        TV = txtTV.getText();
        deskripsi = txtDesk.getText();

        if (idM.isEmpty() || idP.isEmpty() || TV.isEmpty() || deskripsi.isEmpty()) {
            // Salah satu atau lebih field kosong
            lbValidasi.setVisible(true);
            return;
        }

        String PSId = getCategoryId(idP);

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_meja WHERE id_meja = ? AND isDeleted is null";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, idM);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateGameQuery = "UPDATE ms_meja SET id_ps = ?, merkTV = ?, deskripsi = ? WHERE id_meja = ?";
                PreparedStatement updateGameStatement = con.conn.prepareStatement(updateGameQuery);
                updateGameStatement.setString(1, PSId);
                updateGameStatement.setString(2, TV);
                updateGameStatement.setString(3, deskripsi);
                updateGameStatement.setString(4, idM);
                updateGameStatement.executeUpdate();

            } else {
                // ID does not exist, perform an insert
                String insertGameQuery = "INSERT INTO ms_meja VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
                insertGameStatement.setString(1, idM);
                insertGameStatement.setString(2, PSId);
                insertGameStatement.setString(3, TV);
                insertGameStatement.setString(4, deskripsi);
                insertGameStatement.setString(5, null);
                insertGameStatement.executeUpdate();
            }
            clear();
            txtIdT.setText(generateAutoId());
            refreshTable();
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static String getCategoryId(String idPS) throws SQLException {
        String psId = null;
        Connection con = new Connection();
        con.stat = con.conn.createStatement();
        String query = "SELECT id_ps FROM ms_ps WHERE no_seri = ?";
        PreparedStatement preparedStatement = con.conn.prepareStatement(query);
        preparedStatement.setString(1, idPS);
        ResultSet resultSet = preparedStatement.executeQuery();
        // Memeriksa apakah ada hasil dari query
        if (resultSet.next()) {
            psId = resultSet.getString("id_ps");
        } else {
            // Kategori tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
            System.out.println("Kategori tidak ditemukan: " + idPS);
        }
        resultSet.close();
        return psId;
    }


    //Binding DB - CHECKBOX
    public void cbPick() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM [ms_ps] where id_ps NOT IN (SELECT id_ps FROM ms_meja WHERE isDeleted is null)";
            connection.result = connection.stat.executeQuery(query);

            // Initialize column and row counters
            int columnCount = 0;
            int rowCount = 0;

            // Initialize column and row counters
            double xOffset = 5.0; // X offset for the first checkbox
            double yOffset = 5.0; // Y offset for the first checkbox

            ToggleGroup toggleGroup = new ToggleGroup(); // Create a ToggleGroup

            while (connection.result.next()) {
                String idTbl = connection.result.getString("id_ps");
                String noSeri = connection.result.getString("no_seri");
                String deskr = connection.result.getString("deskripsi");
                String hrgSewa = connection.result.getString("harga_sewa");
                String jenPS = connection.result.getString("jenis_ps");

                RadioButton radioButton = new RadioButton(noSeri);
                radioButton.setToggleGroup(toggleGroup); // Assign the RadioButton to the ToggleGroup

                // Set the position of the RadioButton in AnchorPane
                AnchorPane.setTopAnchor(radioButton, yOffset + (rowCount * 50.0)); // Adjust the vertical position as needed
                AnchorPane.setLeftAnchor(radioButton, xOffset + (columnCount * 280 / 3)); // Adjust the horizontal position as needed

                // Add the RadioButton to the AnchorPane
                cbContainer.getChildren().add(radioButton);

                // Update column and row counters
                columnCount++;
                if (columnCount == 3) {
                    columnCount = 0;
                    rowCount++;
                }
            }

            // Add a listener to the ToggleGroup
            toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    RadioButton selectedRadioButton = (RadioButton) newValue;
                    String selectedNoSeri = selectedRadioButton.getText();

                    // Disable other RadioButtons
                    for (Node node : cbContainer.getChildren()) {
                        if (node instanceof RadioButton) {
                            RadioButton radioButton = (RadioButton) node;
                            if (!radioButton.getText().equals(selectedNoSeri)) {
                                radioButton.setSelected(false);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data game: " + e);
        }
    }
}

