package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.Customer;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final String DB_PATH = "//127.0.0.1:3306";
    public static final String DB_Name = "/School";
    public static final String DB_URL = "jdbc:mysql:" + DB_PATH + DB_Name;
    public static final String DB_Driver = "com.mysql.cj.jdbc.Driver";
    private ObservableList<Customer> customers;
    private Connection connection = connectDB();

    @FXML
    private Button addClientBtn;

    @FXML
    private Button deleteClientBtn;

    @FXML
    private TableColumn<Customer, Integer> id;

    @FXML
    private TableColumn<Customer, String> name;

    @FXML
    private void addClient() {
        insertCustomer();
    }

    @FXML
    private void deleteClient() {

    }

    @FXML
    private TextField nameTextField;

    private String getNameText() {
        String str = nameTextField.getText();
        nameTextField.clear();
        return str;
    }

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCustomers();
    }

    private void insertCustomer() {
        String customerName = getNameText();
        try {
            Statement statement = connection.createStatement();
            if (!customerName.isEmpty()) {
                statement.executeUpdate("INSERT INTO customer (customerName) VALUES ('" + customerName + "');");
            }
            showCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showCustomers() {
        ObservableList<Customer> customers = getCustomers();
        System.out.println(customers.toString());
        TableView<Customer> tableView = new TableView<>(customers);
        id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
    }

    private void deleteCustomer() {

    }

    public ObservableList<Customer> getCustomers() {
        customers = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer;");
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt("id"), resultSet.getString("customerName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static Connection connectDB() {
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL, "root", "12345678");//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }
        return null;
    }
}
