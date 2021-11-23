package javapractice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    public List<EmployeePayRollData> readData() {
        List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employee_payroll;";
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollList(result);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayRollData> getEmployeePayrollData(String name) {
        List<EmployeePayRollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayRollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Double salary = resultSet.getDouble("salary");
                LocalDate start = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayRollData(id, name, salary, start));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayRollData> getEmployeePayrollList(ResultSet result) {
        List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate start = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayRollData(id, name, salary, start));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public List<EmployeePayRollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * from employee_payroll WHERE START BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayRollData> getEmployeePayrollDataUsingDB(String sql) {
        List<EmployeePayRollData> payRollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            payRollDataList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payRollDataList;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * from employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayRollData> readEmployeePayRollForDateRange(LocalDate startDate, LocalDate endDate) {
        return employeePayrollDBService.getEmployeeForDateRange(startDate, endDate);
    }

    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "select gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public Map<String, Integer> getCountByGender() {
        String sql = "select gender, count(gender) as count from employee_payroll GROUP BY gender";
        Map<String, Integer> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");
                genderToAverageSalaryMap.put(gender, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }
    public EmployeePayRollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        int employeeId = -1;
        EmployeePayRollData payRollData = null;
        String sql = String.format("INSERT INTO employee_payroll(name, gender, salary,start) values ( '%s', '%s', %s, '%s')",name, gender, salary, Date.valueOf(startDate));
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1){
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            payRollData = new EmployeePayRollData(employeeId, name, salary, startDate);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return payRollData;
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
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath", e);
        }

        try {
            con = DriverManager.getConnection(jdbcURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection is successfull !!" + con);
        return con;
    }
}

