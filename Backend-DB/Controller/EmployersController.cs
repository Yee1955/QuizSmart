using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/employer")]
public class EmployersController : ControllerBase
{
    private readonly EmployerEF _EmployersRepo;
    private readonly EmployeeEF _EmployeesRepo;
    private readonly SessionEF _SessionsRepo;
    public EmployersController()
    {
        _EmployersRepo = new EmployerEF();
        _EmployeesRepo = new EmployeeEF();
        _SessionsRepo = new SessionEF();
    }

    [HttpGet("")]
    public IEnumerable<Employer> GetAllEmployers()
    {
        return _EmployersRepo.GetEmployers();
    }

    [HttpGet("{id}", Name = "GetEmployer")]
    public IActionResult GetEmployerById(int id)
    {
        var Employer = _EmployersRepo.GetEmployer(id);
        return Employer != null  ? Ok(Employer) : NotFound();
    }

    [HttpGet("{id}/sessions")]
    public IEnumerable<Session> GetEmployerSession(int id)
    {
        return _EmployersRepo.GetEmployerSession(id);
    }

    [HttpGet("{id}/employees")]
    public IActionResult GetEmployeesByEmployer(int id)
    {
        var sessions = _EmployersRepo.GetEmployerSession(id);
        var employeeIds = sessions.SelectMany(s => s.EmployeeSessions)
                                .Select(es => es.EmployeeId)
                                .Distinct();

        List<Employee> employees = new List<Employee>();
        foreach (var employeeId in employeeIds)
        {
            var employee = _EmployeesRepo.GetEmployee(employeeId);
            if (employee != null)
            {
                employees.Add(employee);
            }
        }

        return Ok(employees);
    }

    [HttpPost()]
    public IActionResult AddEmployer(Employer newEmployer)
    {
        if (newEmployer == null) return BadRequest();
            
        var(Employer, isInserted) = _EmployersRepo.InsertEmployer(newEmployer);
        if (Employer == null) return BadRequest();
        if (!isInserted) return Conflict(Employer);

        // Construct the URL of the newly created resource
        var newResourceUrl = $"{Request.Scheme}://{Request.Host}{Request.PathBase}/api/Employers/{Employer.Id}";
        Response.Headers["Location"] = newResourceUrl;
        return Created(newResourceUrl, Employer);
    }

    [HttpDelete("{id}")]
    public IActionResult DeleteEmployer(int id)
    {
        var deletedEmployer = _EmployersRepo.DeleteEmployer(id);
        return (deletedEmployer != null) ? 
            Ok(deletedEmployer) : NotFound();
    }

    [HttpPut("{id}")]
    public IActionResult UpdateEmployer(int id, Employer updatedEmployer)
    {
        updatedEmployer.Id = id;
        Employer? returnedEmployer =  _EmployersRepo.UpdateEmployer(updatedEmployer);
        if (returnedEmployer == null) return NotFound();
        return returnedEmployer.Id == id ? Ok(returnedEmployer) : Conflict(returnedEmployer);
    }
}
