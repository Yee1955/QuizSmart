CREATE DATABASE sit305

DROP TABLE IF EXISTS employee_session;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS employer;

CREATE TABLE employee (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE employer (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE session (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    employer_id INT NOT NULL,
    session_code VARCHAR(10) NOT NULL,
    job_position VARCHAR(100) NOT NULL,
    job_requirement VARCHAR(1000) NOT NULL,
    job_responsibilities VARCHAR(1000) NOT NULL,
    company_culture VARCHAR(1000) NOT NULL,
    status VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL,
    question_string VARCHAR(100000) NULL,
    CONSTRAINT fk_employer
      FOREIGN KEY(employer_id) 
        REFERENCES employer(id),
    PRIMARY KEY(id)
);

CREATE TABLE employee_session (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    employee_id INT NOT NULL,
    session_id INT NOT NULL,
    progress INT NOT NULL,
    status VARCHAR(10) NOT NULL,
    answer_string VARCHAR(1000) NULL,
    score_alignment FLOAT NULL,
    score_problem_solving FLOAT NULL,
    score_communication FLOAT NULL,
    score_innovation FLOAT NULL,
    score_team_fit FLOAT NULL,
    summary VARCHAR(1000) NULL,
    CONSTRAINT fk_employee
      FOREIGN KEY(employee_id) 
        REFERENCES employee(id),
    CONSTRAINT fk_session
      FOREIGN KEY(session_id) 
        REFERENCES session(id),
    PRIMARY KEY(id)
);

INSERT INTO employee (email, full_name, password)
VALUES ('acom', 'Alice Chong', 12345);
INSERT INTO employer (email, company_name, password)
VALUES ('x.com', 'SpaceX', 123);
-- Inserting a dummy employee session
INSERT INTO employee_session (employee_id, session_id, progress, status, answer_string, score_alignment, score_problem_solving, score_communication, score_innovation, score_team_fit, summary)
VALUES (
    1,    -- Assuming 1 is a valid employee_id from the employee table
    1,    -- Assuming 1 is a valid session_id from the session table
    10,   -- Example progress percentage
    'Completed',  -- Example status
    'Answer to a question.',  -- Example answer string
    4.5,  -- Example score for alignment
    3.8,  -- Example score for problem solving
    4.2,  -- Example score for communication
    3.5,  -- Example score for innovation
    4.0,  -- Example score for team fit
    'Employee performed well in all areas.'  -- Example summary
);


DROP TABLE session
DROP TABLE employee_session