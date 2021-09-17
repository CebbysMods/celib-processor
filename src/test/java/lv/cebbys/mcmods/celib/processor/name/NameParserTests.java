package lv.cebbys.mcmods.celib.processor.name;

import lv.cebbys.mcmods.celib.processor.exception.ValidationException;
import lv.cebbys.mcmods.celib.processor.parsers.DefinedNamesParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameParserTests {
    @Test
    void test0_DefinedNameParsing() throws ValidationException {
        String inp = "test0";
        String out = "test0";
        assertEquals(out, new DefinedNamesParser().parse(inp));
        inp = "test 1";
        out = "test1";
        assertEquals(out, new DefinedNamesParser().parse(inp));
        inp = "test 2 is cool";
        out = "test2IsCool";
        assertEquals(out, new DefinedNamesParser().parse(inp));
        inp = "test 3_is-amazing";
        out = "test3IsAmazing";
        assertEquals(out, new DefinedNamesParser().parse(inp));
        inp = "test 4_is-error-_/.proof";
        out = "test4IsErrorProof";
        assertEquals(out, new DefinedNamesParser().parse(inp));
    }
}
