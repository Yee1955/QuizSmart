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

    [HttpPost()]
    public IActionResult AddEmployer(EmployerDTO newEmployerDTO)
    {
        var newEmployer = new Employer
        {
            Email = newEmployerDTO.Email,
            CompanyName = newEmployerDTO.CompanyName,
            Password = newEmployerDTO.Password,
        };
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
    public IActionResult UpdateEmployer(int id, EmployerDTO updatedEmployerDTO)
    {
        var updatedEmployer = new Employer
        {
            Id = id,
            Email = updatedEmployerDTO.Email,
            CompanyName = updatedEmployerDTO.CompanyName,
            Password = updatedEmployerDTO.Password,
        };
        updatedEmployer.Id = id;
        Employer? returnedEmployer =  _EmployersRepo.UpdateEmployer(updatedEmployer);
        if (returnedEmployer == null) return NotFound();
        return returnedEmployer.Id == id ? Ok(returnedEmployer) : Conflict(returnedEmployer);
    }
}
