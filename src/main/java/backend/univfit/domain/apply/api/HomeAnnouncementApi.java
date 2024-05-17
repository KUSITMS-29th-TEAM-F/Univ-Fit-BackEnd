package backend.univfit.domain.apply.api;

import backend.univfit.domain.apply.api.dto.response.AnnouncementListBySearchResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementListResponse;
import backend.univfit.domain.apply.api.dto.response.PopularAnnouncementListResponse;
import backend.univfit.domain.apply.application.HomeAnnouncementService;
import backend.univfit.global.ApiResponse;
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

    @GetMapping("/announcements")
    public ApiResponse<PopularAnnouncementListResponse> getPopularAnnouncements() {
        return ApiResponse.onSuccess(homeAnnouncementService.getPopularAnnouncements());
    }

    @GetMapping("/announcements/search")
    public ApiResponse<AnnouncementListBySearchResponse> getAnnouncementsBySearch(@RequestParam(value = "q") String q) {
        return ApiResponse.onSuccess(homeAnnouncementService.getAnnouncementsBySearch(q));
    }
}