namespace Backend_DB.Models
{
    public partial class EmployerDTO
    {
        public int Id { get; set; }
        public string Email { get; set; } = null!;
        public string CompanyName { get; set; } = null!;
        public string Password { get; set; } = null!;
    }
}