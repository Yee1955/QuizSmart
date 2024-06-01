namespace Backend_DB.Models; 
public partial class EmployeeDTO
{
    public int Id { get; set; }
    public string Email { get; set; } = null!;
    public string FullName { get; set; } = null!;
    public string Password { get; set; } = null!;
}