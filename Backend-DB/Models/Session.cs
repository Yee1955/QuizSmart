using System;
using System.Collections.Generic;

namespace Backend_DB.Models
{
    public partial class Session
    {
        public int Id { get; set; }
        public int EmployerId { get; set; }
        public string SessionCode { get; set; } = null!;
        public string JobPosition { get; set; } = null!;
        public string JobRequirement { get; set; } = null!;
        public string JobResponsibilities { get; set; } = null!;
        public string CompanyCulture { get; set; } = null!;
        public string Status { get; set; } = null!;
        public string? QuestionString { get; set; }

        public virtual Employer IdNavigation { get; set; } = null!;
        public virtual ICollection<EmployeeSession> EmployeeSessions { get; set; }
        public Session()
        {
                
        }
        
        public Session(int id, int employerId, string sessionCode, string jobPosition, string jobRequirement, string jobResponsibilities, string companyCulture, string status,  string? questionString)
        {
            Id = id;
            EmployerId = employerId;
            SessionCode = sessionCode;
            JobPosition = jobPosition;
            JobRequirement = jobRequirement;
            JobResponsibilities = jobResponsibilities;
            CompanyCulture = companyCulture;
            Status = status;
            QuestionString = questionString;
        }
    }
}
