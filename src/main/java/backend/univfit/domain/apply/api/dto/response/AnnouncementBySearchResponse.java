package backend.univfit.domain.apply.api.dto.response;

public record AnnouncementBySearchResponse(
        Long announcementId,
        String scholarShipImage,
        String scholarShipName,
        String scholarshipFoundation,
        String applicationPeriod,
        Long remainingDays,
        String applyPossible
) {
    public static AnnouncementBySearchResponse of(Long announcementId, String scholarShipImage, String scholarShipName, String scholarshipFoundation, String applicationPeriod,
                                                  Long remainingDays, String applyPossible) {
        return new AnnouncementBySearchResponse(announcementId, scholarShipImage, scholarShipName, scholarshipFoundation, applicationPeriod, remainingDays, applyPossible
        );
    }

}

