using Microsoft.EntityFrameworkCore;
using Backend_DB.Models;

namespace Backend_DB.Persistence
{
    public class EmployerEF
    {
        public Employer? GetEmployer(int Id)
        {
            using var context = new Context();
            return context.Employers.Find(Id);
        }

        public List<Employer> GetEmployers()
        {
            using var context = new Context();
            return context.Employers.ToList();
        }

        public List<Session> GetEmployerSession(int Id)
        {
            using var context = new Context();
            return context.Sessions.Where(s => s.EmployerId == Id).ToList();
        }

        public (Employer?, bool) InsertEmployer(Employer Employer)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateEmployer = context.Employers.SingleOrDefault(r => r.Email == Employer.Email);
            if (duplicateEmployer != null) return (duplicateEmployer, false);

            // Insert Employer
            try
            {
                context.Employers.Add(Employer);
                context.SaveChanges();  // Update the changes to object
                return (Employer, true);
            }
            catch
            {
                return (null, false);
            }
        }

        public Employer? UpdateEmployer(Employer updatedEmployer)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateEmployer = context.Employers.SingleOrDefault(r => r.Email == updatedEmployer.Email && r.Id != updatedEmployer.Id);
            if (duplicateEmployer != null) return duplicateEmployer;

            // Update Employer
            try
            {
                context.Employers.Where(r => r.Id == updatedEmployer.Id)
                .ExecuteUpdate(setter => setter
                    .SetProperty(r => r.Email, updatedEmployer.Email)
                    .SetProperty(r => r.CompanyName, updatedEmployer.CompanyName)
                    .SetProperty(r => r.Password, updatedEmployer.Password));
                return context.Employers.Single(r => r.Id == updatedEmployer.Id);
            }
            catch
            {
                return null;
            }
        }

        public Employer? DeleteEmployer(int Id)
        {
            using var context = new Context();
            var Employer = context.Employers.Find(Id);
            if (Employer == null) return null;
            try
            {
                context.Employers.Remove(Employer);
                context.SaveChanges();
                return Employer;
            }
            catch
            {
                return null;
            }
        }
    }
}
