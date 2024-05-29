using System;
using System.Collections.Generic;

namespace Backend_DB.Models
{
    public partial class EmployeeSession
    {
        public int Id { get; set; }
        public int EmployeeId { get; set; }
        public int SessionId { get; set; }
        public int Progress { get; set; }
        public string Status { get; set; } = null!;
        public string? AnswerString { get; set; }
        public double? ScoreAlignment { get; set; }
        public double? ScoreProblemSolving { get; set; }
        public double? ScoreCommunication { get; set; }
        public double? ScoreInnovation { get; set; }
        public double? ScoreTeamFit { get; set; }
        public string? Summary { get; set; }

        public virtual Employee Employee { get; set; } = null!;
        public virtual Session Session { get; set; } = null!;
        public EmployeeSession()
        {
                
        }
        
        public EmployeeSession(int id, int employeeId, int sessionId, int progress, string status, string? answerString, double? scoreAlignment, double? scoreProblemSolving, double? scoreCommunication, double? scoreInnovation, double? scoreTeamFit,  string? summary)
        {
            Id = id;
            EmployeeId = employeeId;
            SessionId = sessionId;
            Progress = progress;
            Status = status;
            AnswerString = answerString;
            ScoreAlignment = scoreAlignment;
            ScoreProblemSolving = scoreProblemSolving;
            ScoreCommunication = scoreCommunication;
            ScoreInnovation = scoreInnovation;
            ScoreTeamFit = scoreTeamFit;
            Summary = summary;
        }
    }
}
