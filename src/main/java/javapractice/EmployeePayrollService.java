package javapractice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {

    private List<EmployeePayRollData> employeePayRollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayRollData> employeePayRollList) {
        this();
        this.employeePayRollList = employeePayRollList;
    }

    public List<EmployeePayRollData> readEmployeePayRoll() {
        this.employeePayRollList = new EmployeePayrollDBService().readData();
        this.employeePayRollList = EmployeePayrollDBService.getInstance().readData();
        return employeePayRollList;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if (result == 0) return;
        EmployeePayRollData employeePayRollData = this.getEmployeePayRollData(name);
        if (employeePayRollData != null)
            employeePayRollData.salary = salary;
    }

    private EmployeePayRollData getEmployeePayRollData(String name) {
        EmployeePayRollData employeePayrollData;
        employeePayrollData = this.employeePayRollList.stream().filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayRollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayRollData(name));
    }

    public Map<String, Double> readAverageSalaryByGender() {
        return employeePayrollDBService.getAverageSalaryByGender();
    }

    public Map<String, Integer> readCountSalaryByGender() {
        return employeePayrollDBService.getCountByGender();
    }

    public void addEmployeePayroll(String name, double salary, LocalDate start, String gender) {
        employeePayRollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary, start, gender));
    }
}
