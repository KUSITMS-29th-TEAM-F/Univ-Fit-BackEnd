package backend.univfit.global.error.status;

import backend.univfit.global.error.code.BaseErrorCode;
import backend.univfit.global.error.dto.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    //기본(전역) 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON_500", "서버에서 요청을 처리 하는 동안 오류가 발생했습니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON_400", "입력 값이 잘못된 요청 입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON_401", "인증이 필요 합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청 입니다."),

    //Member 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "사용자를 찾을 수 없습니다."),
    MEMBER_PRIVATE_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER_002","사용자의 온보딩 정보를 찾을 수 없습니다."),

    //Condition 관련 에러
    CONDITION_NOT_FOUND(HttpStatus.NOT_FOUND,"CONDITION_001","관련된 조건일치 판단 테이블을 찾을 수 없습니다."),

    //Announcement 관련 에러
    ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"ANNOUNCEMENT_001","해당 지원공고를 찾을 수 없습니다."),
    ANNOUNCEMENT_CONDITION_NOT_FOUND(HttpStatus.NOT_FOUND,"ANNOUNCEMENT_002","공고의 조건 테이블을 찾을 수 없습니다."),

    //RequiredDocument 관련 에러
    REQUIRED_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"ANNOUNCEMENT_001","해당 필요한 서류를 찾을 수 없습니다."),

    //ScholarShipFoundation 관련 에러
    SCHOLARSHIP_FOUNDATION_NOT_FOUND(HttpStatus.NOT_FOUND,"SCHOLARSHIP_FOUNDATION_001","재단 상세정보를 찾을 수 없습니다."),

    //Like 관련 에러
    LIKE_ERROR(HttpStatus.NOT_FOUND,"LIKE_001","좋아요를 조회 시 오류가 발생하였습니다."),
    LIKE_IS_EXIST(HttpStatus.BAD_REQUEST, "LIKE_002", "이미 좋아요를 누른 공고입니다."),

    //Document 관련 에러
    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "DOCUMENT_001", "서류를 찾지 못하였습니다."),
    DOCUMENT_INVALID_BODY(HttpStatus.BAD_REQUEST, "DOCUMENT_002", "필수항목을 입력해주세요."),


    //Body 에러
    INVALID_BODY(HttpStatus.BAD_REQUEST, "BODY_ERROR", "Body가 올바르지 않습니다."),

    //JWT 관련 에러
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_001", "JWT인증시간이 만료되었습니다."),
    JWT_UNSUPPORTED_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "JWT_002", "지원하는 JWT타입이 아닙니다."),
    JWT_INVALIED(HttpStatus.UNAUTHORIZED, "JWT_003", "유효한 JWT가 아닙니다."),

    //온보딩 에러
    ONBOARD_DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "ONBOARD_001", "중복된 닉네임 입니다."),

    //지원 에러
    APPLY_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "APPLY_001", "유효한 유저가 아닙니다."),

    //자소서 에러
    COVER_LETTER_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "COVERLETTER_001", "유효한 유저가 아닙니다.");




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }


}
