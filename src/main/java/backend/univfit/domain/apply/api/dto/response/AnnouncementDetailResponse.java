package backend.univfit.domain.apply.api.dto.response;

import java.util.List;

public record AnnouncementDetailResponse(
        Long scholarshipId,
        String scholarShipImage,
        String scholarshipName,
        String scholarshipFoundation,
        Long remainingDay,
        String applyPossible,
        String supportAmount,
        String applicationPeriod,
        String hashTag,
        List<String> applyCondition,
//        String applicationConditions,
        String detailContents,
        Integer likes,
        Boolean memberIsLiked,
        Boolean memberIsStored,
        String applyLink
) {
    public static AnnouncementDetailResponse of(Long scholarshipId, String scholarShipImage, String scholarShipName, String scholarShipFoundation, Long remainingDay,
                                                String applyPossible,
                                                String supportAmount,
                                                String applicationPeriod, String hashTag, List<String> applyCondition,
                                                String detailContents,
                                                Integer likes,
                                                Boolean memberIsLiked,
                                                Boolean memberIsStored,
                                                String applyLink) {

        return new AnnouncementDetailResponse(scholarshipId, scholarShipImage,
                scholarShipName, scholarShipFoundation, remainingDay, applyPossible,
                supportAmount, applicationPeriod, hashTag, applyCondition,
                detailContents, likes, memberIsLiked, memberIsStored, applyLink
        );

    }
}
