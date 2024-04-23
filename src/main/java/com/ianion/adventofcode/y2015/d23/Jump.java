package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Jump implements Instruction {

    int offset;

    @Override
    public Computer apply(Computer computer) {
        return computer.jump(offset);
    }
}
