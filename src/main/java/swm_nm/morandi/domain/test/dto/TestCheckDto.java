package swm_nm.morandi.domain.test.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCheckDto {
    private Long testId;
    private String bojId;
    private Long testTypeId;
}