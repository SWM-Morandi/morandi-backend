package swm_nm.morandi.domain.practice.entity;

import lombok.*;
import swm_nm.morandi.domain.common.BaseEntity;
import swm_nm.morandi.domain.common.Language;
import swm_nm.morandi.domain.member.entity.Member;
import swm_nm.morandi.domain.problem.entity.Problem;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeProblem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long practiceProblemId;

    private LocalDate practiceDate;

    private Boolean isSolved;

    @Column(columnDefinition = "TEXT")
    private String submitCode;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Language submitLanguage = Language.Cpp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setSubmitCode(String code) {
        this.submitCode = code;
    }
    public void setSubmitLanguage(Language language) {
        this.submitLanguage = language;
    }
    public void setIsSolved(Boolean isSolved) {
        this.isSolved = isSolved;
    }
}
