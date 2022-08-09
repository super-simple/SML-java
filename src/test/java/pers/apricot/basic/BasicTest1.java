package pers.apricot.basic;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.ss.sml.parse.SmlDataParser;
import org.ss.sml.util.IOs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BasicTest1 {
    @Test
    void test1() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/simple1.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlDataParser.parse(smlStr);
        System.out.println(parse);
    }

    @Test
    void test2() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/simple2.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlDataParser.parse(smlStr);
        System.out.println(parse);
    }
}
