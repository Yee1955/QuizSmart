using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/employee")]
public class EmployeesController : ControllerBase
{
    private readonly EmployeeEF _EmployeesRepo;
    public EmployeesController()
    {
        _EmployeesRepo = new EmployeeEF();
    }

    [HttpGet("")]
    public IEnumerable<Employee> GetAllEmployees()
    {
        return _EmployeesRepo.GetEmployees();
    }

    [HttpGet("{id}", Name = "GetEmployee")]
    public IActionResult GetEmployeeById(int id)
    {
        var Employee = _EmployeesRepo.GetEmployee(id);
        return Employee != null  ? Ok(Employee) : NotFound();
    }

    [HttpPost()]
    public IActionResult AddEmployee(Employee newEmployee)
    {
        if (newEmployee == null) return BadRequest();
            
        var(Employee, isInserted) = _EmployeesRepo.InsertEmployee(newEmployee);
        if (Employee == null) return BadRequest();
        if (!isInserted) return Conflict(Employee);

        // Construct the URL of the newly created resource
        var newResourceUrl = $"{Request.Scheme}://{Request.Host}{Request.PathBase}/api/Employees/{Employee.Id}";
        Response.Headers["Location"] = newResourceUrl;
        return Created(newResourceUrl, Employee);
    }

    [HttpDelete("{id}")]
    public IActionResult DeleteEmployee(int id)
    {
        var deletedEmployee = _EmployeesRepo.DeleteEmployee(id);
        return (deletedEmployee != null) ? 
            Ok(deletedEmployee) : NotFound();
    }

    [HttpPut("{id}")]
    public IActionResult UpdateEmployee(int id, Employee updatedEmployee)
    {
        updatedEmployee.Id = id;
        Employee? returnedEmployee =  _EmployeesRepo.UpdateEmployee(updatedEmployee);
        if (returnedEmployee == null) return NotFound();
        return returnedEmployee.Id == id ? Ok(returnedEmployee) : Conflict(returnedEmployee);
    }
}
