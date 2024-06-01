using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/session")]
public class SessionsController : ControllerBase
{
    private readonly SessionEF _SessionsRepo;
    private readonly EmployeeSessionEF _EmployeeSessionRepo;
    public SessionsController()
    {
        _SessionsRepo = new SessionEF();
        _EmployeeSessionRepo = new EmployeeSessionEF();
    }

    [HttpGet("")]
    public IEnumerable<Session> GetAllSessions()
    {
        return _SessionsRepo.GetSessions();
    }

    [HttpGet("{id}", Name = "GetSession")]
    public IActionResult GetSessionById(int id)
    {
        var Session = _SessionsRepo.GetSession(id);
        return Session != null  ? Ok(Session) : NotFound();
    }

    [HttpGet("{id}/employee-session")]
    public IActionResult GetEmployeeSessionsBySessionId(int id)
    {
        var allEmployeeSessions = _EmployeeSessionRepo.GetEmployeeSessions();
        var employeeSessions = allEmployeeSessions.Where(es => es.SessionId == id).ToList();
        if (employeeSessions.Count == 0)
        {
            return NotFound($"No employee sessions found for session ID: {id}");
        }
        return Ok(employeeSessions);
    }

    [HttpGet("{id}/completed")]
    public IActionResult GetNumOfCompleted(int id)
    {
        var allEmployeeSessions = _EmployeeSessionRepo.GetEmployeeSessions();
        var employeeSessions = allEmployeeSessions.Where(es => es.SessionId == id).ToList();
        var completedSessions = employeeSessions.Where(es => es.Status == "Completed");

        return Ok(completedSessions.Count());
    }

    [HttpGet("session-code/{sessionCode}")]
    public IActionResult GetSessionByCode(string sessionCode)
    {
        Console.WriteLine("Received: " + sessionCode);
        var sessions = _SessionsRepo.GetSessions();
        var session = sessions.FirstOrDefault(s => s.SessionCode.Equals(sessionCode) && s.Status.Equals("Started"));
        if (session == null)
        {
            return NotFound("Session not found with the provided code.");
        }
        return Ok(session);
    }

    [HttpPost()]
    public IActionResult AddSession(SessionDTO newSessionDTO)
    {
        var newSession = new Session
        {
            EmployerId = newSessionDTO.EmployerId,
            SessionCode = newSessionDTO.SessionCode,
            JobPosition = newSessionDTO.JobPosition,
            JobRequirement = newSessionDTO.JobRequirement,
            JobResponsibilities = newSessionDTO.JobResponsibilities,
            CompanyCulture = newSessionDTO.CompanyCulture,
            Status = newSessionDTO.Status,
            Date = newSessionDTO.Date,
            QuestionString = newSessionDTO.QuestionString
            
        };
        if (newSession == null) return BadRequest();
            
        var(Session, isInserted) = _SessionsRepo.InsertSession(newSession);
        if (Session == null) return BadRequest();
        if (!isInserted) return Conflict(Session);

        // Construct the URL of the newly created resource
        var newResourceUrl = $"{Request.Scheme}://{Request.Host}{Request.PathBase}/api/Sessions/{Session.Id}";
        Response.Headers["Location"] = newResourceUrl;
        return Created(newResourceUrl, Session);
    }

    [HttpDelete("{id}")]
    public IActionResult DeleteSession(int id)
    {
        var deletedSession = _SessionsRepo.DeleteSession(id);
        return (deletedSession != null) ? 
            Ok(deletedSession) : NotFound();
    }

    [HttpPut("{id}")]
    public IActionResult UpdateSession(int id, SessionDTO updatedSessionDTO)
    {
        var updatedSession = new Session
        {
            EmployerId = updatedSessionDTO.EmployerId,
            SessionCode = updatedSessionDTO.SessionCode,
            JobPosition = updatedSessionDTO.JobPosition,
            JobRequirement = updatedSessionDTO.JobRequirement,
            JobResponsibilities = updatedSessionDTO.JobResponsibilities,
            CompanyCulture = updatedSessionDTO.CompanyCulture,
            Status = updatedSessionDTO.Status,
            Date = updatedSessionDTO.Date,
            QuestionString = updatedSessionDTO.QuestionString
            
        };
        updatedSession.Id = id;
        Session? returnedSession =  _SessionsRepo.UpdateSession(updatedSession);
        if (returnedSession == null) return NotFound();
        return returnedSession.Id == id ? Ok(returnedSession) : Conflict(returnedSession);
    }
}
