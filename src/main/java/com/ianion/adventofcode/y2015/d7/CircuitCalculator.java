package com.ianion.adventofcode.y2015.d7;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CircuitCalculator {

    public static Circuit assembleAndRun(List<LogicGate> gates) {
        Circuit initialUnfiredCircuit = Circuit.initialize(gates);

        return Stream
                .iterate(
                        initialUnfiredCircuit,
                        Circuit::areUnfiredGates,
                        Circuit::fireNextGate)
                .reduce(initialUnfiredCircuit, (first, second) -> second)
                .fireNextGate();
    }

    @Builder(toBuilder = true)
    public record Circuit(
            Map<Wire, LogicGate> gates,
            Map<Wire, Integer> wireSignals
    ) {

        public static Circuit initialize(List<LogicGate> gates) {
            return Circuit.builder()
                    .gates(gates.stream().collect(
                            Collectors.toMap(LogicGate::getOutput, Function.identity())))
                    .wireSignals(gates.stream().filter(InputSignal.class::isInstance).collect(
                            Collectors.toMap(LogicGate::getOutput, g -> g.fire(Map.of()))))
                    .build();
        }

        public boolean areUnfiredGates() {
            return wireSignals.size() != gates.size();
        }

        public Circuit fireNextGate() {
            return findGateWhichIsReadyToFire()
                    .map(this::fireGate)
                    .orElse(this);
        }

        private Optional<Map.Entry<Wire, LogicGate>> findGateWhichIsReadyToFire() {
            return this.gates.entrySet().stream()
                    .filter(this::isReadyToFire).findFirst();
        }

        private boolean isReadyToFire(Map.Entry<Wire, LogicGate> e) {
            return hasNotAlreadyBeenFired(e.getKey()) && allDependenciesHaveFired(e.getValue());
        }

        private boolean hasNotAlreadyBeenFired(Wire wire) {
            return !wireSignals.containsKey(wire);
        }

        private boolean allDependenciesHaveFired(LogicGate gate) {
            return gate.getDependencies().stream().allMatch(wireSignals::containsKey);
        }

        private Circuit fireGate(Map.Entry<Wire, LogicGate> gate) {
            return this.toBuilder()
                    .wireSignals(
                            ImmutableMap.<Wire, Integer>builder()
                                    .putAll(wireSignals)
                                    .put(gate.getKey(), gate.getValue().fire(wireSignals))
                                    .build())
                    .build();
        }

        public int getSignal(Wire wire) {
            return wireSignals.get(wire);
        }
    }
}
