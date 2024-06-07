package backend.univfit.domain.apply.api;

import backend.univfit.domain.apply.api.dto.response.AnnouncementListBySearchResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementListResponse;
import backend.univfit.domain.apply.api.dto.response.PopularAnnouncementListResponse;
import backend.univfit.domain.apply.application.HomeAnnouncementService;
import backend.univfit.global.ApiResponse;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.argumentResolver.customAnnotation.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/home")
@RestController
public class HomeAnnouncementApi {
    private final HomeAnnouncementService homeAnnouncementService;

    /**
     * 인기 장학금 조회
     * @return
     */
    @GetMapping("/popular-announcements")
    public ApiResponse<PopularAnnouncementListResponse> getPopularAnnouncements() {
        return ApiResponse.onSuccess(homeAnnouncementService.getPopularAnnouncements());
    }

    /**
     * 검색 후 검색 키워드에 맞는 공고 조회
     *
     * @param q
     * @return
     */
    @GetMapping("/announcements/search")
    public ApiResponse<AnnouncementListBySearchResponse> getAnnouncementsBySearch(@RequestParam(value = "q") String q,
                                                                                  @MemberInfo MemberInfoObject memberInfoObject) {
        return ApiResponse.onSuccess(homeAnnouncementService.getAnnouncementsBySearch(q, memberInfoObject));
    }
}
