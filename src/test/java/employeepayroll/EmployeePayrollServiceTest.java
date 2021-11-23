package employeepayroll;

import javapractice.EmployeePayRollData;
import javapractice.EmployeePayrollDBService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javapractice.EmployeePayrollService;

public class EmployeePayrollServiceTest {
    @Test
    public void givenDataBaseConnection_ReturnDataFromDataBase() {
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        List<EmployeePayRollData> list = employeePayRollService.readEmployeePayRoll();
        System.out.println(list);
        Assertions.assertEquals(4, list.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        List<EmployeePayRollData> payRollDataList = employeePayRollService.readEmployeePayRoll();
        employeePayRollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayRollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        EmployeePayrollDBService employeePayRollDBService = new EmployeePayrollDBService();
        employeePayRollService.readEmployeePayRoll();
        LocalDate startDate = LocalDate.of(2019, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayRollData> employeePayRollDBData = employeePayRollDBService.readEmployeePayRollForDateRange(startDate, endDate);
        Assertions.assertEquals(4, employeePayRollDBData.size());
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryRetrieveByGender_ShouldReturnProperValue() {
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        EmployeePayrollDBService employeePayRollDBService = new EmployeePayrollDBService();
        employeePayRollService.readEmployeePayRoll();
        Map<String, Double> averageSalaryByGender = employeePayRollService.readAverageSalaryByGender();
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(4000000.00) && averageSalaryByGender.get("F").equals(1700000.00));
    }

    @Test
    public void givenPayrollData_WhenCountByGender_ShouldReturnCount() {
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        employeePayRollService.readEmployeePayRoll();
        Map<String, Integer> countByGender = employeePayRollService.readCountSalaryByGender();
        Assertions.assertTrue(countByGender.get("M").equals(1) && countByGender.get("F").equals(3));
    }
}


