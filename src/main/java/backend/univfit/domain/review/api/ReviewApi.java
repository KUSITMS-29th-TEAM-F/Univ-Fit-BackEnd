package backend.univfit.domain.review.api;

import backend.univfit.domain.review.dto.ReviewResponse;
import backend.univfit.domain.review.service.ReviewService;
import backend.univfit.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewApi {
    private final ReviewService reviewService;
    @GetMapping("")
    public ApiResponse<ReviewResponse> getReview(){
        return ApiResponse.onSuccess(reviewService.getReview());
    }
}
