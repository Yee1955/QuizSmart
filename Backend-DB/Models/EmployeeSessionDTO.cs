namespace Backend_DB.Models
{
    public partial class EmployeeSessionDTO
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
    }
}