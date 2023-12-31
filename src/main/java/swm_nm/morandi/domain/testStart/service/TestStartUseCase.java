package swm_nm.morandi.domain.testStart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.aop.annotation.MemberLock;
import swm_nm.morandi.domain.problem.dto.BojProblem;
import swm_nm.morandi.domain.testDuring.dto.TempCodeDto;
import swm_nm.morandi.domain.testDuring.service.TempCodeService;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.domain.testStart.dto.BojProblemDto;
import swm_nm.morandi.domain.testStart.dto.TestCodeDto;
import swm_nm.morandi.domain.testStart.dto.TestStartResponseDto;
import swm_nm.morandi.domain.testInfo.entity.AttemptProblem;
import swm_nm.morandi.domain.testInfo.entity.Tests;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.member.repository.MemberRepository;
import swm_nm.morandi.domain.member.service.MemberInfoService;
import swm_nm.morandi.domain.testRecord.repository.AttemptProblemRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.*;
import swm_nm.morandi.global.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Slf4j
public class TestStartUseCase {

    private final AddTestService addTestService;
    private final GetProblemsService getProblemsService;
    private final SaveProblemsService saveProblemsService;
    private final TempCodeInitializer tempCodeInitializer;
    private final MemberInfoService memberInfoService;
    private final TempCodeService tempCodeService;
    private final TestTypeRepository testTypeRepository;
    private final TestProgressCheckService testProgressCheckService;
    private final MemberRepository memberRepository;
    private final AttemptProblemRepository attemptProblemRepository;

    //이미 테스트 중인지 확인
    @MemberLock
    @Transactional
    public TestStartResponseDto getTestStartsData(Long memberId, Long testTypeId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MorandiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // TODO Redis 이용하여
        // 현재 테스트가 진행중인지 확인하도록
        // 이미 테스트 중인지 확인
        Tests test = testProgressCheckService.getOngoingTest(member);
        if (test != null) {
            return getTestStartResponseDto(test);
        }
        TestType testType = testTypeRepository.findById(testTypeId)
                .orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        // 현재 진행중인 테스트가 없을 경우 테스트 타입에 맞는 테스트 시작
        test = addTestService.startTestByTestTypeId(testType, member);

        String bojId = memberInfoService.getMemberInfo().getBojId();

        // 테스트 시작시 문제 가져오기
        // getProblemsService.getProblemsByTestType(testType, bojProblems);

        // API로 문제 가져오기
        List<BojProblem> bojProblems = getProblemsService.getProblemsByApi(testType, bojId);

        // 테스트 시작시 문제 저장
        saveProblemsService.saveAttemptProblems(member, test, bojProblems);

        // 테스트 시작시 코드 캐시 초기화
        TempCodeDto tempCodeDto = tempCodeInitializer.initializeTempCodeCache(test);

        return getTestStartResponseDto(test, bojProblems, tempCodeDto);
    }


    //테스트 만들어졌을 때에는 모두 안 푼 문제니깐 false로 초기화
    private TestStartResponseDto getTestStartResponseDto(Tests test, List<BojProblem> bojProblems,
                                                         TempCodeDto tempCodeDto) {
        List<BojProblemDto> bojProblemDtos = bojProblems.stream().map(bojProblem ->
                        BojProblemDto.builder()
                                .isSolved(false)
                                .bojProblemId(bojProblem.getProblemId())
                                .build())
                .collect(Collectors.toList());

        Integer problemCount = test.getProblemCount();
        List<TestCodeDto> testCodeDtos = IntStream.rangeClosed(1, problemCount).mapToObj(i -> TestCodeDto.builder()
                .cppCode(tempCodeDto.getCppCode())
                .pythonCode(tempCodeDto.getPythonCode())
                .javaCode(tempCodeDto.getJavaCode())
                .problemNumber(i)
                .build()).collect(Collectors.toList());

        TestStartResponseDto testStartResponseDto = TestStartResponseDto.builder()
                .testId(test.getTestId())
                .bojProblems(bojProblemDtos)
                .remainingTime(test.getRemainingTime())
                .testCodeDtos(testCodeDtos)
                .build();

        return testStartResponseDto;
    }

    private List<TestCodeDto> getTestCodeDtos(Tests test) {
        return tempCodeService.getTempCode(test);
    }

    private TestStartResponseDto getTestStartResponseDto(Tests test) {
        Long testId = test.getTestId();
        List<AttemptProblem> attemptProblems = attemptProblemRepository.findAttemptProblemsByTest_TestId(testId);

        List<BojProblemDto> bojProblemDtos =
                attemptProblems.stream().map(attemptProblem -> BojProblemDto.builder()
                        .isSolved(attemptProblem.getIsSolved())
                        .bojProblemId(attemptProblem.getProblem().getBojProblemId())
                        .build()).collect(Collectors.toList());

        List<TestCodeDto> testCodeDtos = getTestCodeDtos(test);

        TestStartResponseDto testStartResponseDto = TestStartResponseDto.builder()
                .testId(testId)
                .bojProblems(bojProblemDtos)
                .remainingTime(test.getRemainingTime())
                .testCodeDtos(testCodeDtos)
                .build();

        // 테스트 시작에 대한 ResponseDto 반환
        return testStartResponseDto;
    }
}