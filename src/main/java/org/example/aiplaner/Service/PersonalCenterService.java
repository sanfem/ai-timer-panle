package org.example.aiplaner.Service;

import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.dao.Userdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

@Service
public class PersonalCenterService {
    @Autowired
    Userdao userdao;

    public PersonalCenterService(Userdao userdao) {
        this.userdao = userdao;
    }

    public String getName(Long userid){
        String name=userdao.findById(userid).get().getName();
        return name;
    }

    public String SetName(Long userid,String name){
         userdao.findById(userid).get().setName(name);
         if(userdao.findById(userid).get().getName().equals(name)){
             return "OK" ;
         }
         else return "FAIL";
    }
}
