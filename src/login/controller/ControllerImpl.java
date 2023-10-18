package login.controller;

import login.dto.Dto;
import login.service.Service;

public class ControllerImpl implements Controller{
    private final Service service = new Service();

    @Override
    public Object handleLogin(Dto.LoginRequestDTO requestDTO) {
        return service.authenticate(requestDTO);
    }
}
