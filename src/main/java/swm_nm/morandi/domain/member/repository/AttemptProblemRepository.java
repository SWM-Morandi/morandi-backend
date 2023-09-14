package swm_nm.morandi.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.test.entity.Test;
import swm_nm.morandi.domain.testResult.entity.AttemptProblem;

import java.util.List;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
    List<AttemptProblem> findAllByTestOrderByAttemptProblemIdAsc(Test test);
    List<AttemptProblem> findAttemptProblemsByTest_TestId(Long testId);

    //List<AttemptProblem> findAttemptProblemsByTest_TestIdOrderByAttemptProblemIdAsc(Long testId);
}

