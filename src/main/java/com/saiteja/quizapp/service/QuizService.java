package com.saiteja.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saiteja.quizapp.Question;
import com.saiteja.quizapp.QuestionWrapper;
import com.saiteja.quizapp.Quiz;
import com.saiteja.quizapp.Response;
import com.saiteja.quizapp.dao.QuestionDao;
import com.saiteja.quizapp.dao.QuizDao;

@Service
public class QuizService {
	
	@Autowired
	QuizDao quizDao;
	
	@Autowired
	QuestionDao questionDao;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		
		List<Question> questions = questionDao.findQuestionsByCategory(category, numQ);
		
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestions(questions);
		quizDao.save(quiz);
		
		return new ResponseEntity<>("Success",HttpStatus.CREATED);
		
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
		
		Optional<Quiz> quiz = quizDao.findById(id);
		List<Question> questionsFromDB = quiz.get().getQuestions();
		List<QuestionWrapper> questionsForUser = new ArrayList<>();
		
		for(Question q : questionsFromDB)
		{
			QuestionWrapper qw = new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3());
			questionsForUser.add(qw);
		}
		
		return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
	}

	public ResponseEntity<Integer> calculateQuestions(int id, List<Response> responses) {
		
		Quiz quiz = quizDao.findById(id).get();
		List<Question> questions = quiz.getQuestions();
		int right = 0;
		int i = 0;
		
		for(Response response : responses)
		{
			if(response.getResponse().equals(questions.get(i).getRightAnswer()));
			right++;
			i++;
		}
		
		return new ResponseEntity<>(right,HttpStatus.OK);
	}

}
