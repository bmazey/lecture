package org.nyu.rumors.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // we just need  basic model mapping config
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
