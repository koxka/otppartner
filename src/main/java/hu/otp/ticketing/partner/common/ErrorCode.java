package hu.otp.ticketing.partner.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    SUCCESS(0L),
    GENERAL_ERROR(90000L),
    NO_SUCH_FILE(90001L),
    NO_SUCH_SEAT(90002L),
    SEAT_ALREADY_TAKEN(90003L);

    private final Long code;

    ErrorCode(long l) {
        this.code = l;
    }
}
