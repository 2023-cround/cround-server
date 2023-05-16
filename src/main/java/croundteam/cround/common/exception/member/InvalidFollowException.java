package croundteam.cround.common.exception.member;

import croundteam.cround.common.exception.BusinessException;
import croundteam.cround.common.exception.ErrorCode;

public class InvalidFollowException extends BusinessException {
    public InvalidFollowException(ErrorCode errorCode) {
        super(errorCode);
    }
}
