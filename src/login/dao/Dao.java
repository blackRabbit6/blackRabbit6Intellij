package login.dao;

import login.dto.Dto;
import login.entity.Entity;

public interface Dao {
    Entity.User findUserByIdAndPw(Dto.LoginRequestDTO loginRequest);
    Entity.Manager findManagerByIdAndPw(Dto.LoginRequestDTO loginRequest);
}
