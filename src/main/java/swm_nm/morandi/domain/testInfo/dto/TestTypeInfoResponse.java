package swm_nm.morandi.domain.testInfo.dto;

import lombok.*;
import swm_nm.morandi.domain.problem.dto.DifficultyLevel;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestTypeInfoResponse {
    private Long testTypeId;
    private String testTypename;
    private String frequentTypes;
    private Long testTime;
    private Integer problemCount;
    private DifficultyLevel startDifficulty;
    private DifficultyLevel endDifficulty;
    private Long averageCorrectAnswerRate;
    private Integer numberOfTestTrial;
}
