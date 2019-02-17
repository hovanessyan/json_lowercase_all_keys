package com.stackoverflow.demo.demo;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class LowerCaseJsonTest {

    @Test
    public void name() throws IOException {

        ObjectMapper mapper = new ObjectMapper(new JsonFactory() {
            @Override
            protected JsonParser _createParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException {
                return new KeysToLowercaseParser(super._createParser(data, offset, len, ctxt));
            }

            @Override
            protected JsonParser _createParser(InputStream in, IOContext ctxt) throws IOException {
                return new KeysToLowercaseParser(super._createParser(in, ctxt));
            }

            @Override
            protected JsonParser _createParser(Reader r, IOContext ctxt) throws IOException {
                return new KeysToLowercaseParser(super._createParser(r, ctxt));
            }

            @Override
            protected JsonParser _createParser(char[] data, int offset, int len, IOContext ctxt, boolean recyclable)
                    throws IOException {
                return new KeysToLowercaseParser(super._createParser(data, offset, len, ctxt, recyclable));
            }
        });


        File file = new File("src/main/resources/test.json");
        JsonNode jsonNode = mapper.readTree(file);
        String output = mapper.writeValueAsString(jsonNode);
        System.out.println(output);
    }

}

class KeysToLowercaseParser extends JsonParserDelegate {
    KeysToLowercaseParser(JsonParser d) {
        super(d);
    }

    @Override
    public String getCurrentName() throws IOException {
        if (hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
            return delegate.getCurrentName().toLowerCase();
        }
        return delegate.getCurrentName();
    }

    @Override
    public String getText() throws IOException {
        if (hasTokenId(JsonTokenId.ID_FIELD_NAME)) {
            return delegate.getText().toLowerCase();
        }
        return delegate.getText();
    }
}

