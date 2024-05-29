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
public class CoverLetterApplyList {
    private ArrayList<CoverLetterApplyObj> applyList;

    public static CoverLetterApplyList of(
            ArrayList<CoverLetterApplyObj> applyList
    ){
        return CoverLetterApplyList.builder()
                .applyList(applyList)
                .build();
    }
}
