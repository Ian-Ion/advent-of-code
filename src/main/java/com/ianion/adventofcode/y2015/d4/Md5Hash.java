package com.ianion.adventofcode.y2015.d4;

import lombok.Builder;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Stream;

@Builder(toBuilder = true)
public record Md5Hash(
        String key,
        int salt,
        String hash
) {

    public static int findLowestSaltProducingHashStartingWith(String key, String prefix) {
        return Stream.iterate(
                        Md5Hash.begin(key),
                        iterator -> !iterator.hash.startsWith(prefix),
                        Md5Hash::next)
                .reduce(Md5Hash.begin(key), (first, second) -> second)
                .next().salt;
    }

    private static Md5Hash begin(String key) {
        return Md5Hash.builder()
                .key(key)
                .salt(1)
                .hash(DigestUtils.md5Hex(key.concat(String.valueOf(1))))
                .build();
    }

    private Md5Hash next() {
        return this.toBuilder()
                .salt(salt + 1)
                .hash(DigestUtils.md5Hex(key.concat(String.valueOf(salt + 1))))
                .build();
    }
}

