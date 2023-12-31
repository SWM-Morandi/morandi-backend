package swm_nm.morandi.domain.testInfo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.testDuring.dto.TestStatus;
import swm_nm.morandi.domain.testInfo.entity.Tests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Tests, Long>{
    //Paging하여 테스트 기록을 가져옴
    Page<Tests> findAllTestsByMember_MemberIdAndTestStatus(Long memberId, TestStatus testStatus, Pageable pageable);
    //1년동안의 테스트 기록을 가져와서 레이팅 반환에 사용함
    List<Tests> findAllTestsByMember_MemberIdAndTestStatusAndTestDateAfterOrderByTestDateAsc(Long memberId, TestStatus testStatus, LocalDateTime oneYearAgo);
    Long countByMember_MemberIdAndTestStatus(Long memberId, TestStatus testStatus);

    Optional<Tests> findTestByTestIdAndMember_MemberId(Long testId, Long memberId);
}
