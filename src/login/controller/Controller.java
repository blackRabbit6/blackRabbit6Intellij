package login.controller;

import login.dto.Dto;

public interface Controller {
    Object handleLogin(Dto.LoginRequestDTO requestDTO);
}
