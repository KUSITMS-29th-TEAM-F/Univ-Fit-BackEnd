package backend.univfit.domain.coverletter.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoverLetterDetailResponse {
    private String coverLetterTitle;
    private String scholarshipFoundation;
    private String scholarshipName;
    private Long applyId;
    private ArrayList<CoverLetterObj> coverLetterList;

    public static CoverLetterDetailResponse of(
            String coverLetterTitle,
            String scholarshipFoundation,
            String scholarshipName,
            Long applyId,
            ArrayList<CoverLetterObj> coverLetterList
    ){
        return CoverLetterDetailResponse.builder()
                .coverLetterTitle(coverLetterTitle)
                .scholarshipFoundation(scholarshipFoundation)
                .scholarshipName(scholarshipName)
                .applyId(applyId)
                .coverLetterList(coverLetterList)
                .build();
    }
}
