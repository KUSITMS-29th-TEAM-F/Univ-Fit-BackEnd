package backend.univfit.domain.coverletter.api;

import backend.univfit.domain.coverletter.api.dto.CoverLetterQuestionsResponse;
import backend.univfit.domain.coverletter.api.dto.PathCoverLetterRequest;
import backend.univfit.domain.coverletter.api.dto.SaveCoverLetterRequest;
import backend.univfit.domain.coverletter.api.dto.SaveCoverLetterResponse;
import backend.univfit.domain.coverletter.service.CoverLetterService;
import backend.univfit.global.ApiResponse;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.argumentResolver.customAnnotation.MemberInfo;
import backend.univfit.global.dto.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/members/cover-letters")
@RestController
public class CoverLetterApi {
    private final CoverLetterService coverLetterService;
    @GetMapping("/questions/{applyId}")
    public ApiResponse<CoverLetterQuestionsResponse> showCoverLetterQuestions (@PathVariable("applyId") Long applyId,
                                                                               @MemberInfo MemberInfoObject mio){
        return ApiResponse.onSuccess(coverLetterService.showCoverLetterQuestions(applyId, mio));
    }

    //자소서 작성
    @PostMapping("")
    public ApiResponse<SaveCoverLetterResponse> saveCoverLetter(@RequestBody SaveCoverLetterRequest clr,
                                                                @MemberInfo MemberInfoObject mio){
        return ApiResponse.onSuccess(coverLetterService.saveCoverLetter(clr, mio));
    }

    //자소서 수정
    @PatchMapping("")
    public ApiResponse<GeneralResponse> patchCoverLetter(@RequestBody PathCoverLetterRequest clr,
                                                         @MemberInfo MemberInfoObject mio){
        return ApiResponse.onSuccess(coverLetterService.patchCoverLetter(clr, mio));
    }




}
