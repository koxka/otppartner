package hu.otp.ticketing.partner.mapper;

import hu.otp.ticketing.partner.endpoint.rest.model.Info;
import hu.otp.ticketing.partner.endpoint.rest.model.State;

import static hu.otp.ticketing.partner.common.ErrorCode.GENERAL_ERROR;
import static hu.otp.ticketing.partner.common.ErrorCode.SUCCESS;

public interface InformationMapper {
    default Info map(boolean source) {
        Info info = new Info();
        info.setSuccess(source);
        State state = new State();
        state.setCode(source ? SUCCESS.getCode() : GENERAL_ERROR.getCode());
        state.setReason(source ? SUCCESS.name() : GENERAL_ERROR.name());
        info.setState(state);
        return info;
    }
}
