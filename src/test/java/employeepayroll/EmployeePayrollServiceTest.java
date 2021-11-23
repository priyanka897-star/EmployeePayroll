package employeepayroll;

import javapractice.EmployeePayRollData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
}


