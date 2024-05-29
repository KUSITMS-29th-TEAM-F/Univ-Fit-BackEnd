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
    private ArrayList<CoverLetterObj> coverLetterList;

    public static CoverLetterDetailResponse of(
            ArrayList<CoverLetterObj> coverLetterList
    ){
       return CoverLetterDetailResponse.builder()
                .coverLetterList(coverLetterList)
                .build();
    }
}
