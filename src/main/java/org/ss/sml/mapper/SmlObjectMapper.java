package org.ss.sml.mapper;

import com.fasterxml.jackson.core.type.TypeReference;

public interface SmlObjectMapper {

    <T> T readSml(String smlStr, Class<T> clz);

    <T> T readSml(String smlStr, TypeReference<T> valueTypeRef);

    String writeMiniSml(Object object);

    String writePrettySml(Object object);
}
