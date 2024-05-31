using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/employee-session")]
public class EmployeeSessionsController : ControllerBase
{
    private readonly EmployeeSessionEF _EmployeeSessionsRepo;
    public EmployeeSessionsController()
    {
        _EmployeeSessionsRepo = new EmployeeSessionEF();
    }

    [HttpGet("")]
    public IEnumerable<EmployeeSession> GetAllEmployeeSessions()
    {
        return _EmployeeSessionsRepo.GetEmployeeSessions();
    }

    [HttpGet("{id}", Name = "GetEmployeeSessionSession")]
    public IActionResult GetEmployeeSessionById(int id)
    {
        var EmployeeSession = _EmployeeSessionsRepo.GetEmployeeSession(id);
        return EmployeeSession != null  ? Ok(EmployeeSession) : NotFound();
    }

    [HttpPost()]
    public IActionResult AddEmployeeSession(EmployeeSession newEmployeeSession)
    {
        if (newEmployeeSession == null) return BadRequest();
            
        var(EmployeeSession, isInserted) = _EmployeeSessionsRepo.InsertEmployeeSession(newEmployeeSession);
        if (EmployeeSession == null) return BadRequest();
        if (!isInserted) return Conflict(EmployeeSession);

        // Construct the URL of the newly created resource
        var newResourceUrl = $"{Request.Scheme}://{Request.Host}{Request.PathBase}/api/EmployeeSessions/{EmployeeSession.Id}";
        Response.Headers["Location"] = newResourceUrl;
        return Created(newResourceUrl, EmployeeSession);
    }

    [HttpDelete("{id}")]
    public IActionResult DeleteEmployeeSession(int id)
    {
        var deletedEmployeeSession = _EmployeeSessionsRepo.DeleteEmployeeSession(id);
        return (deletedEmployeeSession != null) ? 
            Ok(deletedEmployeeSession) : NotFound();
    }

    [HttpPut("{id}")]
    public IActionResult UpdateEmployeeSession(int id, EmployeeSession updatedEmployeeSession)
    {
        updatedEmployeeSession.Id = id;
        EmployeeSession? returnedEmployeeSession =  _EmployeeSessionsRepo.UpdateEmployeeSession(updatedEmployeeSession);
        if (returnedEmployeeSession == null) return NotFound();
        return returnedEmployeeSession.Id == id ? Ok(returnedEmployeeSession) : Conflict(returnedEmployeeSession);
    }
}
