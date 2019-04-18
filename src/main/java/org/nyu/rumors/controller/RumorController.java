package org.nyu.rumors.controller;


import org.nyu.rumors.dto.RumorDto;
import org.nyu.rumors.dto.RumorIdDto;
import org.nyu.rumors.entity.RumorEntity;
import org.nyu.rumors.service.RumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("api")
public class RumorController {

    @Autowired
    private RumorService rumorService;

    @RequestMapping(value = "/rumor", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getRumor() {
        return ResponseEntity.ok(rumorService.getAllRumors());
    }

    @RequestMapping(value = "/rumor/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getRumorById(@PathVariable UUID id) {

        if (!rumorService.rumorExistsBbyId(id)) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }

        RumorEntity rumor = rumorService.getRumorByID(id);
        return ResponseEntity.ok(rumorService.convertToRumorIdDto(rumor));
    }

    @RequestMapping(value = "/rumor", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> postRumor(@RequestBody RumorDto rumor) {
        RumorEntity entity = rumorService.convertToRumorEntity(rumor);
        rumorService.createRumor(entity);
        return new ResponseEntity<RumorIdDto>(rumorService.convertToRumorIdDto(entity), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rumor/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteRumor(@PathVariable UUID id) {

        if (!rumorService.rumorExistsBbyId(id)) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }

        rumorService.deleteRumorbyId(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/rumor", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteAllRumors() {
        rumorService.deleteAllRumors();
        return ResponseEntity.noContent().build();
    }
}
