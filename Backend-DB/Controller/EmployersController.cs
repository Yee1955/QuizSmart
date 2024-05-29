using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/employer")]
public class EmployersController : ControllerBase
{
    private readonly EmployerEF _EmployersRepo;
    public EmployersController()
    {
        _EmployersRepo = new EmployerEF();
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
