package backend.univfit.domain.coverletter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCoverLetterEntry {
    private Long coverLetterId;
    private String title;
    private String scholarshipFoundation;
    private String scholarshipName;
    private LocalDate updatedAt;

    public static MyCoverLetterEntry of(
            Long coverLetterId,
            String title,
            String scholarshipFoundation,
            String scholarshipName,
            LocalDate updatedAt
    ){
        return MyCoverLetterEntry.builder()
                .coverLetterId(coverLetterId)
                .title(title)
                .scholarshipFoundation(scholarshipFoundation)
                .scholarshipName(scholarshipName)
                .updatedAt(updatedAt)
                .build();
    }
}
