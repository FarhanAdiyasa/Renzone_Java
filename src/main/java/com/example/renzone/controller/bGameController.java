package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.curPlay;
import com.example.renzone.models.Game;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class bGameController  implements Initializable {
    @FXML
    private Button boardGame;

    @FXML
    private Label lbKosong;
    @FXML
    private Label katgrLbl;
    @FXML
    private Pane containCb;
    @FXML
    private BorderPane mainPane;
    @FXML
    private AnchorPane cbContainer;
    @FXML
    private AnchorPane Pct;
    @FXML
    private AnchorPane btnHolder;
    String id, nm, des, kat, tgl, jns;

    //TABLE HEAD
    //Kepentingan TABLE
     @FXML
    private TextField txtId;
    @FXML
    private TextField txtNm;
    @FXML
    private ChoiceBox cbJenis;
    @FXML
    private TextField txtDes;
    @FXML
    private DatePicker txtTgl;
    @FXML
    private TextField txtKat;
    @FXML
    private TextField searchBar;
    //Table
    @FXML
    TableView<Game> tblGame;
    @FXML
    TableColumn<Game, String> idCol;
    @FXML
    TableColumn<Game, String> nmCol;
    @FXML
    TableColumn<Game, String> desCol;
    @FXML
    TableColumn<Game, String> tglCol;
    @FXML
    TableColumn<Game, String> typeCol;
    @FXML
    TableColumn<Game, String> katCol;
    @FXML
    TableColumn<Game, String> ubhCol;
    //TABLE TAIL
    //CONNECTION HEAD
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Game game = null;
    private Stage stage;
    //CONNECTION HEAD

    ObservableList<Game> gameList = FXCollections.observableArrayList();
    //---//
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGame.setStyle(
                "-fx-background-color: rgb(152, 107, 34);"
        );
        txtId.setText(generateAutoId());
        loadDate();
        cbPick();
        cbJenis.getItems().add("Playstation 2");
        cbJenis.getItems().add("Playstation 3");
        cbJenis.getItems().add("Playstation 4");
        cbJenis.getItems().add("Playstation 5");
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

                    }Parent root = loader.load();

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
            String query = "SELECT id_game FROM ms_game ORDER BY id_game DESC";
            connection.result = connection.stat.executeQuery(query);

            // Mendapatkan ID game terakhir
            String lastId = null;
            if (connection.result.next()) {
                lastId = connection.result.getString("id_game");
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
            return "GM001";
        } else {
            // Mendapatkan angka dari ID game terakhir
            int lastNumber = Integer.parseInt(lastId.substring(2));

            // Tingkatkan angka ID game terakhir
            int nextNumber = lastNumber + 1;

            // Format ID otomatis dengan 3 digit angka, misal: GM001, GM002, dst.
            return String.format("GM%03d", nextNumber);
        }
    }
//Binding DB - CHECKBOX
    public void cbPick() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM [ms_kategorigame]";
            connection.result = connection.stat.executeQuery(query);
            // Initialize column and row counters
            int columnCount = 0;
            int rowCount = 0;
            // Initialize column and row counters
            double xOffset = 5.0; // X offset for the first checkbox
            double yOffset = 5.0; // Y offset for the first checkbox

            while (connection.result.next()) {
                String id = connection.result.getString("id_kategori");
                String name = connection.result.getString("nama_kategori");

                CheckBox checkBox = new CheckBox(name);
                // Set the position of the checkbox in AnchorPane
                AnchorPane.setTopAnchor(checkBox, yOffset + (rowCount * 50.0)); // Adjust the vertical position as needed
                AnchorPane.setLeftAnchor(checkBox, xOffset + (columnCount *280/3)); // Adjust the horizontal position as needed

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

        } catch (Exception e) {
            System.out.println("Terjadi error saat load data game: " + e);
        }
    }
    //MAIN BUTTON HEAD
    @FXML
    protected void btnSaveClick(ActionEvent event) throws SQLException {
        id = txtId.getText();
        nm = txtNm.getText();
        des = txtDes.getText();
        kat = txtKat.getText();
        if(cbJenis.getValue() != null)
        {
            jns = cbJenis.getValue().toString();
        }


        if (id.isEmpty() || nm.isEmpty() || des.isEmpty() || txtTgl.getValue() == null || kat.isEmpty() || jns.isEmpty()) {
            // Salah satu atau lebih field kosong
            lbKosong.setVisible(true);
            return;
        }

        List<String> categoryList = Arrays.asList(kat.split(",\\s*"));
        List<String> categoryIds = getCategoryIds(categoryList);

        tgl = txtTgl.getValue().toString();

        Connection con = new Connection();

        try {
            // Check if the ID already exists in the database
            String checkIdQuery = "SELECT COUNT(*) FROM ms_game WHERE id_game = ?";
            PreparedStatement checkIdStatement = con.conn.prepareStatement(checkIdQuery);
            checkIdStatement.setString(1, id);
            ResultSet resultSet = checkIdStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // ID exists, perform an update instead of an insert
                String updateGameQuery = "UPDATE ms_game SET nama_game = ?, deskripsi_game = ?, tanggal_rilis = ?, jenis_ps = ? WHERE id_game = ?";
                PreparedStatement updateGameStatement = con.conn.prepareStatement(updateGameQuery);
                updateGameStatement.setString(1, nm);
                updateGameStatement.setString(2, des);
                updateGameStatement.setString(3, tgl);
                updateGameStatement.setString(4, jns);
                updateGameStatement.setString(5, id);
                updateGameStatement.executeUpdate();

                // Delete existing category associations
                String deleteDetailQuery = "DELETE FROM dtl_game WHERE id_game = ?";
                PreparedStatement deleteDetailStatement = con.conn.prepareStatement(deleteDetailQuery);
                deleteDetailStatement.setString(1, id);
                deleteDetailStatement.executeUpdate();
            } else {
                // ID does not exist, perform an insert
                String insertGameQuery = "INSERT INTO ms_game VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertGameStatement = con.conn.prepareStatement(insertGameQuery);
                insertGameStatement.setString(1, id);
                insertGameStatement.setString(2, nm);
                insertGameStatement.setString(3, des);
                insertGameStatement.setString(4, tgl);
                insertGameStatement.setString(5, jns);
                insertGameStatement.executeUpdate();
            }

            // Insert data into dtl_game table
            String insertDetailQuery = "INSERT INTO dtl_game VALUES (?, ?)";
            PreparedStatement insertDetailStatement = con.conn.prepareStatement(insertDetailQuery);

            for (String categoryId : categoryIds) {
                insertDetailStatement.setString(1, id);
                insertDetailStatement.setString(2, categoryId);
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
        //AUTO INSERT dtl_game HEAD
        public static List<String> getCategoryIds(List<String> categories) throws SQLException {
            List<String> categoryIds = new ArrayList<>();
            Connection con = new Connection();
            con.stat = con.conn.createStatement();
            String query = "SELECT id_kategori FROM ms_kategorigame WHERE nama_kategori = ?";
            PreparedStatement preparedStatement = con.conn.prepareStatement(query);
            // Initialize column and row counters
            for (String category : categories) {
                preparedStatement.setString(1, category);
                ResultSet resultSet = preparedStatement.executeQuery();
                // Memeriksa apakah ada hasil dari query
                if (resultSet.next()) {
                    String categoryId = resultSet.getString("id_kategori");
                    categoryIds.add(categoryId);
                } else {
                    // Kategori tidak ditemukan, dapatkan penanganan kesalahan yang sesuai
                    System.out.println("Kategori tidak ditemukan: " + category);
                }
                resultSet.close();
            }
            return categoryIds;
        }
        //AUTO INSERT dtl_game TAIL
    @FXML
    protected void btnClearClick(ActionEvent event) {
        clear();
    }
    public void clear()
    {
        txtId.clear();
        txtNm.clear();
        txtDes.clear();
        txtTgl.setValue(null);
        txtKat.clear();
        cbJenis.setValue("");
        deselectAllCheckboxes();
        lbKosong.setVisible(false);
        txtId.setText(generateAutoId());
    }

    //MAIN BUTTON TAIL

    //BUTTON CHECK BOX HEAD
    @FXML
    public void onClickCtg() {//BUTTON ADD YG POPUP CHECKBOX
        if(Pct.isVisible())
        {
            Pct.setVisible(false);
        }
       else {
            Pct.setVisible(true);
        }
    }
    @FXML
    protected void handleOkButtonClick(){
        String cat = "";
        for (Node node : cbContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    String name = checkBox.getText();
                    cat += name + ", ";
                }
            }
        }

        // Remove the trailing comma and whitespace, if any
        if (!cat.isEmpty()) {
            cat = cat.substring(0, cat.length() - 2);
        }else {
            katgrLbl.setVisible(true);
            return;
        }
        txtKat.setText(cat);
        Pct.setVisible(false);
    }
    @FXML
    protected void handleBtlButtonClick() {
        Pct.setVisible(false);
    }
    //BUTTON CHECK BOX TAIL


        //LOAD DATABASE KE TABLE HEAD
    private void loadDate() {//LOAD DATANYA
        refreshTable();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nmCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("desk"));
        tglCol.setCellValueFactory(new PropertyValueFactory<>("tgl"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("jenis_ps"));
        katCol.setCellValueFactory(new PropertyValueFactory<>("ctgry"));
        //add cell of button edit
        Callback<TableColumn<Game, String>, TableCell<Game, String>> cellFoctory = (TableColumn<Game, String> param) -> {
            // make cell containing buttons
            final TableCell<Game, String> cell = new TableCell<Game, String>() {
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
                                game = tblGame.getSelectionModel().getSelectedItem();

                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.initOwner(tblGame.getScene().getWindow()); // Set the owner window
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Delete Game");
                                confirmationDialog.setContentText("Are you sure you want to delete this game?");

                                Optional<ButtonType> result = confirmationDialog.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    query = "DELETE FROM ms_game WHERE id_game = '" + game.getId()+"'";
                                    connection = Connection.getConnect();
                                    preparedStatement = connection.prepareStatement(query);
                                    preparedStatement.execute();
                                    refreshTable();
                                    loadDate();
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
                            Game selectedGame = tblGame.getSelectionModel().getSelectedItem();

                            if (selectedGame != null) {
                                txtId.setText(selectedGame.getId());
                                txtNm.setText(selectedGame.getNama());
                                txtDes.setText(selectedGame.getDesk());
                                txtTgl.setValue(LocalDate.parse(selectedGame.getTgl()));
                                txtKat.setText(selectedGame.getCtgry());
                                cbJenis.setValue(selectedGame.getJenis_ps());
                                deselectAllCheckboxes();
                                selectCheckboxes(selectedGame.getId());

                                highlightTextField(txtId);
                                highlightTextField(txtNm);
                                highlightTextField(txtDes);
                                highlightDatePicker(txtTgl);
                                highlightTextField(txtKat);
                                highlightTextField(cbJenis);
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
        tblGame.setItems(gameList);

        //Filtered
        FilteredList<Game> filteredData = new FilteredList<>(gameList, b-> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) ->{
        filteredData.setPredicate(Game ->{
            if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                return  true;
            }
            String searchKeyword = newValue.toLowerCase();
            if (Game.getNama() != null && Game.getNama().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            } else if (Game.getId() != null && Game.getId().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            }else if (Game.getDesk() != null && Game.getDesk().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            } else if (Game.getCtgry() != null && Game.getCtgry().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            } else if (Game.getJenis_ps() != null && Game.getJenis_ps().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            }  else if (Game.getTgl() != null && Game.getTgl().toLowerCase().indexOf(searchKeyword) > -1) {
                return true;
            } else {
                return false;
            }
        });
        });
        SortedList<Game> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblGame.comparatorProperty());
        tblGame.setItems(sortedData);
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
    private void highlightTextField(ChoiceBox textField) {//TRANSISI PAS EDIT ICON DI KLIK
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), textField);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        fadeTransition.setOnFinished(event -> textField.setStyle("-fx-text-fill: black;"));

        fadeTransition.play();
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
            gameList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT a.*, STRING_AGG(c.nama_kategori, ', ') AS kategori\n" +
                    "FROM ms_game a\n" +
                    "LEFT JOIN dtl_game b ON a.id_game = b.id_game\n" +
                    "LEFT JOIN ms_kategorigame c ON c.id_kategori = b.id_kategori\n" +
                    "GROUP BY a.id_game, a.nama_game, a.deskripsi_game, a.tanggal_rilis, a.jenis_ps";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                gameList.add(new Game(
                        connection.result.getString("id_game"),
                        connection.result.getString("nama_game"),
                        connection.result.getString("deskripsi_game"),
                        connection.result.getString("tanggal_rilis"),
                        connection.result.getString("jenis_ps"),
                        connection.result.getString("kategori")));
            }
            tblGame.setItems(gameList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
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
            String sql = "SELECT nama_kategori FROM ms_kategorigame WHERE id_kategori IN (SELECT id_kategori FROM dtl_game WHERE id_game = '" + idG + "')";
            conn.stat = conn.conn.createStatement();

            // Execute the query
            conn.result = conn.stat.executeQuery(sql);

            // Retrieve category names from the result set
            while (conn.result.next()) {
                String categoryName = conn.result.getString("nama_kategori");
                categories.add(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    //LOAD DATABASE KE TABLE TAIL

}
