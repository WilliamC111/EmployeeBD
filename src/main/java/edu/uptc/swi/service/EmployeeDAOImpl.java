package edu.uptc.swi.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.uptc.swi.model.Employee;

public class EmployeeDAOImpl implements IEmployeeDAO {

    private static final String USER = "root";
    private static final String PASSWORD = "A1234";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/employee?useSSL=false&allowPublicKeyRetrieval=true";
    private static Connection connection = null;
    private Statement stmt = null;

    @Override
    public List<Employee> findAll() {
        return this.getEmployees();
    }

    @Override
    public Employee findById(String id) {
        return this.getEmployee(String.valueOf(id));
    }

    @Override
    public boolean save(Employee employee) {
        boolean res = false;
        String id = this.getEmployee(employee.getId()).getId();
        if (id != null)
            res = this.executeQuery("update employee set id='" + employee.getId() + "', name='" + employee.getName()
                    + "', email='" + employee.getEmail() + "', phone='" + employee.getPhone() + "' where id='"
                    + employee.getId() + "';");
        else
            res = this.executeQuery("insert into employee (id, name, email, phone) values('" + employee.getId() + "','"
                    + employee.getName() + "','" + employee.getEmail() + "','" + employee.getPhone() + "');");
        return res;
    }

    @Override
    public boolean deleteEmployeeById(String id) {
        boolean res = false;
        if (this.getEmployee(id) != null)
            res = this.executeQuery("delete from employee where id='" + id + "';");
        return res;
    }

    private boolean executeQuery(String query) {
        boolean res = false;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            connection.close();
            res = true;
        } catch (SQLException sqlex) {
            System.out.println(sqlex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return res;
    }

    private Employee getEmployee(String id) {
        String query = "select * from employee where id=" + id + ";";
        Employee emp = new Employee();
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                emp.setId(rs.getString("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setPhone(rs.getString("phone"));
            }
            connection.close();
        } catch (SQLException sqlex) {
            System.out.println(sqlex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return emp;

    }

    private List<Employee> getEmployees() {
        String query = "select * from employee";
        List<Employee> list = new ArrayList<Employee>();
        Employee employee = null;
        ResultSet rs = null;
    
        try {
            // Asegúrate de cargar el driver de la base de datos
            Class.forName(DRIVER);
            
            // Crear una conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Inicializar el Statement
            stmt = connection.createStatement(); 
            
            // Ejecutar la consulta
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                employee = new Employee();
                employee.setId(rs.getString("id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhone(rs.getString("phone"));
                list.add(employee);
            }
        } catch (SQLException sqlex) {
            System.out.println("Error de SQL: " + sqlex);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error cargando el driver JDBC: " + ex);
        } finally {
            // Liberar recursos en el bloque finally
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e);
            }
        }
        
        return list;
    }
     

}
