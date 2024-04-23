package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record Program(
        List<Instruction> instructions,
        int currentInstructionIndex
) {

    public Instruction currentInstruction() {
        return instructions.get(currentInstructionIndex);
    }

    public Program advance() {
        return this.toBuilder()
                .currentInstructionIndex(currentInstructionIndex + 1)
                .build();
    }

    public Program jump(int offset) {
        return this.toBuilder()
                .currentInstructionIndex(currentInstructionIndex + offset)
                .build();
    }

    public boolean isFinished() {
        return currentInstructionIndex < 0 || currentInstructionIndex >= instructions.size();
    }
}
