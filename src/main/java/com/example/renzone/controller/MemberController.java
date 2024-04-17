package com.example.renzone.controller;
import com.example.renzone.StageManager;
import com.example.renzone.models.bMember;
import com.example.renzone.DBConnection.Connection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MemberController implements Initializable {
    @FXML TextField txtid;
    @FXML TextField txtnama;
    @FXML TextField txtalamat;
    @FXML TextField txtnotelepon;
    @FXML TextField txttanggal;
    @FXML
    private ChoiceBox cmbStatus;
    @FXML Button btnCancel;
    @FXML Button btnSave;

    @FXML
    AnchorPane btnHolder;
    @FXML TextField searchBar;
    @FXML Label lbValidasi;
    @FXML TableView<bMember> tblmember;
    @FXML TableColumn<bMember, String> idColumn;
    @FXML TableColumn<bMember, String> namaColumn;
    @FXML TableColumn<bMember, String> alamatColumn;
    @FXML TableColumn<bMember, String> noteleponColumn;
    @FXML TableColumn<bMember, String> tanggalColumn;
    @FXML TableColumn<bMember, String> statusColumn;
    @FXML TableColumn<bMember, String> ubhCol;

    //TABLE TAIL
    //CONNECTION HEAD
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    bMember member = null;
    private Stage stage;
    ObservableList<bMember> MemberList = FXCollections.observableArrayList();
    String idmember, namamember, alamatmember, noteleponmember, tanggalmember, statusmember;
    @FXML
    private Button crudMember;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        crudMember.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        txtid.setText(generateAutoId());
        loadDate();
        cmbStatus.getItems().addAll("Active", "Inactive");
        cmbStatus.setValue("Active");
        cmbStatus.setDisable(true);
        txtnama.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Memastikan hanya huruf yang dapat diinputkan
                if (!newValue.matches("[a-zA-Z\\s]*")) {
                    txtnama.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
                }
            }
        });
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txttanggal.setText(currentDateTime.format(formatter));
        txtnotelepon.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtnotelepon.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 13) {
                    txtnotelepon.setText(newValue.substring(0, 13));
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
                    // Membuat FXMLLoader dan memuat file FXML
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

    @FXML
    protected void btnCancelClick(ActionEvent event){
        clear();


    }
    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        idmember = txtid.getText();
        namamember = txtnama.getText();
        alamatmember = txtalamat.getText();
        noteleponmember = txtnotelepon.getText();
        tanggalmember = txttanggal.getText();

        statusmember = cmbStatus.getValue().toString();


        if (idmember.isEmpty() || namamember.isEmpty() || alamatmember.isEmpty() || noteleponmember.isEmpty() || tanggalmember.isEmpty()) {
            lbValidasi.setVisible(true);
            return;
        }



        /*List<String> categoryList = Arrays.asList(kat.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);*/

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_member WHERE id_member = ?";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, idmember);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (btnSave.getText().equals("Save")) {
                // ID exists, perform an update instead of an insert
                String updateAksesorisQuery = "UPDATE ms_member SET nama_member = ?, alamat = ?, no_telp = ?, tgl_daftar = ?, status_anggota = ? WHERE id_member = ?";
                PreparedStatement updateAksesorisStatement = con.conn.prepareStatement(updateAksesorisQuery);
                updateAksesorisStatement.setString(1, namamember);
                updateAksesorisStatement.setString(2, alamatmember);
                updateAksesorisStatement.setString(3, noteleponmember);
                updateAksesorisStatement.setString(4, tanggalmember);
                updateAksesorisStatement.setString(5, statusmember);
                updateAksesorisStatement.setString(6, idmember);
                updateAksesorisStatement.executeUpdate();

            } else if(btnSave.getText().equals("Transaction")){
                    String insertmembers = "DELETE FROM bntu_member";
                    PreparedStatement insertmemberStatements = con.conn.prepareStatement(insertmembers);
                    insertmemberStatements.executeUpdate();
                    String insertmember = "INSERT INTO bntu_member VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertmemberStatement = con.conn.prepareStatement(insertmember);
                    insertmemberStatement.setString(1, idmember);
                    insertmemberStatement.setString(2, namamember);
                    insertmemberStatement.setString(3, alamatmember);
                    insertmemberStatement.setString(4, noteleponmember);
                    insertmemberStatement.setString(5, tanggalmember);
                    insertmemberStatement.setString(6, statusmember);

                    insertmemberStatement.executeUpdate();
                    switchToTr(event);
            }
            clear();
            txtid.setText(generateAutoId());
            refreshTable();
            loadDate();
            loadDate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_member FROM ms_member ORDER BY id_member DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_member");
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
    @FXML
    private void switchToTr(ActionEvent event) throws IOException {
        // Close the previous stage
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/Transaksi.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Transaksi_Controller tc = loader.getController();
        tc.pnBtn.setVisible(false);
        tc.setAfterMember(true);
        // Inisialisasi stage sebelum penggunaannya
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(scene);
        StageManager.setPrimaryStage(stage);

/*        dsbCustController dashboardController = loader.getController();
        dashboardController.setName(name);*/

        stage.show();
    }

    private static String generateNextId(String lastId) {
        if (lastId == null) {
            // Jika belum ada data game dalam tabel, set ID otomatis pertama
            return "MBR001";
        } else {
            // Mendapatkan angka dari ID aksesoris terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID aksesoris terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("MBR%03d", nextNumber);
        }
    }
    public void clear(){
        txtnama.clear();
        txtalamat.clear();
        txtnotelepon.clear();
        txttanggal.clear();
        btnSave.setText("Transaction");
        cmbStatus.setValue("Active");
        cmbStatus.setDisable(true);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        txttanggal.setText(currentDateTime.format(formatter));
        lbValidasi.setVisible(false);
        txtid.setText(generateAutoId());
    }
    private void loadDate() {
        refreshTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idM"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaM"));
        alamatColumn.setCellValueFactory(new PropertyValueFactory<>("alamatM"));
        noteleponColumn.setCellValueFactory(new PropertyValueFactory<>("notelpM"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalM"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusM"));


        //add cell of button edit
        Callback<TableColumn<bMember, String>, TableCell<bMember, String>> cellFoctory = (TableColumn<bMember, String> param) -> {
            // make cell containing buttons
            final TableCell<bMember, String> cell = new TableCell<bMember, String>() {
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
                                member = (bMember) tblmember.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(tblmember.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Aksesoris");
                                confirmationDialog.setContentText("Are you sure you want to delete this accessories?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_member WHERE id_member = '" + member.getIdM()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    loadDate();
                                    clear();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
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
                            bMember selectedAksesoris = (bMember) tblmember.getSelectionModel().getSelectedItem();

                            if (selectedAksesoris != null) {
                                txtid.setText(selectedAksesoris.getIdM());
                                txtnama.setText(selectedAksesoris.getNamaM());
                                txtalamat.setText(selectedAksesoris.getAlamatM());
                                txtnotelepon.setText(selectedAksesoris.getNotelpM());
                                txttanggal.setText(selectedAksesoris.getTanggalM());
                                cmbStatus.setValue(selectedAksesoris.getStatusM());
                                cmbStatus.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    // Logika yang ingin Anda lakukan ketika nilai cmbStatus berubah
                                    if (newValue.toString().equals("Active")) {
                                        // Panggil method untuk memeriksa status_anggota pada ms_member
                                        if (cekStatusAnggota("Inactive")) {
                                            btnSave.setText("Transaction");
                                        } else {
                                            btnSave.setText("Save");
                                        }
                                    }
                                    else {
                                        btnSave.setText("Save");
                                    }
                                });


                                highlightTextField(txtid);
                                highlightTextField(txtnama);
                                highlightTextField(txtalamat);
                                highlightTextField(txtnotelepon);
                                highlightTextField(txttanggal);
                                highlightComboBox(cmbStatus);

                                btnSave.setText("Save");
                                cmbStatus.setDisable(false);
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
        tblmember.setItems(MemberList);

        //Filtered
        FilteredList<bMember> filteredData = new FilteredList<>(MemberList, b-> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(bMember ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (bMember.getNamaM() != null && bMember.getNamaM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMember.getAlamatM() != null && bMember.getAlamatM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMember.getNotelpM() != null && bMember.getNotelpM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }  else if (bMember.getIdM() != null && bMember.getIdM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMember.getTanggalM() != null && bMember.getTanggalM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMember.getStatusM() != null && bMember.getStatusM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });
        SortedList<bMember> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblmember.comparatorProperty());
        tblmember.setItems(sortedData);
    }
    private boolean cekStatusAnggota(String status) {
        // Kode untuk melakukan query ke tabel ms_member dan memeriksa status_anggota
        // Anda perlu mengganti bagian ini dengan implementasi sesuai dengan database Anda
        // Misalnya, menggunakan kelas Connection yang telah Anda berikan

        // Contoh sederhana
        // Pada contoh ini, kita hanya mengembalikan nilai status yang sama dengan parameter status
        // Anda perlu menggantinya dengan query sesuai dengan struktur tabel ms_member pada database Anda

        try {
            // Mendapatkan koneksi dari kelas Connection
            java.sql.Connection connection = Connection.getConnect();

            // Query SELECT status_anggota dari tabel ms_member
            String query = "SELECT status_anggota FROM ms_member where id_member = '" + txtid.getText() +  "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Cek setiap baris pada hasil query
            while (resultSet.next()) {
                String statusAnggota = resultSet.getString("status_anggota");
                if (statusAnggota.equals(status)) {
                    return true;
                }
            }

            // Menutup statement dan resultSet
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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
    private void highlightComboBox(ChoiceBox<String> comboBox) {//TRANSISI PAS EDIT ICON DI KLIK
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
            MemberList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_member], [nama_member], [alamat], [no_telp], [tgl_daftar], [status_anggota] FROM [ms_member] WHERE isDeleted is null";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                MemberList.add(new bMember(
                        connection.result.getString("id_member"),
                        connection.result.getString("nama_member"),
                        connection.result.getString("alamat"),
                        connection.result.getString("no_telp"),
                        connection.result.getString("tgl_daftar"),
                        connection.result.getString("status_anggota")));
            }
            tblmember.setItems(MemberList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data aksesoris: " + e);
        }
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

            // Set the owner of the popup stage as the primary stage (fullscreen form)
            stage.initOwner(StageManager.getPrimaryStage());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
