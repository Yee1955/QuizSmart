using Backend_DB.Persistence;
using Backend_DB.Models;
using Microsoft.AspNetCore.Mvc;

namespace Backend_DB.Controllers;

[ApiController]
[Route("api/session")]
public class SessionsController : ControllerBase
{
    private readonly SessionEF _SessionsRepo;
    public SessionsController()
    {
        _SessionsRepo = new SessionEF();
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

    [HttpPost()]
    public IActionResult AddSession(Session newSession)
    {
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
    public IActionResult UpdateSession(int id, Session updatedSession)
    {
        updatedSession.Id = id;
        Session? returnedSession =  _SessionsRepo.UpdateSession(updatedSession);
        if (returnedSession == null) return NotFound();
        return returnedSession.Id == id ? Ok(returnedSession) : Conflict(returnedSession);
    }
}
