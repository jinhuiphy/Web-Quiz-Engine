package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Page<Quiz> getAllQuizzes(Integer page, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));

        Page<Quiz> pagedResult = quizRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return Page.empty();
        }
    }

    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    public void deleteById(Long id) {
        quizRepository.deleteById(id);
    }

    public void save(Quiz quiz) {
        quizRepository.save(quiz);
    }
}
