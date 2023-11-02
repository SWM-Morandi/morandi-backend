package swm_nm.morandi.domain.practice.entity;

import lombok.*;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.problem.entity.Problem;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeProblem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long practiceProblemId;

    private LocalDate practiceDate;

    private Boolean isSolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
