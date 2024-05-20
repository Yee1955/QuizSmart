using System;
using System.Collections.Generic;

namespace Backend_DB.Models
{
    public partial class Employer
    {
        public int Id { get; set; }
        public string Email { get; set; } = null!;
        public string CompanyName { get; set; } = null!;
        public string Password { get; set; } = null!;

        public virtual Session Session { get; set; } = null!;
        public Employer()
        {
                
        }
        
        public Employer(int id, string email, string companyName,  string password)
        {
            Id = id;
            Email = email;
            CompanyName = companyName;
            Password = password;
        }
    }
}
