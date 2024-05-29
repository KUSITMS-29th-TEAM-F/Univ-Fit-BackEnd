package backend.univfit.domain.coverletter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoverLetterApplyObj {
    private Long applyId;
    private String scholarShipName;

    public static CoverLetterApplyObj of(
            Long applyId,
            String scholarShipName
    ){
        return CoverLetterApplyObj.builder()
                .applyId(applyId)
                .scholarShipName(scholarShipName)
                .build();
    }
}
