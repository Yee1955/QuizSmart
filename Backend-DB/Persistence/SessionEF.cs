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

            // Ensure the session code is unique among sessions with the status "Started"
            bool isUniqueCode;
            string sessionCode;

            do
            {
                sessionCode = GenerateSessionCode();
                isUniqueCode = !context.Sessions.Any(s => s.SessionCode == sessionCode && s.Status == "Started");
            } while (!isUniqueCode);

            Session.SessionCode = sessionCode;

            // Insert Session
            try
            {
                context.Sessions.Add(Session);
                context.SaveChanges();  // Update the changes to object
                return (Session, true);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Exception: {ex.Message}");
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
        
        private string GenerateSessionCode()
        {
            // Generate a session code in the format '#F1JHD'
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            var random = new Random();
            var code = new char[6];
            for (int i = 0; i < code.Length; i++)
            {
                code[i] = chars[random.Next(chars.Length)];
            }
            return $"{new string(code)}";
        }

    }
}
