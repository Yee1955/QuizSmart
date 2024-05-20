using System;
using System.Collections.Generic;

namespace Backend_DB.Models
{
    public partial class Employee
    {
        public int Id { get; set; }
        public string Email { get; set; } = null!;
        public string FullName { get; set; } = null!;
        public string Password { get; set; } = null!;

        public virtual ICollection<EmployeeSession> EmployeeSessions { get; set; }
        public Employee()
        {
                
        }
        
        public Employee(int id, string email, string fullName,  string password)
        {
            Id = id;
            Email = email;
            FullName = fullName;
            Password = password;
        }
    }
}
