package com.ianion.adventofcode.y2015.d23;

import lombok.Builder;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Computer(
        Set<Register> registers,
        Program program
) {

    public static Computer runProgram(Set<Register> registers, Program program) {
        return loadProgram(registers, program)
                .runLoadedProgram();
    }

    private static Computer loadProgram(Set<Register> registers, Program program) {
        return Computer.builder()
                .registers(registers)
                .program(program)
                .build();
    }

    private Computer runLoadedProgram() {
        return Stream
                .iterate(
                        this,
                        c -> !c.program.isFinished(),
                        Computer::executeNextInstruction)
                .reduce(this, (first, second) -> second)
                .executeNextInstruction();
    }

    private Computer executeNextInstruction() {
        return program.currentInstruction().apply(this);
    }

    public Computer halve(String registerName) {
        return alterRegister(registerName, Register::halve).advanceProgram();
    }

    public Computer triple(String registerName) {
        return alterRegister(registerName, Register::triple).advanceProgram();
    }

    public Computer increment(String registerName) {
        return alterRegister(registerName, Register::increment).advanceProgram();
    }

    private Computer alterRegister(String registerName, UnaryOperator<Register> function) {
        return this.toBuilder()
                .registers(
                        registers.stream()
                                .map(r -> r.name().equals(registerName) ? function.apply(r) : r)
                                .collect(Collectors.toSet()))
                .build();
    }

    public Computer jump(int offset) {
        return this.toBuilder()
                .program(program.jump(offset))
                .build();
    }

    public Computer jumpIfEven(String registerName, int offset) {
        return registers.stream()
                .filter(r -> r.name().equals(registerName) && r.value() % 2 == 0)
                .findFirst()
                .map(r -> jump(offset))
                .orElseGet(this::advanceProgram);
    }

    public Computer jumpIfOne(String registerName, int offset) {
        return registers.stream()
                .filter(r -> r.name().equals(registerName) && r.value() == 1)
                .findFirst()
                .map(r -> jump(offset))
                .orElseGet(this::advanceProgram);
    }

    private Computer advanceProgram() {
        return this.toBuilder().program(program.advance()).build();
    }

    public int getRegisterValue(String registerName) {
        return registers().stream()
                .filter(r -> r.name().equals(registerName))
                .findFirst()
                .map(Register::value)
                .orElseThrow(() -> new RuntimeException("Could not find register"));
    }
}
