package swm_nm.morandi.domain.testInfo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm_nm.morandi.domain.testInfo.dto.TestTypeInfoResponse;
import swm_nm.morandi.domain.testInfo.entity.TestType;
import swm_nm.morandi.domain.testInfo.mapper.TestTypeMapper;
import swm_nm.morandi.domain.testInfo.repository.TestTypeRepository;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.global.exception.errorcode.TestTypeErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestTypeInfoService {

    private final TestTypeRepository testTypeRepository;

    @Transactional(readOnly = true)
    public TestTypeInfoResponse getTestTypeDto(Long testTypeId) {
        TestType testType = testTypeRepository.findById(testTypeId).orElseThrow(() -> new MorandiException(TestTypeErrorCode.TEST_TYPE_NOT_FOUND));
        TestTypeInfoResponse testTypeDto = TestTypeMapper.convertToDto(testType);
        return testTypeDto;
    }
}
