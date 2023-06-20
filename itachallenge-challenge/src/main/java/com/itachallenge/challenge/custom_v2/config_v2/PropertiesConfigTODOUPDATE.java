package com.itachallenge.challenge.custom_v2.config_v2;

import org.springframework.stereotype.Component;

@Component  //@Configuration not used due there isn't any @bean method
public class PropertiesConfigTODOUPDATE {


    //TODO: new attribute + inject value (from consul?) + implement get method

    public String getFindStatisticsURL() {
        return null; // TODO: must return url for request cahllenge statistics to micro user
    }
}
