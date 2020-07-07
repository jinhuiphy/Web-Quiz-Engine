package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {
    Page<CompletedQuiz> findAllByAuthorEmail(String authorEmail, Pageable pageable);
}
