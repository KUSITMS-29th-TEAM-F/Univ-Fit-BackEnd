package backend.univfit.domain.coverletter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaveCoverLetterResponse {
    private Long coverLetterId;

    public static SaveCoverLetterResponse of(
            Long coverLetterId
    ){
        return SaveCoverLetterResponse.builder()
                .coverLetterId(coverLetterId)
                .build();
    }
}
