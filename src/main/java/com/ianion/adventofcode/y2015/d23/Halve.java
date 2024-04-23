package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Halve implements Instruction {

    String registerName;

    @Override
    public Computer apply(Computer computer) {
        return computer.halve(registerName);
    }
}
