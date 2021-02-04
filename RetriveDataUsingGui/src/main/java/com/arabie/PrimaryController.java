package com.arabie;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.sql.DataSource;

public class PrimaryController implements Initializable {

    private DataSource ds = MyDataSourceFactory.getMYSQLDataSource();
    private ResultSet rs ;

    @FXML
    private TextField idTxtFd;
    @FXML
    private TextField fNameTxtFd;
    @FXML
    private TextField mNameTxtFd;
    @FXML
    private TextField lNameTxtFd;
    @FXML
    private TextField emailTxtFd;
    @FXML
    private TextField phoneTxtFd;

    @FXML
    private Button newBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button firstBtn;
    @FXML
    private Button prevBtn;
    @FXML
    private Button nxtBtn;
    @FXML
    private Button lastBtn;


    enum Position{
        FIRST,LAST,NEXT,PREV
    }

    private Emp emp= new Emp();
    private Connection con ;
    private Statement stmt;
    private void manageBtns() throws SQLException {

        prevBtn.setDisable(rs.isFirst() || rs.isAfterLast() || rs.isBeforeFirst());
        firstBtn.setDisable(rs.isFirst() || rs.isAfterLast() || rs.isBeforeFirst());
        deleteBtn.setDisable(rs.isAfterLast() || rs.isBeforeFirst());
        updateBtn.setDisable(rs.isAfterLast() || rs.isBeforeFirst());
        nxtBtn.setDisable(rs.isLast() || rs.isAfterLast() || rs.isBeforeFirst());
        lastBtn.setDisable(rs.isLast() ||rs.isAfterLast() || rs.isBeforeFirst());

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            updateConnection();
            rs.beforeFirst();
            if(!rs.next()){
                prevBtn.setDisable(true);
                nxtBtn.setDisable(true);
                firstBtn.setDisable(true);
                lastBtn.setDisable(true);
            }
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            idTxtFd.setDisable(true);
            checkFieldLength(idTxtFd,10);
            checkFieldLength(emailTxtFd,15);
            checkFieldLength(fNameTxtFd,15);
            checkFieldLength(mNameTxtFd,15);
            checkFieldLength(lNameTxtFd,15);
            checkFieldLength(phoneTxtFd,11);

        } catch (SQLException e) {
            displayAlert(e, Alert.AlertType.ERROR);
        }



    }
    @FXML
    private void lab3Handler(){
        try(Connection connection = ds.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("create table Employee (id SMALLINT ," +
                    "f_name varchar(20),l_name varchar(20),sex varchar(10)," +
                    "age SMALLINT ,address varchar(40),phone_number varchar(20)," +
                    "vac_balance smallint default 30)"))
        {

            int n =pStmt.executeUpdate();
            PreparedStatement insrtStmt =connection.prepareStatement("insert into employee values (?,?,?,?,?,?,?,?);");
            for(int i=1;i<=5;i++){
                insrtData(insrtStmt,i,"user"+i,"userFather"+i,
                        i%2==0?"male":"female",43+i,"someWhere","01245",30);
                n=insrtStmt.executeUpdate();
            }
            displayAlert(new SQLException("Lab3 ran successfully"), Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            displayAlert(e, Alert.AlertType.ERROR);

        }

    }
    @FXML
    private void dropBtnHandler(){
        try(Connection connection = ds.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("drop table Employee "))
        {

            int n =pStmt.executeUpdate();
            displayAlert(new Exception("Table Dropped Successfully!"), Alert.AlertType.INFORMATION);
        }
        catch (Exception e){
            displayAlert(e, Alert.AlertType.ERROR);
        }

    }
    /**
     *
     * validation
     * GUI formatting
     * javafx design (benny coder)
     * exception
     * nxt and prev
     * remove buttons
     *
     * */

    @FXML
    private void lab4Handler(){
        try(Connection connection = ds.getConnection();
            Statement stmt = connection.createStatement()//;
//            PreparedStatement pStmt1 =connection.prepareStatement("update Employee2 " +
//                    "set F_Name = ?, Vacation_Balance=45 " +
//                    "where ID=?");
//            PreparedStatement pStmt2 =connection.prepareStatement("update employee set f_name = ? where sex like ?")
            )
        {
                connection.setAutoCommit(false);

                stmt.addBatch("update employee set vac_balance =45 where  age > 45");
                stmt.addBatch("update employee set f_name =CONCAT('Mr ',f_name) where sex like 'male'");
                stmt.addBatch("update employee set f_name =CONCAT('Mrs ',f_name) where sex like 'female'");
                stmt.executeBatch();

                connection.commit();
                displayAlert(new SQLException("Lab4 ran successfully"), Alert.AlertType.INFORMATION);

        } catch (SQLException e) {
            displayAlert(e, Alert.AlertType.ERROR);
        }
    }

    private void insrtData(PreparedStatement insrtStmt,int id,String fname,
                           String lname,String sex,int age,String addrs,
                           String phNmbr,int vacBal) throws SQLException {

        insrtStmt.setInt(1,id);
        insrtStmt.setString(2,fname);
        insrtStmt.setString(3,lname);
        insrtStmt.setString(4,sex);
        insrtStmt.setInt(5,age);
        insrtStmt.setString(6,addrs);
        insrtStmt.setString(7,phNmbr);
        insrtStmt.setInt(8,vacBal);

    }

    private void updateConnection() throws SQLException {
        con = ds.getConnection();
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        rs = stmt.executeQuery("select * from emps");
    }

    private void updateView(){
        idTxtFd.setText(emp.getId().toString());
        fNameTxtFd.setText(emp.getfName());
        mNameTxtFd.setText(emp.getmName());
        lNameTxtFd.setText(emp.getlName());
        emailTxtFd.setText(emp.getEmail());
        phoneTxtFd.setText(emp.getPhone());
    }

//    private void testDataSource() {
////        emp =new Emp();
////        System.out.println("I'm here");
//        try(Connection con = ds.getConnection();
//            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE))
//        {
//            rs = stmt.executeQuery("select * from emps");
//            updateModelFromDB();
////            while(rs.next()){
////            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void updateModelFromDB(ResultSet rs) throws SQLException {

            emp.setId(rs.getInt("id"));
            emp.setfName(rs.getString("fName"));
            emp.setmName(rs.getString("mName"));
            emp.setlName(rs.getString("lName"));
            emp.setEmail(rs.getString("email"));
            emp.setPhone(rs.getString("phone"));


    }
    private void updateModelFromView()throws Exception{

        if(idTxtFd.getText().matches("[0-9]+"))
            emp.setId(Integer.parseInt(idTxtFd.getText()));
        else
            throw new Exception("Invalid ID Must be a number");
        if( fNameTxtFd.getText().length()<=20
            ||mNameTxtFd.getText().length()<=20
            ||lNameTxtFd.getText().length()<=20){

            emp.setfName(fNameTxtFd.getText());
            emp.setmName(mNameTxtFd.getText());
            emp.setlName(lNameTxtFd.getText());
        }
        else {
            throw new Exception("Too Long names must not exceed 15 character");
        }
        if(emailTxtFd.getText().matches("^[\\w-\\._]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            emp.setEmail(emailTxtFd.getText());
        else
            throw new Exception("Wrong Email");
        if(phoneTxtFd.getText().matches("01\\d{9}"))
            emp.setPhone(phoneTxtFd.getText());
        else
            throw new Exception("Wrong Phone number 11 digits must Entered");

    }
    @FXML
    private void firstBtnHandler() {

        handlerAtPosition(Position.FIRST);
    }
    @FXML
    private void lastBtnHandler() {
        handlerAtPosition(Position.LAST);
    }
    @FXML
    private void nextBtnHandler() {
        handlerAtPosition(Position.NEXT);
    }
    @FXML
    private void prevBtnHandler() {
        handlerAtPosition(Position.PREV);
    }
    private void handlerAtPosition(Position position) {
        try{
            clearView();
            if (position == Position.FIRST){
//                prevBtn.setDisable(true);
//                nxtBtn.setDisable(false);
                rs.first();
            }
            else if (position == Position.LAST){
//                prevBtn.setDisable(false);
//                nxtBtn.setDisable(true);
                rs.last();
            }
            else if (position == Position.NEXT) {
                if(rs.isLast()){
//                    prevBtn.setDisable(false);
//                    nxtBtn.setDisable(true);
                }else {
                    rs.next();
                }
            } else if (position == Position.PREV){
                if(rs.isFirst()){
//                    prevBtn.setDisable(true);
//                    nxtBtn.setDisable(false);
                }else {
                    rs.previous();
                }

            }
            manageBtns();
            updateModelFromDB(rs);
            updateView();

        }catch (SQLException e) {
            displayAlert(e, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void newBtnHandler() throws SQLException {
        if(newBtn.getText().equals("New")){
            idTxtFd.setDisable(false);
            clearView();
            newBtn.setText("Insert");
            prevBtn.setDisable(true);
            firstBtn.setDisable(true);
            deleteBtn.setDisable(true);
            updateBtn.setDisable(true);
            nxtBtn.setDisable(true);
            lastBtn.setDisable(true);

        }
        else if(newBtn.getText().equals("Insert")){

            if(isDataValid()){
                try(Connection connection = ds.getConnection();
                    PreparedStatement pStmt = connection.prepareStatement("insert into emps values (?,?,?,?,?,?);",
                            ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE))
                {
                    idTxtFd.setDisable(true);
                    updateModelFromView();

                    updateDBFromModel(pStmt);

                    int n=pStmt.executeUpdate();
                    connection.setAutoCommit(true);
                    clearView();
                    displayAlert(new Exception("Data Inserted Successfully!"), Alert.AlertType.INFORMATION);

                    updateConnection();

                }catch (Exception e) {
                    displayAlert(e, Alert.AlertType.ERROR);
                }
            }
            else{
                displayAlert(new Exception("Illegal Arguments not valid Data"), Alert.AlertType.ERROR);

            }


            newBtn.setText("New");
            rs.beforeFirst();
            if(rs.next()) {
                //            rs.next();
                updateModelFromDB(rs);
                updateView();
                manageBtns();
            }
        }
    }
    @FXML
    private void clearBtnHandler(){

        clearView();
    }

    private void checkFieldLength(TextField textField,int maxLength){
        textField.textProperty().addListener((observableValue, s, t1) -> {
            if(textField.getText().length()>maxLength){
                textField.setText(s);
            }
        });
    }

    private void updateDBFromModel(PreparedStatement pStmt) throws SQLException {
        pStmt.setInt(1,emp.getId());
        pStmt.setString(2, emp.getfName());
        pStmt.setString(3,emp.getmName());
        pStmt.setString(4,emp.getlName());
        pStmt.setString(5,emp.getEmail());
        pStmt.setString(6,emp.getPhone());
    }
    private void updateDBFromModel() throws SQLException {
        rs.updateInt(1,emp.getId());
        rs.updateString(2, emp.getfName());
        rs.updateString(3,emp.getmName());
        rs.updateString(4,emp.getlName());
        rs.updateString(5,emp.getEmail());
        rs.updateString(6,emp.getPhone());
    }
    private void displayAlert(Exception e, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Watch out!!!");
        if(e instanceof SQLException){
            if(((SQLException)e).getErrorCode()==1062)
                alert.setContentText("Duplicated ID");
       }
        else {
            alert.setContentText(e.getMessage());
        }
        alert.show();
        clearView();
    }
    private boolean isDataValid(){
        if(idTxtFd.getText().equals("")||
            fNameTxtFd.getText().equals("")||
            lNameTxtFd.getText().equals("")||
            mNameTxtFd.getText().equals("")||
            emailTxtFd.getText().equals("")||
            phoneTxtFd.getText().equals("")){
            return false;
        }
        return true;
    }
    private void clearView() {
        idTxtFd.clear();
        phoneTxtFd.clear();
        fNameTxtFd.clear();
        lNameTxtFd.clear();
        mNameTxtFd.clear();
        emailTxtFd.clear();
    }
    @FXML
    private void updateBtnHandler() throws SQLException {
        if(isDataValid()){

            try {
                updateModelFromView();
                updateDBFromModel();
                rs.updateRow();
                displayAlert(new Exception("Data updated Successfully!"), Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                displayAlert(e, Alert.AlertType.ERROR);
            }
        }
        else {
            displayAlert(new Exception("not Valid Data"), Alert.AlertType.ERROR);
        }
        updateModelFromDB(rs);
        updateView();
        manageBtns();

    }
    @FXML
    private void deleteBtnHandler()  {
        if(isDataValid()){
            try {

                if(rs.isLast()&&rs.isFirst()){
                    prevBtn.setDisable(true);
                    firstBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                    updateBtn.setDisable(true);
                    nxtBtn.setDisable(true);
                    lastBtn.setDisable(true);
                }
                rs.deleteRow();
                clearView();
                displayAlert(new RuntimeException("Data deleted Successfully!"), Alert.AlertType.INFORMATION);
                rs.beforeFirst();
                if(rs.next()) {

//                    if (rs.isLast()) {
//                        rs.previous();
//                    } else {
//                    rs.next();
//                    }

                    updateModelFromDB(rs);
                    updateView();
                }
            } catch (Exception e) {
                displayAlert(e, Alert.AlertType.ERROR);
            }
        }
        else {
            displayAlert(new Exception("No valid Data to delete"), Alert.AlertType.ERROR);
        }
    }
}
