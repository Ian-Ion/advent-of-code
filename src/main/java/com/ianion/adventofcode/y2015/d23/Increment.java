package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Increment implements Instruction {
    
    String registerName;

    @Override
    public Computer apply(Computer computer) {
        return computer.increment(registerName);
    }
}
