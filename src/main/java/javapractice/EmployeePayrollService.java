package javapractice;

import java.util.List;

public class EmployeePayrollService {

    public List<EmployeePayRollData> readEmployeePayroll()  {
        List<EmployeePayRollData> employeePayRollList ;
        employeePayRollList = new EmployeePayrollDBService().readData();
        return employeePayRollList;
    }
}