package backend.univfit.global.Comparator;

import backend.univfit.domain.coverletter.entity.CoverLetterQuestionEntity;

import java.util.Comparator;

public class CoverLetterQuestionComparator implements Comparator<CoverLetterQuestionEntity> {

    @Override
    public int compare(CoverLetterQuestionEntity o1, CoverLetterQuestionEntity o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
