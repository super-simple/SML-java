package org.ss.basic;

import org.junit.jupiter.api.Test;
import org.ss.smljava.mapper.SmlMapper;
import org.ss.smljava.util.IOs;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SmlMapperTest {
    @Test
    void test1() {
        InputStream resourceAsStream = SimpleTest.class.getResourceAsStream("/basic/simple.sml");
        String documentStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        SmlMapper smlMapper = new SmlMapper();
        Map map = smlMapper.readValue(documentStr, Map.class);
        System.out.println(map.getClass());
        System.out.println(map);
    }
}
