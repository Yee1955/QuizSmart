using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/login")]
public class LoginController : ControllerBase
{
    private readonly EmployerEF _EmployersRepo;
    private readonly EmployeeEF _EmployeesRepo;
    public LoginController()
    {
        _EmployersRepo = new EmployerEF();
        _EmployeesRepo = new EmployeeEF();
    }

    [HttpPost("")]
    public IActionResult VerifyLogin([FromBody] LoginRequest loginRequest)
    {
        if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Email) || string.IsNullOrEmpty(loginRequest.Password))
        {
            return BadRequest("Invalid login request.");
        }

        // Check if the user is an Employer
        var employer = _EmployersRepo.GetEmployers().FirstOrDefault(e => e.Email == loginRequest.Email && e.Password == loginRequest.Password);
        if (employer != null)
        {
            return Ok(new { Message = "Login successful", UserType = "Employer", User = employer });
        }

        // Check if the user is an Employee
        var employee = _EmployeesRepo.GetEmployees().FirstOrDefault(e => e.Email == loginRequest.Email && e.Password == loginRequest.Password);
        if (employee != null)
        {
            return Ok(new { Message = "Login successful", UserType = "Employee", User = employee });
        }

        // If no user is found
        return Unauthorized("Invalid email or password.");
    }

}
