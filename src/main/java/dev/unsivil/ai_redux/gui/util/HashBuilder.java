package dev.unsivil.ai_redux.gui.util;

import java.util.StringJoiner;


public abstract class HashBuilder {
    public static final String fullQualifiedPath(String... hashID) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String id : hashID) {
            if (!id.startsWith("#")) id = '#' + id;
            joiner.add(id);
        }
        
        return joiner.toString();
    }
}
