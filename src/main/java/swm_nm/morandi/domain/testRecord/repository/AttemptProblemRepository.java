package swm_nm.morandi.domain.testRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm_nm.morandi.domain.testExit.entity.Tests;
import swm_nm.morandi.domain.testExit.entity.AttemptProblem;

import java.util.List;

public interface AttemptProblemRepository extends JpaRepository<AttemptProblem, Long> {
    List<AttemptProblem> findAllByMember_MemberId(Long memberId);
    List<AttemptProblem> findAllByTest_TestId(Long testId);
    List<AttemptProblem> findAllByTestOrderByAttemptProblemIdAsc(Tests test);
    List<AttemptProblem> findAttemptProblemsByTest_TestId(Long testId);

    //List<AttemptProblem> findAttemptProblemsByTest_TestIdOrderByAttemptProblemIdAsc(Long testId);
}

