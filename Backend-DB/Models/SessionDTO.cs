public class SessionDTO
{
    public int Id { get; set; }
    public int EmployerId { get; set; }
    public string SessionCode { get; set; }
    public string JobPosition { get; set; }
    public string JobRequirement { get; set; }
    public string JobResponsibilities { get; set; }
    public string CompanyCulture { get; set; }
    public string Status { get; set; }
    public DateTime Date { get; set; }
    public string? QuestionString { get; set; }
}