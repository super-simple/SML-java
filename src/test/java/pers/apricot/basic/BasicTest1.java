package pers.apricot.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ss.sml.parse.SmlDataParser;
import org.ss.sml.util.IOs;
import org.ss.sml.util.ObjectMappers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BasicTest1 {

    private ObjectMapper objectMapper = ObjectMappers.getObjectMapper();

    @Test
    void test1() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/data1.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlDataParser.parse(smlStr);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse));
    }

    @Test
    void test2() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/origin1.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlDataParser.parse(smlStr);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse));
    }

    @Test
    void test3() throws IOException {
        String smlStr;
        try (InputStream resourceAsStream = BasicTest1.class.getResourceAsStream("/basic/origin2.sml")) {
            smlStr = IOs.toString(resourceAsStream, StandardCharsets.UTF_8);
        }
        JsonNode parse = SmlDataParser.parse(smlStr);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parse));
    }
}
