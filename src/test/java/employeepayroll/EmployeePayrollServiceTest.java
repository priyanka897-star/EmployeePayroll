package employeepayroll;

import javapractice.EmployeePayRollData;
import javapractice.EmployeePayrollDBService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import javapractice.EmployeePayrollService;
public class EmployeePayrollServiceTest {
    @Test
    public void givenDataBaseConnection_ReturnDataFromDataBase(){
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        List<EmployeePayRollData> list = employeePayRollService.readEmployeePayRoll();
        System.out.println(list);
        Assertions.assertEquals(4, list.size());
    }
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        List<EmployeePayRollData> payRollDataList = employeePayRollService.readEmployeePayRoll();
        employeePayRollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayRollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }
    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayRollService = new EmployeePayrollService();
        EmployeePayrollDBService employeePayRollDBService= new EmployeePayrollDBService() ;
        employeePayRollService.readEmployeePayRoll();
        LocalDate startDate = LocalDate.of(2019,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayRollData> employeePayRollDBData = employeePayRollDBService.readEmployeePayRollForDateRange(startDate, endDate);
        Assertions.assertEquals(2, employeePayRollDBData.size());
    }
}


