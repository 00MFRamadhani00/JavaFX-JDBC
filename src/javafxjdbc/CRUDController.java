package javafxjdbc;

import db.DBHelper;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CRUDController implements Initializable{
    
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Mahasiswa, String> colAlamat;

    @FXML
    private TableColumn<Mahasiswa, String> colNama;

    @FXML
    private TableColumn<Mahasiswa, String> colNpm;
    
    @FXML
    private TableView<Mahasiswa> tvData;

    @FXML
    private TextField tfAlamat;

    @FXML
    private TextField tfNama;

    @FXML
    private TextField tfNpm;


    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnInsert){
            insertRecord();
        }else if(event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteRecord();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        showMahasiswa();
    }
    
    public ObservableList<Mahasiswa> getDataMahasiswa(){
        ObservableList<Mahasiswa> mhs = FXCollections.observableArrayList();
        Connection conn = DBHelper.getConnection();
        String query = "SELECT * FROM `mahasiswa`";
        Statement st;
        ResultSet rs;
        
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Mahasiswa temp;
            while(rs.next()){
                temp = new Mahasiswa(rs.getString("npm"), rs.getString("nama"), rs.getString("alamat"));
                mhs.add(temp);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return mhs;
    }
    
    public void showMahasiswa(){
        ObservableList<Mahasiswa> list = getDataMahasiswa();
        
        colNpm.setCellValueFactory(new PropertyValueFactory<>("npm"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        tvData.setItems(list);
    }
    
    private void update(String query){
        Connection conn = DBHelper.getConnection();
        Statement st;
        
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void insertRecord(){
        String query = "INSERT INTO `mahasiswa` VALUES ('" + tfNpm.getText() + "',' " + tfNama.getText() + 
                "','" + tfAlamat.getText() + "')";
        update(query);
        showMahasiswa();
        
        tfNpm.clear();
        tfNama.clear();
        tfAlamat.clear();
        
    }
    
    private void updateRecord(){
        String query = "UPDATE `mahasiswa` SET nama = '" + tfNama.getText() + "', alamat = '" + tfAlamat.getText()
                + "' WHERE npm = '" + tfNpm.getText() + "'";
        update(query);
        showMahasiswa();
        
        tfNpm.clear();
        tfNama.clear();
        tfAlamat.clear();
    }
    
    private void deleteRecord(){
        String query = "DELETE FROM `mahasiswa` WHERE npm = '" + tfNpm.getText() + "'";
        update(query);
        showMahasiswa();
        
        tfNpm.clear();
    }
}
