package javapractice;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class EmployeePayrollDBService  {

  public  List<EmployeePayRollData> readData() {
    List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try{
       String sql = "SELECT * FROM employee_payroll;";
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            int count = 0;
            while (result.next()) {
                count++;
                int id = result.getInt("id");
                String name = result.getString("name");
               double salary = result.getDouble("salary");
                LocalDate start = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayRollData(id, name, salary, start));
            }
            System.out.println(count);
            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
      return employeePayrollList;
    }
    public Connection getConnection() {

            String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
            String userName = "root";
            String password = "Admin@123";
            Connection con = null;
            System.out.println("Connectiong to database" + jdbcURL);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        }
        catch (ClassNotFoundException e){
            throw  new IllegalStateException("Cannot find the driver in the classpath",e);
        }

        try {
            con = DriverManager.getConnection(jdbcURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection is successfull" +con);
        return con;
    }
}

