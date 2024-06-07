package backend.univfit.domain.apply.application;

import backend.univfit.domain.apply.api.dto.response.AnnouncementListBySearchResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementListResponse;
import backend.univfit.domain.apply.api.dto.response.PopularAnnouncementListResponse;
import backend.univfit.global.argumentResolver.MemberInfoObject;

public interface HomeAnnouncementService {
    PopularAnnouncementListResponse getPopularAnnouncements();

    AnnouncementListBySearchResponse getAnnouncementsBySearch(String q, MemberInfoObject memberInfoObject);
}
