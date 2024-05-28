package backend.univfit.domain.coverletter.api;

import backend.univfit.domain.coverletter.api.dto.*;
import backend.univfit.domain.coverletter.service.CoverLetterService;
import backend.univfit.global.ApiResponse;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.argumentResolver.customAnnotation.MemberInfo;
import backend.univfit.global.dto.response.GeneralResponse;
import lombok.Getter;
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

    //내가 지원한 공고 불러오기(자소서 용)
    @GetMapping("/apply-list")
    public ApiResponse<CoverLetterApplyList> getApplyList(@MemberInfo MemberInfoObject mio){
        return ApiResponse.onSuccess(coverLetterService.getApplyList(mio));
    }





}
