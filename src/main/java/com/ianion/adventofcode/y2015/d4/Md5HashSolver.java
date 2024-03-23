package com.ianion.adventofcode.y2015.d4;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Stream;

@UtilityClass
public class Md5HashSolver {

    public static int findLowestSaltProducingHashStartingWith(String key, String prefix) {
        return Stream.iterate(
                        HashSaltIterator.begin(key),
                        iterator -> !iterator.hash.startsWith(prefix),
                        HashSaltIterator::next)
                .reduce(HashSaltIterator.begin(key), (first, second) -> second)
                .next().salt;
    }

    @Builder(toBuilder = true)
    private record HashSaltIterator(
            String key,
            int salt,
            String hash
    ) {

        public static HashSaltIterator begin(String key) {
            return HashSaltIterator.builder()
                    .key(key)
                    .salt(1)
                    .hash(DigestUtils.md5Hex(key.concat(String.valueOf(1))))
                    .build();
        }

        public HashSaltIterator next() {
            return this.toBuilder()
                    .salt(salt + 1)
                    .hash(DigestUtils.md5Hex(key.concat(String.valueOf(salt + 1))))
                    .build();
        }
    }

}
