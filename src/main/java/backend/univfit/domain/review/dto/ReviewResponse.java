package backend.univfit.domain.review.dto;

import backend.univfit.domain.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewResponse {
    private ArrayList<ReviewEntity> reviewList;

    public static ReviewResponse of(
            ArrayList<ReviewEntity> reviewList
    ){
        return ReviewResponse.builder()
                .reviewList(reviewList)
                .build();
    }
}
