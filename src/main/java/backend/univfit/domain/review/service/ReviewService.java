package backend.univfit.domain.review.service;

import backend.univfit.domain.review.dto.ReviewResponse;
import backend.univfit.domain.review.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewJpaRepository reviewJpaRepository;

    public ReviewResponse getReview(){
        return ReviewResponse.of(reviewJpaRepository.findAllBy());
    }
}
