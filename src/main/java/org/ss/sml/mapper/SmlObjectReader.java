package org.ss.sml.mapper;

public interface SmlObjectReader {
    <T> T readSml(String smlStr, Class<T> clz);
}
