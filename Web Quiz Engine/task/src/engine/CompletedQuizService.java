package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CompletedQuizService {
    @Autowired
    private CompletedQuizRepository completedQuizRepository;

    public Page<CompletedQuiz> getAllCompletedQuizzes(Integer page, Integer pageSize, String sortBy, Principal principal) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());

        Page<CompletedQuiz> pagedResult = completedQuizRepository.findAllByAuthorEmail(principal.getName(), paging);

        if (pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return Page.empty();
        }
    }

    public void save(CompletedQuiz completedQuiz) {
        completedQuizRepository.save(completedQuiz);
    }
}
