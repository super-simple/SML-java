package org.ss.smljava.model;

public class SmlDocument {
    private SmlHeader sml;
    private SmlNode root;

    public SmlHeader getSml() {
        return sml;
    }

    public void setSml(SmlHeader sml) {
        this.sml = sml;
    }

    public SmlNode getRoot() {
        return root;
    }

    public void setRoot(SmlNode root) {
        this.root = root;
    }
}
