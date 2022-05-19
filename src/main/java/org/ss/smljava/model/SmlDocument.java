package org.ss.smljava.model;

public class SmlDocument {
    private SmlNode sml;
    private SmlNode root;

    public SmlNode getSml() {
        return sml;
    }

    public void setSml(SmlNode sml) {
        this.sml = sml;
    }

    public SmlNode getRoot() {
        return root;
    }

    public void setRoot(SmlNode root) {
        this.root = root;
    }
}
