package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import swm_nm.morandi.domain.testExit.dto.AttemptProblemDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testRecord.dto.TestRecordDto;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.testRecord.mapper.TestRecordMapper;
import swm_nm.morandi.domain.testInfo.repository.TestRepository;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LatestTestInfoService {

    private final TestRepository testRepository;

    private final AttemptProblemRepository attemptProblemRepository;

    public List<TestRecordDto> getTestRecordDtosLatest() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Pageable pageable = PageRequest.of(0, 4);
        List<Tests> recentTests = testRepository.findAllByMember_MemberIdOrderByTestDateDesc(memberId, pageable);
        List<AttemptProblemDto> attemptProblemDtos = getAttemptProblemDtos(recentTests);
        List<TestRecordDto> testRecordDtos = recentTests.stream().map((Tests test) ->
                TestRecordMapper.convertToDto(test, attemptProblemDtos)).collect(Collectors.toList());
        getTestRecordDtos(testRecordDtos);

        return testRecordDtos;
    }

    private List<AttemptProblemDto> getAttemptProblemDtos(List<Tests> recentTests) {
        List<AttemptProblemDto> attemptProblemDtos = new ArrayList<>();
        recentTests.stream().map(Tests::getTestId).map(attemptProblemRepository::findAttemptProblemsByTest_TestId)
                .forEach(attemptProblems -> attemptProblems.forEach(attemptProblem -> {
            attemptProblemDtos.add(AttemptProblemDto.getAttemptProblemDto(attemptProblem));
        }));
        return attemptProblemDtos;
    }

    private static void getTestRecordDtos(List<TestRecordDto> testRecordDtos) {
        while (testRecordDtos.size() < 4) {
            testRecordDtos.add(TestRecordMapper.dummyDto());
        }
    }
}
