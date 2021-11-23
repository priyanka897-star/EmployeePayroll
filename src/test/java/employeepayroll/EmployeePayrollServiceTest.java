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
        List<EmployeePayRollData> list = employeePayRollService.readEmployeePayroll();
        System.out.println(list);
        Assertions.assertEquals(4, list.size());
    }
}


