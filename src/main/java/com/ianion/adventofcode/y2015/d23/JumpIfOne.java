package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JumpIfOne implements Instruction {

    String registerName;
    int offset;

    @Override
    public Computer apply(Computer computer) {
        return computer.jumpIfOne(registerName, offset);
    }
}
