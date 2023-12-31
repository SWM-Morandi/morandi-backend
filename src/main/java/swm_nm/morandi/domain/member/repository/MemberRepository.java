package swm_nm.morandi.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm_nm.morandi.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // OAuth 로그인으로 반환되는 값 중 email을 통해 이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메소드
    Optional<Member> findByEmail(String email);
    Boolean existsByBojId(String bojId);
    Page<Member> findAll(Pageable pageable);

    List<Member> findAllByBojIdIn(List<String> bojIdList);
}
