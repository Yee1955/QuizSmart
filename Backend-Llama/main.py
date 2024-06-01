from flask import Flask, request, jsonify
from datetime import datetime
import socket
import argparse
import os
from gradientai import Gradient
import re

app = Flask(__name__)
base_model = None
token = 'P3aDzHuzYAYDPE04jEPdS2CBycTcGQ57'
workspace_id = '369327cf-280f-4d9c-b15a-9c022e5fd709_workspace'

os.environ['GRADIENT_ACCESS_TOKEN'] = token
os.environ['GRADIENT_WORKSPACE_ID'] = workspace_id

def prepareLlamaBot():
    global base_model
    gradient = Gradient()
    base_model = gradient.get_base_model(base_model_slug="llama3-8b-chat")

def generate_questions1(job_title, job_requirements, job_responsibilities, company_culture, model_adapter):
    query = (
        f"[INST]Generate 5 open-ended multiple-choice questions designed to evaluate candidates for the job position "
        f"based on the provided job title, requirements, responsibilities, and company culture. "
        f"Each question should include options for personal answers and assess the candidate on one or more of the following indices: "
        f"Alignment with Job Requirements and Problem-Solving. For each question, "
        f"include three suggested response options that reflect different levels of suitability for the role. "
        f"Ensure the formatting is compact with minimal use of newline characters. Responses should be concise, "
        f"and the entire response should not exceed 5-10 words per instructional comment if information is insufficient for a question. "
        f"Format your output strictly as follows (with no extra newlines or spaces):\n\n"
        f"QUESTION: [Your question or short comment]\n"
        f"OPTION A: [First option or blank]\n"
        f"OPTION B: [Second option or blank]\n"
        f"OPTION C: [Third option or blank]\n\n"
        f"Here are the job details:\n"
        f"Job Title: {job_title}\n"
        f"Job Requirements: {job_requirements}\n"
        f"Job Responsibilities: {job_responsibilities}\n"
        f"Company Culture: {company_culture}\n"
        f"[/INST]"
    )
    response = model_adapter.complete(query=query, max_generated_token_count=500).generated_output
    return response

def generate_questions2(job_title, job_requirements, job_responsibilities, company_culture, model_adapter):
    query = (
        f"[INST]Generate 5 open-ended multiple-choice questions designed to evaluate candidates for the job position "
        f"based on the provided job title, requirements, responsibilities, and company culture. "
        f"Each question should include options for personal answers and assess the candidate on one or more of the following indices: "
        f"Critical Thinking, Communication Skills, Innovation and Creativity, Cultural and Team Fit. For each question, "
        f"include three suggested response options that reflect different levels of suitability for the role. "
        f"Ensure the formatting is compact with minimal use of newline characters. Responses should be concise, "
        f"and the entire response should not exceed 5-10 words per instructional comment if information is insufficient for a question. "
        f"Format your output strictly as follows (with no extra newlines or spaces):\n\n"
        f"QUESTION: [Your question or short comment]\n"
        f"OPTION A: [First option or blank]\n"
        f"OPTION B: [Second option or blank]\n"
        f"OPTION C: [Third option or blank]\n\n"
        f"Here are the job details:\n"
        f"Job Title: {job_title}\n"
        f"Job Requirements: {job_requirements}\n"
        f"Job Responsibilities: {job_responsibilities}\n"
        f"Company Culture: {company_culture}\n"
        f"[/INST]"
    )
    response = model_adapter.complete(query=query, max_generated_token_count=500).generated_output
    return response

def process_questions(respoonse_text):
    questions = []
    pattern = re.compile(r'QUESTION: (.*?)\nOPTION A: (.*?)\nOPTION B: (.*?)\nOPTION C: (.*?)\n',re.DOTALL)
    matches = pattern.findall(respoonse_text)
    if len(matches) == 10:
        for match in matches:
            question = match[0].strip()
            options = match[1].strip(), match[2].strip(), match[3].strip()
            
            question_data = {
                "question": question,
                "options": options
            }
            questions.append(question_data)
    else:
        match = re.match(r'([^.!?]*[.!?])', respoonse_text)
        if match:
            question_data = {
                "question": "The provided information is insufficient",
                "options": []
        }
        questions.append(question_data)

    return questions

def evaluateCandidateResponses(questionString, answerString, jobDetailsString, model_adapter):
    questions = questionString.split("---")
    answers = answerString.split("---")
    jobDetails = jobDetailsString.split("---")
    
    # Generate the formatted text for the candidate responses
    candidate_responses = "\n".join([f"- **Response to Question {i+1} ({q.strip()}):** {a.strip()}" for i, (q, a) in enumerate(zip(questions, answers))])
    
    # Create the query text
    query = (
        f"[INST]Please evaluate the responses provided by a candidate to the open-ended multiple-choice questions based "
        f"on their alignment with the following five scoring indices. Conclude with a numerical score out of 5 for each index. The scores should reflect "
        f"the candidate's competencies and alignment with the job position and company culture.\n\n"
        f"### Candidate Responses:\n{candidate_responses}\n\n"
        f"### Job Details:\n"
        f"- **Job Title**: {jobDetails[0]}\n"
        f"- **Job Requirements**: {jobDetails[1]}\n"
        f"- **Job Responsibilities**: {jobDetails[2]}\n"
        f"- **Company Culture**: {jobDetails[3]}\n"
        f"\nEvaluate the candidateCultural and Team Fit on these indices and format your response as below:\n"
        f"**Alignment with Job Requirements: [0.00 to 5.00]**\n"
        f"**Problem Solving and Critical Thinking: [0.00 to 5.00]**\n"
        f"**Communication Skills: [0.00 to 5.00]**\n"
        f"**Innovation and Creativity: [0.00 to 5.00]**\n"
        f"**: [0.00 to 5.00]**\n"
        f"@@Overall Summary: [No More Than 15 Words Summary]**\n"
        f"[/INST]"
    )
    
    # Assuming model_adapter.complete() correctly interfaces with your model and retrieves the response
    response = model_adapter.complete(query=query, max_generated_token_count=500).generated_output
    return response

def process_evaluation(evaluation_text):
    results = {
        "scores": {},
        "summary": ""
    }
    scores_pattern = re.compile(r'\*\*([\w\s]+?):\s+([0-5]\.[0-9]{2})\*\*')
    matches = scores_pattern.findall(evaluation_text)
    for match in matches:
        index_name = ' '.join(match[0].strip().split())
        score = float(match[1])
        results["scores"][index_name] = score
    summary_pattern = re.compile(r'\*\*Overall Summary:\*\*\s*(.+)', re.DOTALL)
    summary_match = summary_pattern.search(evaluation_text)
    if summary_match:
        results["summary"] = summary_match.group(1).strip()
    
    return results


@app.route('/')
def index():
    return "Welcome to the Flask API!"

@app.route('/chat', methods=['POST'])
def chat():
    global base_model
    data = request.get_json()

    input_message = data.get('inputMessage', '')

    # Check if the ingetQuestionsput message is empty
    if not input_message.strip():
        return jsonify({'error': 'Input message is empty', 'response': None}), 400

    QUERY = f"[INST]{data}[/INST]"
    response = base_model.complete(query=QUERY, max_generated_token_count=500).generated_output
    parts = response.split("\n\n", 1)  # Split only at the first occurrence of double new lines
    if len(parts) > 1:
        processed_response = parts[1]
    else:
        processed_response = response
    print(processed_response)
    return jsonify({'response': processed_response}), 200

@app.route('/getQuiz', methods=['GET'])
def get_quiz():
    global base_model
    if base_model is None:
        return jsonify({'error': 'Model adapter not initialized'}), 500
    job_title = request.args.get('jobTitle')
    job_requirements = request.args.get('jobRequirements')
    job_responsibilities = request.args.get('jobResponsibilities')
    company_culture = request.args.get('companyCulture')
    
    # Check if any of the parameters are missing
    if not job_title:
        print("Missing job title parameter")
        return jsonify({'error': 'Missing job title parameter'}), 400
    if not job_requirements:
        print("Missing job requirements parameter")
        return jsonify({'error': 'Missing job requirements parameter'}), 400
    if not job_responsibilities:
        print("Missing job responsibilities parameter")
        return jsonify({'error': 'Missing job responsibilities parameter'}), 400
    if not company_culture:
        print("Missing company culture parameter")
        return jsonify({'error': 'Missing company culture parameter'}), 400
    quiz = generate_questions1(job_title, job_requirements, job_responsibilities, company_culture, base_model)
    quiz = quiz + "\n" + generate_questions2(job_title, job_requirements, job_responsibilities, company_culture, base_model) + "\n"
    processed_quiz = process_questions(quiz)
    
    # Print the processed quiz data
    print("Quiz: ", quiz)
    print("Proccessed: ", processed_quiz)
    return jsonify(process_questions(quiz)), 200

@app.route('/evaluate', methods=['POST'])
def get_evaluation():
    global base_model
    data = request.get_json()

    questionString = data.get('questionString')
    answerString = data.get('answerString')
    jobDetailsString = data.get('jobDetailsString')

    if base_model is None:
        return jsonify({'error': 'Model adapter not initialized'}), 500
    
    # Check if any of the parameters are missing
    if not questionString:
        print("Missing questionString body")
        return jsonify({'error': 'Missing questionString body'}), 400
    if not answerString:
        print("Missing answerString body")
        return jsonify({'error': 'Missing answerString body'}), 400
    if not jobDetailsString:
        print("Missing jobDetailsString body")
        return jsonify({'error': 'Missing jobDetailsString body'}), 400
    evaluation_text = evaluateCandidateResponses(questionString, answerString, jobDetailsString, base_model)
    print(evaluation_text)
    return jsonify(process_evaluation(evaluation_text)), 200

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', type=int, default=5000, help='Specify the port number')
    args = parser.parse_args()

    port_num = args.port
    print("Starting Llama bot...\n This may take a while.")
    prepareLlamaBot()
    print(f"App running on port {port_num}")
    app.run(port=port_num)