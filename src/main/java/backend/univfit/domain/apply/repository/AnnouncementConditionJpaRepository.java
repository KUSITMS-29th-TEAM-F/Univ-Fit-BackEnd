package backend.univfit.domain.apply.repository;

import backend.univfit.domain.apply.entity.AnnouncementConditionEntity;
import backend.univfit.domain.apply.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementConditionJpaRepository extends JpaRepository<AnnouncementConditionEntity, Long> {
    Optional<AnnouncementConditionEntity> findByAnnouncementEntity(AnnouncementEntity announcementEntity);
}
