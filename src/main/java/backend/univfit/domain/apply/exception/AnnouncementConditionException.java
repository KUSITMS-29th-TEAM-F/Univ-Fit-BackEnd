package backend.univfit.domain.apply.exception;

import backend.univfit.global.error.code.BaseErrorCode;
import backend.univfit.global.error.exception.GeneralException;

public class AnnouncementConditionException extends GeneralException {
    private BaseErrorCode baseErrorCode;
    public AnnouncementConditionException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
