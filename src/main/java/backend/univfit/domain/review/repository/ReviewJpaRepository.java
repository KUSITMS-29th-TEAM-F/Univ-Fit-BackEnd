package backend.univfit.domain.review.repository;

import backend.univfit.domain.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    ArrayList<ReviewEntity> findAllBy();
}
