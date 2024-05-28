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
public class MyCoverLetterList {
    ArrayList<MyCoverLetterEntry> coverLetterList;

    public static MyCoverLetterList of(
            ArrayList<MyCoverLetterEntry> coverLetterList
    ){
        return MyCoverLetterList.builder()
                .coverLetterList(coverLetterList)
                .build();
    }
}
