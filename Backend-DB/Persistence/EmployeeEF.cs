using Microsoft.EntityFrameworkCore;
using Backend_DB.Models;

namespace Backend_DB.Persistence
{
    public class EmployeeEF
    {
        public Employee? GetEmployee(int Id)
        {
            using var context = new Context();
            return context.Employees.Find(Id);
        }

        public List<Employee> GetEmployees()
        {
            using var context = new Context();
            return context.Employees.ToList();
        }

        public (Employee?, bool) InsertEmployee(Employee Employee)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateEmployee = context.Employees.SingleOrDefault(r => r.Email == Employee.Email);
            if (duplicateEmployee != null) return (duplicateEmployee, false);

            // Insert Employee
            try
            {
                context.Employees.Add(Employee);
                context.SaveChanges();  // Update the changes to object
                return (Employee, true);
            }
            catch
            {
                return (null, false);
            }
        }

        public Employee? UpdateEmployee(Employee updatedEmployee)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateEmployee = context.Employees.SingleOrDefault(r => r.Email == updatedEmployee.Email && r.Id != updatedEmployee.Id);
            if (duplicateEmployee != null) return duplicateEmployee;

            // Update Employee
            try
            {
                context.Employees.Where(r => r.Id == updatedEmployee.Id)
                .ExecuteUpdate(setter => setter
                    .SetProperty(r => r.Email, updatedEmployee.Email)
                    .SetProperty(r => r.FullName, updatedEmployee.FullName)
                    .SetProperty(r => r.Password, updatedEmployee.Password));
                return context.Employees.Single(r => r.Id == updatedEmployee.Id);
            }
            catch
            {
                return null;
            }
        }

        public Employee? DeleteEmployee(int Id)
        {
            using var context = new Context();
            var Employee = context.Employees.Find(Id);
            if (Employee == null) return null;
            try
            {
                context.Employees.Remove(Employee);
                context.SaveChanges();
                return Employee;
            }
            catch
            {
                return null;
            }
        }
    }
}
