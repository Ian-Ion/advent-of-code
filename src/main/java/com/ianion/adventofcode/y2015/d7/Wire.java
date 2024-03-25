package com.ianion.adventofcode.y2015.d7;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Wire {

    String id;

    public static Wire from(String id) {
        return Wire.builder()
                .id(id)
                .build();
    }

}
