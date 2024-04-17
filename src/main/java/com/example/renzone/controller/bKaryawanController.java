package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.Employee;
import com.example.renzone.models.Game;
import com.example.renzone.models.Layanan;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.image.Image;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.nio.ByteBuffer;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class bKaryawanController implements Initializable {
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCncl;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnCncUpd;
    @FXML
    private Button btnSaveUpd;
    @FXML
    AnchorPane btnHolder;
    @FXML
    private Button btnChg;
    @FXML
    private AnchorPane dtPnl;
    @FXML
    private AnchorPane pnUsn;
    @FXML
    private AnchorPane pnOld;
    @FXML
    private AnchorPane firstBtn;
    @FXML
    private AnchorPane secondBtn;
    @FXML
    private AnchorPane thirdBtn;
    @FXML
    private TextField txtIdEm;
    @FXML
    private TextField txtNamaEm;
    @FXML
    private TextField txtAlmtEm;
    @FXML
    private TextField txtNoEm;
    @FXML
    private TextField txtUsn;
    @FXML
    private PasswordField txtPw;
    @FXML
    private PasswordField txtTA;
    @FXML
    private TextField txtUsnUpd;
    @FXML
    private PasswordField txtOldPw;
    @FXML
    private PasswordField txtPwUpd;
    @FXML
    private PasswordField txtTAUpd;
    @FXML
    private TextField txtSearch;
    @FXML
    private Circle imgCon;
    @FXML
    private Pane vldOldPw;
    @FXML
    private Pane vldPwUpd;
    @FXML
    private Pane vldPw;
    @FXML
    private Label lblVlds;
    @FXML
    private Label lblVldsAk;
    @FXML
    private Label lblVldsAkUpd;
    @FXML
    private Label lblVldsUpd;
    @FXML
    TableView<Employee> tblEm;
    @FXML
    TableColumn<Employee, String> idCol;
    @FXML
    TableColumn<Employee, String> namaCol;
    @FXML
    TableColumn<Employee, String> alamatCol;
    @FXML
    TableColumn<Employee, String> pnCol;
    @FXML
    TableColumn<Employee, String> usnCol;
    @FXML
    TableColumn<Employee, Image> ppCol;
    @FXML
    TableColumn<Employee, String> ubhCol;
    @FXML
    protected Button logoutBtn;
    @FXML
    private ImageView imageView;
    private Image image;
    private FileInputStream fis;
    private File file;
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Employee emp = null;
    private Stage stage;
    ObservableList<Employee> empList = FXCollections.observableArrayList();

    String id, nm, almt, telp, usn, pswd;
    Boolean onUpdate = false, isChg = false, chgAcc = false;
    @FXML
    protected void btnNextClick()
    {
        this.id = txtIdEm.getText();
        this.nm = txtNamaEm.getText();
        this.almt = txtAlmtEm.getText();
        this.telp = txtNoEm.getText();
        if (txtIdEm.getText().isEmpty() || txtNamaEm.getText().isEmpty() || txtAlmtEm.getText().isEmpty() || txtNoEm.getText().isEmpty()) {
            lblVlds.setVisible(true);
            return;
        } else {
            lblVlds.setVisible(false);
        }
        dtPnl.setVisible(false);
        pnUsn.setVisible(true);
        pnOld.setVisible(false);
        firstBtn.setVisible(false);
        secondBtn.setVisible(true);
        thirdBtn.setVisible(false);
        lblVlds.setVisible(false);
        txtUsn.textProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println(newValue.length());
            if(newValue.length()<5)
            {
                vldLengths.setVisible(true);
            }
            else
            {
                vldLengths.setVisible(false);
            }
            isUsernameExists(newValue,"");
    });
        txtPw.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.length()<5)
            {
                vldLengths.setVisible(true);
            }
            else
            {
                vldLengths.setVisible(false);
            }
        });

    }
    @FXML
    protected void btnPrevClick()
    {
        dtPnl.setVisible(true);
        pnUsn.setVisible(false);
        pnOld.setVisible(false);
        if(onUpdate)
        {
            firstBtn.setVisible(false);
            secondBtn.setVisible(false);
            thirdBtn.setVisible(true);
        }
        else {
            firstBtn.setVisible(true);
            secondBtn.setVisible(false);
            thirdBtn.setVisible(false);
        }
    }
    @FXML
    protected void btnCnclClick()
    {
        clear();
        awal();
    }
    private void awal()
    {
        dtPnl.setVisible(true);
        pnUsn.setVisible(false);
        pnOld.setVisible(false);
        firstBtn.setVisible(true);
        secondBtn.setVisible(false);
        thirdBtn.setVisible(false);
    }
    @FXML
    protected void btnChgClick()
    {
        this.id = txtIdEm.getText();
        this.nm = txtIdEm.getText();
        this.almt =txtAlmtEm.getText();
        this.telp = txtNoEm.getText();
        if (txtIdEm.getText().isEmpty() || txtNamaEm.getText().isEmpty() || txtAlmtEm.getText().isEmpty() || txtNoEm.getText().isEmpty()) {
            lblVlds.setVisible(true);
            return;
        } else {
            lblVlds.setVisible(false);
        }
        dtPnl.setVisible(false);
        pnUsn.setVisible(false);
        pnOld.setVisible(true);
        firstBtn.setVisible(false);
        secondBtn.setVisible(true);
        thirdBtn.setVisible(false);
        chgAcc = true;
        txtUsnUpd.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.length()<5)
            {
                vldLength.setVisible(true);
            }
            else {
                vldLength.setVisible(false);
            }
            isUsernameExists(newValue, oldUsn);
        });
        txtPwUpd.textProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue.length()<5)
            {
                vldLength.setVisible(true);
            }
            else {
                vldLength.setVisible(false);
            }

        });
    }
    public void clear() {
        txtIdEm.clear();
        txtNamaEm.clear();
        txtAlmtEm.clear();
        txtNoEm.clear();
        txtUsn.clear();
        txtPw.clear();
        txtTA.clear();
        txtUsnUpd.clear();
        txtOldPw.clear();
        txtPwUpd.clear();
        txtTAUpd.clear();
        imgCon.setFill(null);
        onUpdate = false;
        isChg = false;
        chgAcc = false;
        file = null;
        fis = null;
        id = null;
        nm = null;
        almt = null;
        telp = null;
        usn = null;
        pswd = null;
        txtIdEm.setText(generateAutoId());
    }

    @FXML
    protected void pickImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File Gambar", "*.jpg", "*.jpeg", "*.png", "*.gif"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Semua File", "*.*"));
        fileChooser.setTitle("Pilih Gambar");

        // Get the stage from the existing fotoProf ImageView
        Stage stage = (Stage) imgCon.getScene().getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);
        file = selectedFile;
        if(onUpdate && file != null)
        {
             isChg = true;
        }
        if (selectedFile != null) {
            String filePath = selectedFile.toURI().toString();
            Image image = new Image(filePath);
            imgCon.setFill(new ImagePattern(image));
        }
        refreshImg();
    }
    @FXML
    protected void dleteImg() {
        imgCon.setFill(null);
        file = null;
        fis = null;
        isChg = true;
        refreshImg();
    }
    protected void refreshImg() {
        if (imgCon.getFill() == null) {
            imgCon.setVisible(false);
        } else {
            imgCon.setVisible(true);
        }
    }
    String oldUsn = "";
    private void loadDate() {//LOAD DATANYA
        refreshTable();
        idCol.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));
        alamatCol.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        pnCol.setCellValueFactory(new PropertyValueFactory<>("noTelp"));
        usnCol.setCellValueFactory(new PropertyValueFactory<>("namaPengguna"));
        ppCol.setCellValueFactory(new PropertyValueFactory<>("fotoProfil"));
        ppCol.setCellFactory(param -> new TableCell<Employee, Image>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                setGraphic(imageView);
            }
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    imageView.setImage(null);
                } else {
                    imageView.setImage(item);
                }
            }
        });
        //add cell of button edit
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFoctory = (TableColumn<Employee, String> param) -> {
            // make cell containing buttons
            final TableCell<Employee, String> cell = new TableCell<Employee, String>() {
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
                                emp = tblEm.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(tblEm.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Employee");
                                confirmationDialog.setContentText("Are you sure you want to delete this employee?");
                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_karyawan WHERE id_karyawan = '" + emp.getIdKaryawan()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    lblVlds.setVisible(false);
                                    lblVldsAk.setVisible(false);
                                    refreshTable();
                                    loadDate();
                                    clear();
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(bGameController.class.getName()).log(Level.SEVERE, null, ex);
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
                            Employee selectedGame = tblEm.getSelectionModel().getSelectedItem();

                            if (selectedGame != null) {
                                txtIdEm.setText(selectedGame.getIdKaryawan());
                                txtNamaEm.setText(selectedGame.getNamaKaryawan());
                                txtAlmtEm.setText(selectedGame.getAlamat());
                                txtNoEm.setText(selectedGame.getNoTelp());
                                txtUsnUpd.setText(selectedGame.getNamaPengguna());
                                oldUsn = selectedGame.getNamaPengguna();
                                if(selectedGame.getFotoProfil()!=null)
                                {
                                    imgCon.setFill(new ImagePattern(selectedGame.getFotoProfil()));
                                }
                                else {
                                    imgCon.setFill(null);
                                }
                                    refreshImg();

                                highlightTextField(txtIdEm);
                                highlightTextField(txtNamaEm);
                                highlightTextField(txtAlmtEm);
                                highlightTextField(txtNoEm);
                                dtPnl.setVisible(true);
                                pnUsn.setVisible(false);
                                pnOld.setVisible(false);
                                firstBtn.setVisible(false);
                                secondBtn.setVisible(false);
                                thirdBtn.setVisible(true);
                                onUpdate = true;
                                lblVlds.setVisible(false);
                                lblVldsAk.setVisible(false);
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
        tblEm.setItems(empList);

        //Filtered
        FilteredList<Employee> filteredData = new FilteredList<>(empList, b-> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(Employee ->{
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return  true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (Employee.getIdKaryawan() != null && Employee.getIdKaryawan() .toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Employee.getNamaKaryawan() != null && Employee.getNamaKaryawan().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Employee.getAlamat() != null && Employee.getAlamat().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else if (Employee.getNoTelp() != null && Employee.getNoTelp().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (Employee.getNamaPengguna() != null && Employee.getNamaPengguna().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Employee> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblEm.comparatorProperty());
        tblEm.setItems(sortedData);
    }


    private void highlightTextField(TextField textField) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), textField);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        // Apply the blinking effect to the image
        Timeline blinkAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(imgCon.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(500), new KeyValue(imgCon.opacityProperty(), 0.0))
        );
        blinkAnimation.setCycleCount(1); // Blink four times
        blinkAnimation.setOnFinished(event -> {
            imgCon.setOpacity(1.0); // Reset the opacity to fully visible
            textField.setStyle("-fx-text-fill: black;");
        });

        fadeTransition.play();
        blinkAnimation.play();
    }
    public void refreshTable() {
        try {
            empList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_karyawan, nama_karyawan, alamat, no_telp, foto_profil, nama_pengguna FROM ms_karyawan WHERE id_karyawan <> 'OWNER' AND isDeleted is null";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                empList.add(new Employee(
                        connection.result.getString("id_karyawan"),
                        connection.result.getString("nama_karyawan"),
                        connection.result.getString("alamat"),
                        connection.result.getString("no_telp"),
                        getFotoProfilAsImage(connection.result.getBlob("foto_profil")),
                        connection.result.getString("nama_pengguna")
                ));
            }
            tblEm.setItems(empList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data karyawan: " + e);
        }
    }

// Method to convert Blob data to Image
    private Image getFotoProfilAsImage(Blob blobData) {
        if (blobData != null) {
            try (InputStream inputStream = blobData.getBinaryStream()) {
                return new Image(inputStream);
            } catch (SQLException | IOException ex) {
                System.out.println("Error while converting Blob data to Image: " + ex);
            }
        }
        return null;
    }
    public String getPasswordFromDatabase(String id) {
        String password = null;

        try {
            Connection connection = new Connection(); // Assuming Connection class is implemented correctly
            String query = "SELECT kata_sandi FROM ms_karyawan WHERE id_karyawan = ? AND isDeleted is null";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if there is a result and retrieve the password
            if (resultSet.next()) {
                password = resultSet.getString("kata_sandi");
            }

            // Close the resources properly
            resultSet.close();
            preparedStatement.close();
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    @FXML
    private void cekOldPw(Event event) {
        String enteredPassword = txtOldPw.getText();
        String storedPassword = getPasswordFromDatabase(this.id);

        if (!enteredPassword.equals(storedPassword)) {
            vldOldPw.setVisible(true);
        } else {
            vldOldPw.setVisible(false);
        }
    }
    @FXML
    private void cekPwUpd(Event event) {
        String enteredPassword = txtTAUpd.getText();
        String storedPassword = txtPwUpd.getText();
        if (!enteredPassword.equals(storedPassword)) {
            vldPwUpd.setVisible(true);
        } else {
            vldPwUpd.setVisible(false);
        }
    }
    @FXML
    private void cekPw(Event event) {
        String enteredPassword = txtTA.getText();
        String storedPassword = txtPw.getText();
        if (!enteredPassword.equals(storedPassword)) {
            vldPw.setVisible(true);
        } else {
            vldPw.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
        showActiveMember();
        showEmployee();
        showIncome();
        loadIncome();
        loadAssetConditionData();
        loadMemberRegistrationData();
        showMIncome();
        showPrsn();
        loadMemberActive();
        loadMYIncome();
        txtIdEm.setText(generateAutoId());
        txtNoEm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtNoEm.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > 13) {
                    txtNoEm.setText(newValue.substring(0, 13));
                }

            }
        });
        txtNamaEm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Memastikan hanya huruf yang dapat diinputkan
                if (!newValue.matches("[a-zA-Z\\s]*")) {
                    txtNamaEm.setText(newValue.replaceAll("[^a-zA-Z\\s]", ""));
                }
            }
        });
        logoutBtn.setOnMouseClicked(event -> {
            ((Node) event.getSource()).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/login.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Renzone");
            // Menampilkan stage baru dalam mode fullscreen
            stage.setFullScreen(true);
            StageManager.setPrimaryStage(stage);
            // Menampilkan stage baru
            stage.show();
        });
        logoutBtn.setOnMouseEntered(event -> {
            kluar.setStyle("-fx-fill: rgb(180, 119, 0);"); // Ubah warna ke merah saat dihover
        });

        logoutBtn.setOnMouseExited(event -> {
            kluar.setStyle("-fx-fill: white;"); // Kembalikan warna ke putih setelah keluar dari hover
        });

        lblEpf.setOnMouseClicked(event -> {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Owner/updateAccountOwner.fxml"));
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

            // Tambahkan logika Anda di sini
        });
        // Mendapatkan daftar tombol dari btnHolder

    }
    @FXML
    private void saveButtonClicked() {
        String id = txtIdEm.getText();
        String namaKaryawan = txtNamaEm.getText();
        String alamat = txtAlmtEm.getText();
        String noTelp = txtNoEm.getText();
        String cekpsw = txtTA.getText();
        String cekpswU = txtTAUpd.getText();
        // At this point, all fields are filled, and the image is set, so you can proceed with the database insert
        try {
            Connection connection = new Connection();
            if(onUpdate)
            {
                String username = txtUsnUpd.getText();
                String password = txtPwUpd.getText();

                if(chgAcc) {
                    if (id.isEmpty() || namaKaryawan.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || username.isEmpty() || password.isEmpty() || cekpswU.isEmpty() ||  vldPwUpd.isVisible() ||  vldOldPw.isVisible() || vldUsnUpd.isVisible() || vldLength.isVisible()) {
                        lblVldsAkUpd.setVisible(true);
                        return;
                    } else {
                        lblVldsAkUpd.setVisible(false);
                    }
                    if (isChg) {
                        String query = "UPDATE ms_karyawan SET nama_karyawan = ?, alamat = ?, no_telp = ?, foto_profil = ?, nama_pengguna = ?, kata_sandi = ? WHERE id_karyawan = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

                        preparedStatement.setString(1, namaKaryawan);
                        preparedStatement.setString(2, alamat);
                        preparedStatement.setString(3, noTelp);
                        if(file == null)
                        {
                            preparedStatement.setBinaryStream(4, null);
                        }
                        else {
                            fis = new FileInputStream(file);
                            preparedStatement.setBinaryStream(4, fis, file.length());
                        }
                        preparedStatement.setString(5, username);
                        preparedStatement.setString(6, password);
                        preparedStatement.setString(7, id);
                        preparedStatement.executeUpdate();
                    } else {

                        String query = "UPDATE ms_karyawan SET nama_karyawan = ?, alamat = ?, no_telp = ?, nama_pengguna = ?, kata_sandi = ? WHERE id_karyawan = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

                        preparedStatement.setString(1, namaKaryawan);
                        preparedStatement.setString(2, alamat);
                        preparedStatement.setString(3, noTelp);
                        preparedStatement.setString(4, username);
                        preparedStatement.setString(5, password);
                        preparedStatement.setString(6, id);
                        preparedStatement.executeUpdate();
                    }
                    lblVldsAkUpd.setVisible(false);
                }
                else {
                    if (id.isEmpty() || namaKaryawan.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() ) {
                        lblVlds.setVisible(true);
                        return;
                    } else {
                        lblVlds.setVisible(false);
                    }
                    if (isChg) {
                        String query = "UPDATE ms_karyawan SET nama_karyawan = ?, alamat = ?, no_telp = ?, foto_profil = ? WHERE id_karyawan = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

                        preparedStatement.setString(1, namaKaryawan);
                        preparedStatement.setString(2, alamat);
                        preparedStatement.setString(3, noTelp);
                        if(file == null)
                        {
                            preparedStatement.setBinaryStream(4, null);
                        }
                        else {
                            fis = new FileInputStream(file);
                            preparedStatement.setBinaryStream(4, fis, file.length());
                        }
                        preparedStatement.setString(5, id);
                        preparedStatement.executeUpdate();
                    } else {
                        String query = "UPDATE ms_karyawan SET nama_karyawan = ?, alamat = ?, no_telp = ? WHERE id_karyawan = ?";
                        PreparedStatement preparedStatement = connection.conn.prepareStatement(query);

                        preparedStatement.setString(1, namaKaryawan);
                        preparedStatement.setString(2, alamat);
                        preparedStatement.setString(3, noTelp);
                        preparedStatement.setString(4, id);
                        preparedStatement.executeUpdate();
                    }
                    lblVlds.setVisible(false);
                }

            }
            else
            {
                String username = txtUsn.getText();
                String password = txtPw.getText();
                if (id.isEmpty() || namaKaryawan.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || username.isEmpty() || password.isEmpty() || cekpsw.isEmpty()  || vldPw.isVisible()|| vldUsn.isVisible()|| vldLengths.isVisible() ) {
                    lblVldsAk.setVisible(true);
                    return;
                } else {
                    lblVldsAk.setVisible(false);
                }
                String query = "INSERT INTO ms_karyawan (id_karyawan, nama_karyawan, alamat, no_telp, foto_profil, nama_pengguna, kata_sandi) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, namaKaryawan);
                preparedStatement.setString(3, alamat);
                preparedStatement.setString(4, noTelp);

                // Get the image from the circle and convert it to an InputStream
                if(file == null)
                {
                    preparedStatement.setBinaryStream(5, null);
                }
                else {
                    fis = new FileInputStream(file);
                    preparedStatement.setBinaryStream(5, fis, file.length());
                }

                preparedStatement.setString(6, username); // Replace with the actual value for the nama_pengguna field
                preparedStatement.setString(7, password);

                preparedStatement.executeUpdate();
                lblVldsAk.setVisible(false);
                lblVlds.setVisible(false);
            }

        clear();
        awal();
        refreshTable();
        loadDate();
            imgCon.setFill(null); // Clear the image in the circle
        } catch (IOException | SQLException e) {
            // Handle any errors that occurred during the database insert
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while inserting data to the database.");
            alert.showAndWait();
        }
    }


    // Utility method to convert Image to byte array


    public static String generateAutoId() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT id_karyawan\n" +
                    "FROM ms_karyawan\n" +
                    "WHERE id_karyawan <> 'OWNER'\n" +
                    "ORDER BY id_karyawan DESC \n";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_karyawan");
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
            return "KRY001";
        } else {
            // Mendapatkan angka dari ID game terakhir
            int lastNumber = Integer.parseInt(lastId.substring(3));

            // Tingkatkan angka ID game terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("KRY%03d", nextNumber);
        }
    }
    @FXML
    private Label print;
    @FXML
    private Label print1;
    @FXML
    protected void exportAsset() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("noLap", generatNoLapAss());
                    // Membuat koneksi ke SQL Server
                    java.sql.Connection conn = Connection.getConnect();
                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\AssetReport.jrxml";
                    JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                    // Export laporan ke format yang diinginkan (misalnya PDF)
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    // Mengatur zoom level ke 75%
                    viewer.setZoomRatio(0.50F);
                    viewer.setVisible(true);
                    Stage reportStage = new Stage();
                    reportStage.initOwner(firstBtn.getScene().getWindow());
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
        print1.setStyle("-fx-text-fill: rgb(51, 118, 208);");
    }
    public static String generatNoLapAss() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT MAX(no_asset) as no_asset FROM increment_laporan";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("no_asset");
            }

            // Generate ID otomatis berdasarkan ID game terakhir
            autoId = generateNextLap(lastId);

            // Insert the new record with the generated autoId
            String insertQuery = "INSERT INTO increment_laporan  (no_asset)  VALUES ('" + autoId +  "')";
            connection.stat.executeUpdate(insertQuery);
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(autoId);
        return autoId;
    }
    public static String generatNoLapInc() {
        String autoId = null;

        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT MAX(no_income) as no_income FROM increment_laporan ORDER BY no_income DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("no_income");
            }

            // Generate ID otomatis berdasarkan ID game terakhir
            autoId = generateNextLap(lastId);

            // Insert the new record with the generated autoId
            String insertQuery = "INSERT INTO increment_laporan (no_income) VALUES ('"+ autoId + "')";
            connection.stat.executeUpdate(insertQuery);

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return autoId;
    }

    private static String generateNextLap(String lastId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);
        String id;
        if (lastId == null || !lastId.substring(2, 10).equals(formattedDate)) {
            // Jika belum ada data game dalam tabel atau tanggal terakhir tidak sama dengan tanggal saat ini,
            // set ID otomatis pertama dengan angka 1
            id = "01" + formattedDate;
        } else {
            // Mendapatkan angka dari ID game terakhir
            int lastNumber = Integer.parseInt(lastId.substring(0, 2));

            // Tingkatkan angka ID game terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 2 digit angka, misal: 01, 02, dst.
            id = String.format("%02d", nextNumber) + formattedDate;
        }
        return id;
    }

    @FXML
    protected void exportIncome() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("noLap", generatNoLapInc());
                    // Membuat koneksi ke SQL Server
                    java.sql.Connection conn = Connection.getConnect();

                    // Compile template laporan
                    String templatePath = "D:\\coba\\JAVA\\Renzone\\src\\main\\java\\com\\example\\renzone\\report\\IncomeReport.jrxml";
                    JasperReport jasperReport = JasperCompileManager.compileReport(templatePath);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                    // Export laporan ke format yang diinginkan (misalnya PDF)
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    // Mengatur zoom level ke 75%
                    viewer.setZoomRatio(0.50F);
                    viewer.setVisible(true);
                    Stage reportStage = new Stage();
                    reportStage.initOwner(print.getScene().getWindow());

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
        print.setStyle("-fx-text-fill: rgb(51, 118, 208);");

    }
    @FXML
    private Button btnDtl1;
    @FXML
    private Button btnDtl2;
    @FXML
    private Button btnDtl3;
    @FXML
    private Button pane2Up;
    @FXML
    private Button pane3Up;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;

    @FXML
    private Label lblActive;
    @FXML
    protected void onEtrBar2() {
        // Membuat transisi fade in dengan durasi 0.3 detik
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.3), btnDtl2);
        fadeInTransition.setFromValue(0); // Mulai dengan keadaan transparan (alpha = 0)
        fadeInTransition.setToValue(1); // Akhiri dengan keadaan terlihat (alpha = 1)
        fadeInTransition.play(); // Memulai transisi fade in

        btnDtl2.setVisible(true);
        btnDtl2.setOnAction(event -> {
                ScrollTransition.scrollToPane(scrollPane, 1350);
                pane2Up.setVisible(true);
        });
    }
    @FXML
    protected void upToPane1()
    {
        ScrollTransition.scrollToPane(scrollPane, 0);
        pane2Up.setVisible(false);
        pane3Up.setVisible(false);
    }
    @FXML
    protected void onExtBar2() {
        // Membuat transisi fade out dengan durasi 0.3 detik
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.3), btnDtl2);
        fadeOutTransition.setFromValue(1); // Mulai dengan keadaan terlihat (alpha = 1)
        fadeOutTransition.setToValue(0); // Akhiri dengan keadaan transparan (alpha = 0)
        fadeOutTransition.setOnFinished(event -> btnDtl2.setVisible(false)); // Mengatur tombol tidak terlihat setelah transisi selesai
        fadeOutTransition.play(); // Memulai transisi fade out
    }
    @FXML
    protected void onEtrBar3() {
        // Membuat transisi fade in dengan durasi 0.3 detik
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.3), btnDtl3);
        fadeInTransition.setFromValue(0); // Mulai dengan keadaan transparan (alpha = 0)
        fadeInTransition.setToValue(1); // Akhiri dengan keadaan terlihat (alpha = 1)
        fadeInTransition.play(); // Memulai transisi fade in

        btnDtl3.setVisible(true);
        btnDtl3.setOnAction(event -> {
            ScrollTransition.scrollToPane(scrollPane, 2680);
            pane3Up.setVisible(true);
        });
    }

    @FXML
    protected void onExtBar3() {
        // Membuat transisi fade out dengan durasi 0.3 detik
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.3), btnDtl3);
        fadeOutTransition.setFromValue(1); // Mulai dengan keadaan terlihat (alpha = 1)
        fadeOutTransition.setToValue(0); // Akhiri dengan keadaan transparan (alpha = 0)
        fadeOutTransition.setOnFinished(event -> btnDtl3.setVisible(false)); // Mengatur tombol tidak terlihat setelah transisi selesai
        fadeOutTransition.play(); // Memulai transisi fade out
    }
    public class ScrollTransition {
        public static void scrollToPane(ScrollPane scrollPane, double targetY) {
            double currentY = scrollPane.getVvalue() * scrollPane.getContent().getBoundsInLocal().getHeight();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(scrollPane.vvalueProperty(), targetY / scrollPane.getContent().getBoundsInLocal().getHeight()))
            );
            timeline.play();
        }
    }
    private void showActiveMember() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM ms_member WHERE status_anggota = 'active' AND isDeleted is null";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            String cat = Integer.toString(count);
            lblActive.setText(cat);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data active member: " + e);
        }
    }
   @FXML
    protected  Label lblEmployee;
    private void showEmployee() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM ms_karyawan WHERE id_karyawan <> 'OWNER' AND isDeleted is null;";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            String cat = Integer.toString(count);
            lblEmployee.setText(cat);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data total Emp: " + e);
        }
    }
    @FXML
    private Label lblIncome;
    private void showIncome() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT SUM(total_transaksi)\n" +
                    "FROM total_all\n" +
                    "WHERE CONVERT(DATE, tanggal_transaksi) = CONVERT(DATE, GETDATE())\n";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            String cat = Integer.toString(count);
            String formattedIncome = "";

            if (cat.length() >= 4 && cat.length() <= 6) {
                formattedIncome = cat.substring(0, cat.length() - 3) + "K";
            } else if (cat.length() >= 7) {
                formattedIncome = cat.substring(0, cat.length() - 6) + "M";
            } else {
                formattedIncome = cat;
            }
            lblIncome.setText(formattedIncome);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data Income: " + e);
        }
    }
    @FXML
    LineChart<String, Number> lcIncome;
    private void loadIncome()
    {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Set x-axis and y-axis labels
        xAxis.setLabel("Month");
        yAxis.setLabel("Income");

        // Retrieve data from SQL Server table
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Income");

        try {
            // Establish database connection
            Connection conn = new Connection();
            conn.stat = conn.conn.createStatement();

            // Execute SQL query
            String sqlQuery = "SELECT\n" +
                    "    MONTH(tanggal_transaksi) AS bulan,\n" +
                    "    SUM(total_transaksi) AS pendapatan\n" +
                    "FROM (\n" +
                    "    SELECT CONVERT(date, tanggal_peminjaman) AS tanggal_transaksi, biaya AS total_transaksi\n" +
                    "    FROM trs_peminjaman\n" +
                    "\n" +
                    "    UNION ALL\n" +
                    "\n" +
                    "    SELECT CONVERT(date, CONVERT(datetime, tanggal_pembelian, 120)) AS tanggal_transaksi, jumlah_bayar AS total_transaksi\n" +
                    "    FROM trs_paket\n" +
                    "\n" +
                    "    UNION ALL\n" +
                    "\n" +
                    "    SELECT CONVERT(date, tanggal_pengembalian) AS tanggal_transaksi, denda AS total_transaksi\n" +
                    "    FROM trs_pengembalian\n" +
                    "    WHERE denda <> 0\n" +
                    "\n" +
                    "    UNION ALL\n" +
                    "\n" +
                    "    SELECT CONVERT(date, tanggal_mulai) AS tanggal_transaksi, total_biaya AS total_transaksi\n" +
                    "    FROM trs_member\n" +
                    ") AS pendapatan_per_bulan\n" +
                    "GROUP BY MONTH(tanggal_transaksi);\n";

            conn.result = conn.stat.executeQuery(sqlQuery);

            // Iterate over the result set and add data to the series
            while (conn.result.next()) {
                int bulan = conn.result.getInt("bulan");
                int pendapatan = conn.result.getInt("pendapatan");

                String labelBulan = getMonthLabel(bulan); // Metode untuk mendapatkan label bulan berdasarkan angka bulan

                series.getData().add(new XYChart.Data<>(labelBulan, pendapatan));
            }

            // Close database resources
            conn.result.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add series to the line chart
        lcIncome.getData().add(series);
        lcIncome.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");
    }
    private String getMonthLabel(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }
    @FXML
    private PieChart pcAsset;
    @FXML
    private Label lb1;
    @FXML
    private Label lb2;
    @FXML
    private Label lb3;

    public void loadAssetConditionData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String sqlQuery = "SELECT " +
                "SUM(CASE WHEN kondisi_ps = 'Good' THEN 1 ELSE 0 END) + SUM(CASE WHEN kondisi_aksesoris = 'Good' THEN 1 ELSE 0 END) AS total_good, " +
                "SUM(CASE WHEN kondisi_ps = 'Broke' THEN 1 ELSE 0 END) + SUM(CASE WHEN kondisi_aksesoris = 'Broke' THEN 1 ELSE 0 END) AS total_broke " +
                "FROM ms_ps " +
                "FULL JOIN ms_aksesoris ON 1 = 0"; // Dummy join to include both tables

        try {
            // Establish database connection
            com.example.renzone.DBConnection.Connection conn = new com.example.renzone.DBConnection.Connection();
            conn.stat = conn.conn.createStatement();

            // Execute SQL query
            ResultSet resultSet = conn.stat.executeQuery(sqlQuery);

            int totalGood = 0;
            int totalBroke = 0;

            if (resultSet.next()) {
                totalGood = resultSet.getInt("total_good");
                totalBroke = resultSet.getInt("total_broke");
            }

            int totalAssets = totalGood + totalBroke;

            pieChartData.add(new PieChart.Data("Good", totalGood));
            pieChartData.add(new PieChart.Data("Broke", totalBroke));

            // Calculate percentages and add labels to each Data in Pie Chart
            for (PieChart.Data data : pieChartData) {
                double persentase = (data.getPieValue() / totalAssets) * 100;
                String label = String.format("%.2f", persentase) + "% (" + (int) data.getPieValue() + ")";

                data.setName(label);
            }

            // Set data to Pie Chart
            pcAsset.setData(pieChartData);

            lb1.setText("Total Assets: " + totalAssets);
            lb2.setText("Total Good Assets: " + totalGood);
            lb3.setText("Total Broke Assets: " + totalBroke);
            // Close database connection
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private BarChart<String, Number> bcMember;
    private void loadMemberRegistrationData() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Set x-axis and y-axis labels
        xAxis.setLabel("Month");
        yAxis.setLabel("Number of Members");

        // Retrieve data from SQL Server table
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("New Registration");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Renewal");

        try {
            // Establish database connection
            Connection conn = new Connection();
            conn.stat = conn.conn.createStatement();

            // Execute SQL query
            String sqlQuery = "SELECT\n" +
                    "    MONTH(transaction_date) AS month,\n" +
                    "    SUM(CASE WHEN transaction_type = 'Renewal' THEN 1 ELSE 0 END) AS total_renewal,\n" +
                    "    SUM(CASE WHEN transaction_type = 'New Subscription' THEN 1 ELSE 0 END) AS total_new_transaction\n" +
                    "FROM\n" +
                    "    (\n" +
                    "        SELECT \n" +
                    "            tm1.id_trsmember,\n" +
                    "            tm1.id_member,\n" +
                    "            tm1.total_biaya,\n" +
                    "            tm1.tanggal_mulai AS transaction_date,\n" +
                    "            tm1.tanggal_akhir,\n" +
                    "            tm1.id_karyawan,\n" +
                    "            CASE\n" +
                    "                WHEN tm2.id_trsmember IS NOT NULL THEN 'Renewal'\n" +
                    "                ELSE 'New Subscription'\n" +
                    "            END AS transaction_type\n" +
                    "        FROM\n" +
                    "            dbo.trs_member tm1\n" +
                    "        LEFT JOIN\n" +
                    "            dbo.trs_member tm2 ON tm1.id_member = tm2.id_member\n" +
                    "            AND tm2.tanggal_mulai < tm1.tanggal_mulai\n" +
                    "    ) AS subquery\n" +
                    "GROUP BY\n" +
                    "    MONTH(transaction_date)\n";

            conn.result = conn.stat.executeQuery(sqlQuery);

            // Iterate over the result set and add data to the series
            while (conn.result.next()) {
                int bulan = conn.result.getInt("month");
                int jumlahMemberDaftar = conn.result.getInt("total_renewal");
                int jumlahMemberPerpanjang = conn.result.getInt("total_new_transaction");

                String labelBulan = getMonthLabel(bulan); // Metode untuk mendapatkan label bulan berdasarkan angka bulan

                series1.getData().add(new XYChart.Data<>(labelBulan, jumlahMemberDaftar));
                series2.getData().add(new XYChart.Data<>(labelBulan, jumlahMemberPerpanjang));
            }

            // Close database resources
            conn.result.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add series to the bar chart
        bcMember.getData().addAll(series1, series2);
        bcMember.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");

        // Add labels to the data points
        for (XYChart.Series<String, Number> series : bcMember.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                String label = data.getYValue().intValue() + "";
                Tooltip tooltip = new Tooltip(label);
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }
    @FXML
    private Label lblMIncome;
    @FXML
    private Label lblPrsn;

    private void showMIncome() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT SUM(total_transaksi)  FROM transaksi_view WHERE tipe_transaksi = 'Member'";
            connection.result = connection.stat.executeQuery(query);
            int count = 0;
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            String cat = Integer.toString(count);
            String formattedIncome = "";

            if (cat.length() >= 4 && cat.length() <= 6) {
                formattedIncome = cat.substring(0, cat.length() - 3) + "K";
            } else if (cat.length() >= 7) {
                formattedIncome = cat.substring(0, cat.length() - 6) + "M";
            } else {
                formattedIncome = cat;
            }
            lblMIncome.setText(formattedIncome);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data MIncome: " + e);
        }
    }
    private void showPrsn()
    {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT \n" +
                    "    (SUM(CASE WHEN tipe_transaksi = 'Member' THEN total_transaksi ELSE 0 END) / SUM(total_transaksi)) * 100 AS persentase_pendapatan_member\n" +
                    "FROM total_all WHERE MONTH(tanggal_transaksi) = MONTH(GETDATE())\n";
            connection.result = connection.stat.executeQuery(query);
            double count = 0;
            if (connection.result.next()) {
                count = connection.result.getDouble(1);
            }
            String cat = Double.toString(count);
            lblPrsn.setText(cat + "% FROM TOTAL INCOME");
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data Persen: " + e);
        }
    }
    @FXML
    private PieChart pcMember;
    @FXML
    private Label lb11;
    @FXML
    private Label lb21;
    @FXML
    private Label lb31;

    public void loadMemberActive() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String sqlQuery = "SELECT " +
                "COUNT(CASE WHEN status_anggota = 'Active' THEN 1 END) AS member_active, " +
                "COUNT(CASE WHEN status_anggota = 'Inactive' THEN 1 END) AS member_inactive " +
                "FROM ms_member";

        try {
            // Establish database connection
            com.example.renzone.DBConnection.Connection conn = new com.example.renzone.DBConnection.Connection();
            conn.stat = conn.conn.createStatement();

            // Execute SQL query
            ResultSet resultSet = conn.stat.executeQuery(sqlQuery);

            int memberActive = 0;
            int memberInactive = 0;

            if (resultSet.next()) {
                memberActive = resultSet.getInt("member_active");
                memberInactive = resultSet.getInt("member_inactive");
            }

            int totalMembers = memberActive + memberInactive;

            pieChartData.add(new PieChart.Data("Active", memberActive));
            pieChartData.add(new PieChart.Data("Inactive", memberInactive));

            // Calculate percentages and add labels to each Data in Pie Chart
            for (PieChart.Data data : pieChartData) {
                double percentage = (data.getPieValue() / totalMembers) * 100;
                String label = String.format("%.2f", percentage) + "% (" + (int) data.getPieValue() + ")";

                data.setName(label);
            }

            // Set data to Pie Chart
            pcMember.setData(pieChartData);

            lb11.setText("Total Members: " + totalMembers);
            lb21.setText("Active Members: " + memberActive);
            lb31.setText("Inactive Members: " + memberInactive);

            // Close database connection
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Label monthI;
    @FXML
    private Label yearI;
    public void loadMYIncome() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String sqlQuery = "\tSELECT\n" +
                "    SUM(total_transaksi) AS total_income,\n" +
                "    SUM(CASE WHEN MONTH(tanggal_transaksi) = MONTH(GETDATE()) THEN total_transaksi ELSE 0 END) AS income_bulan_ini\n" +
                "FROM\n" +
                "    total_all\n";

        try {
            // Establish database connection
            com.example.renzone.DBConnection.Connection conn = new com.example.renzone.DBConnection.Connection();
            conn.stat = conn.conn.createStatement();

            // Execute SQL query
            ResultSet resultSet = conn.stat.executeQuery(sqlQuery);

            int memberActive = 0;
            int memberInactive = 0;

            if (resultSet.next()) {
                memberActive = resultSet.getInt("income_bulan_ini");
                memberInactive = resultSet.getInt("total_income");
            }
            String cat = convertToRupiah(memberActive);
            monthI.setText(cat);
            String cat2 = convertToRupiah(memberInactive);
            yearI.setText(cat2);

            // Close database connection
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String convertToRupiah(int amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setCurrencySymbol("Rp ");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0", symbols);
        return "Rp." +  format.format(amount);
    }
@FXML
    protected Label lblEpf;
    @FXML
    protected SVGPath kluar;
    @FXML
    private Pane vldUsn;
    @FXML
    private Pane vldUsnUpd;
    public void isUsernameExists(String username, String old) {
        boolean exists = false;
        try {
            Connection connection = new Connection(); // Sesuaikan dengan cara Anda mendapatkan koneksi ke database
            String query = "SELECT COUNT(*) AS count FROM ms_karyawan WHERE nama_pengguna = ? AND nama_pengguna<> ? AND isDeleted is null;";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, old);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                exists = count > 0;
            }

            // Close the resources properly
            resultSet.close();
            preparedStatement.close();
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        if(onUpdate)
        {
            if (exists) {
                vldUsnUpd.setVisible(true);
            } else {
                vldUsnUpd.setVisible(false);
            }
        }
        else {
            if (exists) {
                vldUsn.setVisible(true);
            } else {
                vldUsn.setVisible(false);
            }
        }
    }
    @FXML
    protected Pane vldLength;
    @FXML
    protected Pane vldLengths;





}
