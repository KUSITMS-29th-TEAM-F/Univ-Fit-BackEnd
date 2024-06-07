package backend.univfit.domain.apply.application;

import backend.univfit.domain.apply.api.dto.response.*;
import backend.univfit.domain.apply.entity.AnnouncementEntity;
import backend.univfit.domain.apply.repository.AnnouncementJpaRepository;
import backend.univfit.domain.apply.repository.LikeJpaRepository;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeAnnouncementServiceImpl implements HomeAnnouncementService {
    private final AnnouncementJpaRepository announcementJpaRepository;
    private final LikeJpaRepository likeJpaRepository;
    private final AnnouncementManager announcementManager;

    @Override
    public PopularAnnouncementListResponse getPopularAnnouncements() {
        List<PopularAnnouncementResponse> responses = likeJpaRepository.findTopPopularAnnouncements(PageRequest.of(0, 3))
                .stream()
                .map(objects -> {
                    AnnouncementEntity announcement = (AnnouncementEntity) objects[0];
                    Long likeCount = (Long) objects[1];
                    return PopularAnnouncementResponse.of(
                            announcement.getId(),
                            announcement.getScholarShipImage(),
                            announcement.getScholarShipName(),
                            announcement.getScholarShipFoundation(),
                            announcement.getApplicationPeriod(),
                            likeCount
                    );
                })
                .toList();

        return new PopularAnnouncementListResponse(responses);
    }

    @Override
    public AnnouncementListBySearchResponse getAnnouncementsBySearch(String q, MemberInfoObject memberInfoObject) {
        Long memberId = memberInfoObject.getMemberId();
        List<AnnouncementBySearchResponse> announcementBySearchResponseList = announcementJpaRepository.findBySearchCriteria(q)
                .stream()
                .map(findAnnouncement -> {
                    Long remainingDay = ChronoUnit.DAYS.between(LocalDate.now(), findAnnouncement.getEndDocumentDate());
                    String applyPossible = announcementManager.checkEligibility(findAnnouncement, memberId);
                    return AnnouncementBySearchResponse.of(
                            findAnnouncement.getId(), findAnnouncement.getScholarShipImage(), findAnnouncement.getScholarShipName(),
                            findAnnouncement.getScholarShipFoundation(), findAnnouncement.getApplicationPeriod(),
                            remainingDay, applyPossible);
                }).toList();

        return AnnouncementListBySearchResponse.of(announcementBySearchResponseList);
    }
}
