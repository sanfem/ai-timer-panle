package org.example.aiplaner.controler;

import org.example.aiplaner.Service.PersonalCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/PersonalCenter")
class PersonalCenterController {
    @Autowired
    private PersonalCenterService personalCenterService;

    public PersonalCenterController(PersonalCenterService personalCenterService) {
        this.personalCenterService = personalCenterService;
    }

    @GetMapping("/Getname")
    public ResponseEntity<Map<String, Object>> getName(@RequestAttribute long userid){
          return ResponseEntity.status(HttpStatus.OK).body(Map.of("name",personalCenterService.getName(userid)));
    }

    @PostMapping("/Setname")
    public  ResponseEntity<Map<String, Object>>  setName(@RequestAttribute long userid, @RequestParam("name") String name){
        if("OK".equals(personalCenterService.SetName(userid,name))){
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("name",personalCenterService.getName(userid)));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("name",personalCenterService.getName(userid)));
        }
    }
}
