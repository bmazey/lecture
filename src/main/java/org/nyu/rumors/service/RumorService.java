package org.nyu.rumors.service;


import org.modelmapper.ModelMapper;
import org.nyu.rumors.dto.RumorDto;
import org.nyu.rumors.dto.RumorIdDto;
import org.nyu.rumors.dto.RumorListDto;
import org.nyu.rumors.entity.RumorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RumorService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RumorJpaRepository repository;

    /**
     * Here we'll use our JPA repository methods ...
     * @param id
     * @return
     */

    public RumorEntity getRumorByID(UUID id) {
        return repository.findById(id).get();
    }

    public RumorListDto getAllRumors() {
        List<RumorEntity> rumors = new ArrayList<>();
        repository.findAll().forEach(rumors :: add);

        // Now we have an ArrayList of <RumorEntity> ... but we want to convert it to <RumorIdDto> !
        List<RumorIdDto> rumorIds = rumors
                .stream()
                .map(rumor -> convertToRumorIdDto(rumor))
                .collect(Collectors.toList());

        // This is probably not the *best* way to do this, but all we're looking for is a small dto change ...
        RumorListDto result = new RumorListDto();
        result.setRumors(rumorIds);
        return result;
    }

    public void createRumor(RumorEntity rumor) {
        repository.save(rumor);
    }

    public void deleteRumorbyId(UUID id) {
        repository.deleteById(id);
    }

    public void deleteAllRumors() {
        repository.deleteAll();
    }

    public boolean rumorExistsBbyId(UUID id) {
        return repository.findById(id).isPresent();
    }

    /**
     * Here we use the model mapper to convert to and from dtos and entities ...
     * @param entity
     * @return
     */

    public RumorIdDto convertToRumorIdDto(RumorEntity entity) {
        RumorIdDto rumor = modelMapper.map(entity, RumorIdDto.class);
        return rumor;
    }

    public RumorEntity convertToRumorEntity(RumorDto rumor) {
        RumorEntity entity = modelMapper.map(rumor, RumorEntity.class);
        return entity;
    }

}
