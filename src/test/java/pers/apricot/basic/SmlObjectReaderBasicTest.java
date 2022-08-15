package pers.apricot.basic;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.ss.sml.mapper.SmlObjectReader;
import org.ss.sml.mapper.SmlObjectReaderImpl;
import org.ss.sml.util.IOs;
import org.ss.sml.util.ObjectMappers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SmlObjectReaderBasicTest {
    @Test
    void test1() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/simple2.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        SmlObjectReader smlObjectReader = new SmlObjectReaderImpl();
        Map map = smlObjectReader.readSml(smlStr, Map.class);
        ObjectWriter objectWriter = ObjectMappers.getObjectMapper().writerWithDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(map));
    }
}
