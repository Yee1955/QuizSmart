using Microsoft.EntityFrameworkCore;
using Backend_DB.Models;

namespace Backend_DB.Persistence
{
    public class SessionEF
    {
        public Session? GetSession(int Id)
        {
            using var context = new Context();
            return context.Sessions.Find(Id);
        }

        public List<Session> GetSessions()
        {
            using var context = new Context();
            return context.Sessions.ToList();
        }

        public (Session?, bool) InsertSession(Session Session)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateSession = context.Sessions.SingleOrDefault(r => r.SessionCode == Session.SessionCode);
            if (duplicateSession != null) return (duplicateSession, false);

            // Insert Session
            try
            {
                context.Sessions.Add(Session);
                context.SaveChanges();  // Update the changes to object
                return (Session, true);
            }
            catch
            {
                return (null, false);
            }
        }

        public Session? UpdateSession(Session updatedSession)
        {
            using var context = new Context();
            // Check for duplicate email
            var duplicateSession = context.Sessions.SingleOrDefault(r => r.SessionCode == updatedSession.SessionCode && r.Id != updatedSession.Id);
            if (duplicateSession != null) return duplicateSession;

            // Update Session
            try
            {
                context.Sessions.Where(r => r.Id == updatedSession.Id)
                .ExecuteUpdate(setter => setter
                    .SetProperty(r => r.EmployerId, updatedSession.EmployerId)
                    .SetProperty(r => r.SessionCode, updatedSession.SessionCode)
                    .SetProperty(r => r.JobPosition, updatedSession.JobPosition)
                    .SetProperty(r => r.JobRequirement, updatedSession.JobRequirement)
                    .SetProperty(r => r.JobResponsibilities, updatedSession.JobResponsibilities)
                    .SetProperty(r => r.CompanyCulture, updatedSession.CompanyCulture)
                    .SetProperty(r => r.Status, updatedSession.Status)
                    .SetProperty(r => r.QuestionString, updatedSession.QuestionString));
                return context.Sessions.Single(r => r.Id == updatedSession.Id);
            }
            catch
            {
                return null;
            }
        }

        public Session? DeleteSession(int Id)
        {
            using var context = new Context();
            var Session = context.Sessions.Find(Id);
            if (Session == null) return null;
            try
            {
                context.Sessions.Remove(Session);
                context.SaveChanges();
                return Session;
            }
            catch
            {
                return null;
            }
        }
    }
}
