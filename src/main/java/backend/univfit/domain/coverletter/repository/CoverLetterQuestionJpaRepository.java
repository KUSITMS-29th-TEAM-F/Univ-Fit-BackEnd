package backend.univfit.domain.coverletter.repository;

import backend.univfit.domain.coverletter.entity.CoverLetterEntity;
import backend.univfit.domain.coverletter.entity.CoverLetterQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CoverLetterQuestionJpaRepository extends JpaRepository<CoverLetterQuestionEntity, Long> {
    ArrayList<CoverLetterQuestionEntity> findAllByCoverLetterEntity(CoverLetterEntity coverLetterEntity);
}
