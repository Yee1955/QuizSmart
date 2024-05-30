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
        f"Alignment with Job Requirements and Problem-Solving. For each question, include three suggested responses options "
        f"that reflect different levels of suitability for the role. If the information provided "
        f"is insufficient to generate meaningful questions, leave all blank empty and must response only 5-10 words comment never longer than that\n"
        f"Remove unnecessary response, strictly format your questions as follows:\n\n"
        f"QUESTION: [Your question or short comment here]\n"
        f"OPTION A: [First option or blank]\n"
        f"OPTION B: [Second option or blank]\n"
        f"OPTION C: [Third option or blank]\n"
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
        f"job position based on the provided job title, requirements, responsibilities, and company culture. "
        f"Each question should include options for personal answers and assess the candidate on one or more of the following indices: "
        f"Critical Thinking, Communication Skills, Innovation and Creativity, Cultural and Team Fit. For each question, "
        f"include three suggested responses options that reflect different levels of suitability for the role. If the information provided "
        f"is insufficient to generate meaningful questions, leave all blank empty and must response only 5-10 words comment never longer than that\n"
        f"Remove unnecessary response, strictly format your questions as follows:\n\n"
        f"QUESTION: [Your question or short comment here]\n"
        f"OPTION A: [First option or blank]\n"
        f"OPTION B: [Second option or blank]\n"
        f"OPTION C: [Third option or blank]\n"
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
    if matches:
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

def evaluateCandidateResponses(candidate_responses, job_details, model_adapter):
    query = (
        f"[INST]Please evaluate the responses provided by a candidate to the open-ended multiple-choice questions based "
        f"on their alignment with the following five scoring indices. Provide detailed feedback on each index based on "
        f"the responses given and conclude with a numerical score out of 5 for each index. The scores should reflect "
        f"the candidate's competencies and alignment with the job position and company culture.\n\n"
        f"### Candidate Responses:\n"
        + "\n".join([f"- **Response to Question {i+1}**: {resp}" for i, resp in enumerate(candidate_responses.values())]) +
        f"\n\n### Job Details:\n"
        f"- **Job Title**: {job_details['title']}\n"
        f"- **Job Requirements**: {job_details['requirements']}\n"
        f"- **Job Responsibilities**: {job_details['responsibilities']}\n"
        f"- **Company Culture**: {job_details['culture']}\n"
        f"[/INST]"
    )
    response = model_adapter.complete(query=query, max_generated_token_count=500).generated_output
    return response

def process_quiz(quiz_text):
    questions = []
    pattern = re.compile(r'QUESTION: (.+?)\n(?:OPTION A: (.+?)\n)+(?:OPTION B: (.+?)\n)+(?:OPTION C: (.+?)\n)+(?:OPTION D: (.+?)\n)+ANS: (.+?)', re.DOTALL)
    matches = pattern.findall(quiz_text)

    for match in matches:
        question = match[0].strip()
        options = match[1].strip(), match[2].strip(), match[3].strip(), match[4].strip()
        correct_ans = match[-1].strip()
        
        question_data = {
            "question": question,
            "options": options,
            "correct_answer": correct_ans
        }
        questions.append(question_data)
    
    return questions

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

    QUERY = f"[INST]{input_message}[/INST]"
    response = base_model.complete(query=QUERY, max_generated_token_count=500).generated_output
    return jsonify({'response': response}), 200

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
    return jsonify(process_questions(quiz)), 200

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--port', type=int, default=5000, help='Specify the port number')
    args = parser.parse_args()

    port_num = args.port
    print("Starting Llama bot...\n This may take a while.")
    prepareLlamaBot()
    print(f"App running on port {port_num}")
    app.run(port=port_num)