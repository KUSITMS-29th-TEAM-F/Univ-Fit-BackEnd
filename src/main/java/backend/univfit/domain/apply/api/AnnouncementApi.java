package backend.univfit.domain.apply.api;

import backend.univfit.domain.apply.api.dto.response.AnnouncementDetailResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementListResponse;
import backend.univfit.domain.apply.application.AnnouncementService;
import backend.univfit.domain.apply.api.dto.response.AnnouncementResponse;
import backend.univfit.global.ApiResponse;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.argumentResolver.customAnnotation.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/announcements")
@RestController
public class AnnouncementApi {
    private final AnnouncementService announcementService;

    @GetMapping("")
    public ApiResponse<AnnouncementListResponse> getAnnouncementList(@RequestParam(required = false, defaultValue = "ING") List<String> status/**,@RequestHeader("socialAccessToken") String accessToken**/) {
        return ApiResponse.onSuccess(announcementService.getAnnouncementList(status));
    }

    @GetMapping("/{announcementId}")
    public ApiResponse<AnnouncementDetailResponse> getAnnouncement(@PathVariable Long announcementId
                                                                   /**,@MemberInfo MemberInfoObject memberInfoObject**/) {
        return ApiResponse.onSuccess(announcementService.getAnnouncement(announcementId/**,memberInfoObject**/));
    }

}