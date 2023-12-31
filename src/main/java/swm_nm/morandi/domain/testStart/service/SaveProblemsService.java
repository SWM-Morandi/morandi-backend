package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.problem.entity.Problem;
import swm_nm.morandi.domain.problem.repository.ProblemRepository;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.ProblemErrorCode;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveProblemsService {

    private final ProblemRepository problemRepository;

    private final AttemptProblemRepository attemptProblemRepository;
    @Transactional
    public List<Long> saveAttemptProblems(Member member, Tests test, List<BojProblem> bojProblems) {
        List<Long> bojProblemIds = new ArrayList<>();
        for (BojProblem bojProblem : bojProblems) {
            Problem problem = problemRepository.findProblemByBojProblemId(bojProblem.getProblemId())
                    .orElseThrow(()-> {
                        log.error("저장하려는 문제 정보를 찾지 못했습니다. 문제 번호: {}", bojProblem.getProblemId());
                        return new MorandiException(ProblemErrorCode.PROBLEM_NOT_FOUND);
                    });

            bojProblemIds.add(bojProblem.getProblemId());

            AttemptProblem attemptProblem = AttemptProblem.builder()
                    .isSolved(false)
                    .testDate(LocalDate.now())
                    .executionTime(null)
                    .member(member)
                    .problem(problem)
                    .build();
            //양방향 연관관계 설정
            attemptProblem.setTest(test);
            attemptProblemRepository.save(attemptProblem);

        }
        return bojProblemIds;
    }
}
