package dev.unsivil.ai_redux.commands.util;

import dev.unsivil.ai_redux.AutoIgnitionRedux;


public abstract class PermBuilder {
    
    private static final String PLUGIN_NAME = AutoIgnitionRedux.getInstance()
        .getManifest().getName().toLowerCase();
    
    public enum Perm {
        
        BASE(PLUGIN_NAME),
        COMMANDS(BASE + ".commands"),
        CONFIG(COMMANDS + ".config"),
        GLOBAL(COMMANDS + ".global");
        
        private final String node;
        
        private Perm(String node) {
            this.node = node;
        }
        
        public String extend(String name) {
            return this.node.concat("." + name);
        }
        
        @Override
        public String toString() {
            return this.node;
        }
    }
}
