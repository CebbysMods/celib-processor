package lv.cebbys.mcmods.celib.processor;

import lv.cebbys.mcmods.celib.processor.exception.ValidationException;
import lv.cebbys.mcmods.celib.processor.parsers.MapResourceElementParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapResourceElementParserTests {

    @Test
    void test0_stringValidation() {
        MapResourceElementParser parser = new MapResourceElementParser();
        String valid0 = "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"test\", method=\"testMethod\")";
        String valid1 = "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"te st-_.#A\", method=\"testMethod\")";
        String valid2 = "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"te s t\", method=\"testasda08Method\")";
        assertDoesNotThrow(() -> parser.validateString(valid0));
        assertDoesNotThrow(() -> parser.validateString(valid1));
        assertDoesNotThrow(() -> parser.validateString(valid2));
        String invalid0 = "lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"test\", method=\"testMethod\")";
        String invalid1 = "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"te,st\", method=\"testMethod\")";
        String invalid2 = "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(method=\"testM_ethod\", json=\"asd\")";
        assertThrows(ValidationException.class, () -> parser.validateString(invalid0));
        assertThrows(ValidationException.class, () -> parser.validateString(invalid1));
        assertThrows(ValidationException.class, () -> parser.validateString(invalid2));
    }

    @Test
    void test1_annotationMirrorToMethodList() throws ValidationException {
        MapResourceElementParser parser = new MapResourceElementParser();
        assertEquals(
                "{json=\"test\", method=\"testMethod\"}",
                parser.parseCompoundString(
                        "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"test\", method=\"testMethod\")"
                )
        );
        assertEquals(
                "{json=\"test2_must_work\"}",
                parser.parseCompoundString(
                        "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"test2_must_work\")"
                )
        );
        assertEquals(
                "{json=\"test 1\"}",
                parser.parseCompoundString(
                        "@lv.cebbys.mcmods.celib.processor.utility.DefinedName(json=\"test 1\")"
                )
        );
    }
}
