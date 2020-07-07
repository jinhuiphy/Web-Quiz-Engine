package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
public class QuizController {
    @Autowired
    QuizService quizService;
    @Autowired
    CompletedQuizService completedQuizService;

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody QuizDao quizDao, Principal principal) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDao.getTitle());
        quiz.setText(quizDao.getText());
        quiz.setOptions(quizDao.getOptions());
        quiz.setAnswer(quizDao.getAnswer());
        quiz.setAuthorEmail(principal.getName());
        quizService.save(quiz);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @GetMapping(path = "/api/quizzes")
    public ResponseEntity<Page<Quiz>> getQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        Page<Quiz> list = quizService.getAllQuizzes(page, pageSize, sortBy);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@PathVariable Long id, @RequestBody Answer answer, Principal principal) {
        Quiz quiz = quizService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (quiz.checkAnswer(answer)) {
            CompletedQuiz completedQuiz = new CompletedQuiz();

            completedQuiz.setQuizId(quiz.getId());
            completedQuiz.setCompletedAt(LocalDateTime.now());
            completedQuiz.setAuthorEmail(principal.getName());
            completedQuizService.save(completedQuiz);

            return new ResponseEntity<>(Result.CORRECT, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Result.WRONG, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, Principal principal) {
        Quiz quiz = quizService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (quiz.getAuthorEmail().equals(principal.getName())) {
            quizService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/api/quizzes/completed")
    public ResponseEntity<Page<CompletedQuiz>> getAllCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(defaultValue = "completedAt") String sortBy,
                                                                      Principal principal) {
        Page<CompletedQuiz> list = completedQuizService.getAllCompletedQuizzes(page, pageSize, sortBy, principal);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
