package backend.univfit.domain.coverletter.repository;

import backend.univfit.domain.apply.entity.ApplyEntity;
import backend.univfit.domain.apply.repository.ApplyJpaRepository;
import backend.univfit.domain.coverletter.entity.CoverLetterEntity;
import backend.univfit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverLetterJpaRepository extends JpaRepository<CoverLetterEntity, Long> {
   List<CoverLetterEntity> findAllByApplyEntity(ApplyEntity applyEntity);
   List<CoverLetterEntity> findAllByMember(Member member);
}
