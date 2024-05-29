using Microsoft.EntityFrameworkCore;
using Backend_DB.Models;

namespace Backend_DB.Persistence
{
    public class EmployeeSessionEF
    {
        public EmployeeSession? GetEmployeeSession(int Id)
        {
            using var context = new Context();
            return context.EmployeeSessions.Find(Id);
        }

        public List<EmployeeSession> GetEmployeeSessions()
        {
            using var context = new Context();
            return context.EmployeeSessions.ToList();
        }

        public (EmployeeSession?, bool) InsertEmployeeSession(EmployeeSession EmployeeSession)
        {
            using var context = new Context();

            // Insert EmployeeSession
            try
            {
                context.EmployeeSessions.Add(EmployeeSession);
                context.SaveChanges();  // Update the changes to object
                return (EmployeeSession, true);
            }
            catch
            {
                return (null, false);
            }
        }

        public EmployeeSession? UpdateEmployeeSession(EmployeeSession updatedEmployeeSession)
        {
            using var context = new Context();

            // Update EmployeeSession
            try
            {
                context.EmployeeSessions.Where(r => r.Id == updatedEmployeeSession.Id)
                .ExecuteUpdate(setter => setter
                    .SetProperty(r => r.EmployeeId, updatedEmployeeSession.EmployeeId)
                    .SetProperty(r => r.SessionId, updatedEmployeeSession.SessionId)
                    .SetProperty(r => r.Progress, updatedEmployeeSession.Progress)
                    .SetProperty(r => r.Status, updatedEmployeeSession.Status)
                    .SetProperty(r => r.AnswerString, updatedEmployeeSession.AnswerString)
                    .SetProperty(r => r.ScoreAlignment, updatedEmployeeSession.ScoreAlignment)
                    .SetProperty(r => r.ScoreProblemSolving, updatedEmployeeSession.ScoreProblemSolving)
                    .SetProperty(r => r.ScoreCommunication, updatedEmployeeSession.ScoreCommunication)
                    .SetProperty(r => r.ScoreInnovation, updatedEmployeeSession.ScoreInnovation)
                    .SetProperty(r => r.ScoreTeamFit, updatedEmployeeSession.ScoreTeamFit)
                    .SetProperty(r => r.Summary, updatedEmployeeSession.Summary));
                return context.EmployeeSessions.Single(r => r.Id == updatedEmployeeSession.Id);
            }
            catch
            {
                return null;
            }
        }

        public EmployeeSession? DeleteEmployeeSession(int Id)
        {
            using var context = new Context();
            var EmployeeSession = context.EmployeeSessions.Find(Id);
            if (EmployeeSession == null) return null;
            try
            {
                context.EmployeeSessions.Remove(EmployeeSession);
                context.SaveChanges();
                return EmployeeSession;
            }
            catch
            {
                return null;
            }
        }
    }
}
