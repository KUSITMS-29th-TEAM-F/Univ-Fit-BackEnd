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
public class SaveCoverLetterRequest {
    private Long applyId;
    private String title;
    private ArrayList<CoverLetterObj> coverLetters;
}
