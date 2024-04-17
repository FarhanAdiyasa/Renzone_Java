package com.example.renzone.controller;

import com.example.renzone.DBConnection.Connection;
import com.example.renzone.StageManager;
import com.example.renzone.models.bMember;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class pickFromTbController implements Initializable {
    @FXML
    TableView<bMember> tblmember;
    @FXML
    TableColumn<bMember, String> idColumn;
    @FXML TableColumn<bMember, String> namaColumn;
    @FXML TableColumn<bMember, String> statusColumn;
    @FXML
    TextField searchBar;
    String query = null;
    java.sql.Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    bMember member = null;
    private Stage stage;
    ObservableList<bMember> MemberList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
    }
    private void loadDate() {
        refreshTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idM"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaM"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusM"));
        tblmember.setItems(MemberList);
        tblmember.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                bMember selectedData = tblmember.getSelectionModel().getSelectedItem();
                if (selectedData != null) {
                    System.out.println(selectedData.getIdM());
                    deleteBntuMember();
                    insertDataFromMsMemberToBntuMember(selectedData.getIdM());
                }
                Stage stages = (Stage) tblmember.getScene().getWindow();
                stages.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/renzone/FXML/Karyawan/transaksi.fxml"));
                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Transaksi_Controller tc = loader.getController();
                tc.loadDataFromBntuMemberTable();

                // Get the primary stage and set the refreshed parent form
                Stage primaryStage = StageManager.getPrimaryStage();
                primaryStage.setScene(new Scene(parent));
                primaryStage.setFullScreen(true);
            }
        });
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
                }  else if (bMember.getStatusM() != null && bMember.getStatusM().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (bMember.getIdM() != null && bMember.getIdM().toLowerCase().indexOf(searchKeyword) > -1) {
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
    public void insertDataFromMsMemberToBntuMember(String id) {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            // Prepare the SQL query to insert data from ms_member to bntu_member
            String insertQuery = "INSERT INTO bntu_member SELECT id_member, nama_member, alamat, no_telp, tgl_daftar, status_anggota FROM ms_member WHERE id_member = '" + id + "'";

            // Execute the insert query
            connection.stat.executeUpdate(insertQuery);

            // Refresh the table or perform any other necessary actions
            loadDate();
        } catch (Exception e) {
            System.out.println("Error inserting data from ms_member to bntu_member: " + e);
        }
    }
    public void deleteBntuMember() {
        try {
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();

            // Prepare the SQL query to insert data from ms_member to bntu_member
            String insertQuery = "DELETE FROM bntu_member";

            // Execute the insert query
            connection.stat.executeUpdate(insertQuery);

            // Refresh the table or perform any other necessary actions
            loadDate();
        } catch (Exception e) {
            System.out.println("Error inserting data from ms_member to bntu_member: " + e);
        }
    }
    public void refreshTable() {
        try {
            MemberList.clear();
            Connection connection = new Connection();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT [id_member], [nama_member], [status_anggota] FROM [ms_member] where status_anggota = 'Inactive' AND isDeleted is null";
            connection.result = connection.stat.executeQuery(query);

            while  (connection.result.next()){
                MemberList.add(new bMember(
                        connection.result.getString("id_member"),
                        connection.result.getString("nama_member"),
                        connection.result.getString("status_anggota")));
            }
            tblmember.setItems(MemberList);
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data aksesoris: " + e);
        }
    }
}
