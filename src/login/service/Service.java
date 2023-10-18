package login.service;

import login.dao.Dao;
import login.dao.DaoImpl;
import login.dto.Dto;
import login.entity.Entity;

public class Service {
    private Dao dao;
    public Service(){
        this.dao = new DaoImpl();
    }
    public Object authenticate(Dto.LoginRequestDTO request){
        Entity.User user = dao.findUserByIdAndPw(request);
        if(user != null){
            return user;
        }else{
            Entity.Manager manager = dao.findManagerByIdAndPw(request);
            if(manager != null){
                return manager;
            }else{
                return null;
            }
        }
    }
}
