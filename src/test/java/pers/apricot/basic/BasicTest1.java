package pers.apricot.basic;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.ss.sml.parse.SmlNoMixParser;
import org.ss.sml.util.IOs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BasicTest1 {
    @Test
    void test1() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/simple1.sml")) {
            smlStr = IOs.toString(BasicTest1.class.getResourceAsStream("/basic/simple1.sml"), StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlNoMixParser.parse(smlStr);
        System.out.println(parse);
    }
}
