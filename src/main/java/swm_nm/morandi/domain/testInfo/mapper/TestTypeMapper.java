package swm_nm.morandi.domain.testInfo.mapper;

import swm_nm.morandi.domain.testInfo.dto.TestTypeInfoResponse;
import swm_nm.morandi.domain.testInfo.entity.TestType;


public class TestTypeMapper {
    public static TestTypeInfoResponse convertToDto(TestType testType) {
        TestTypeInfoResponse testTypeDto = TestTypeInfoResponse.builder()
                .testTypeId(testType.getTestTypeId())
                .testTypename(testType.getTestTypename())
                .frequentTypes(testType.getFrequentTypes())
                .testTime(testType.getTestTime())
                .problemCount(testType.getProblemCount())
                .startDifficulty(testType.getStartDifficulty())
                .endDifficulty(testType.getEndDifficulty())
                .numberOfTestTrial(testType.getNumberOfTestTrial())
                .averageCorrectAnswerRate(testType.getAverageCorrectAnswerRate())
                .build();

        return testTypeDto;
    }
}
