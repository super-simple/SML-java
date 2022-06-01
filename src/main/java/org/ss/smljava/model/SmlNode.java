package org.ss.smljava.model;

import java.util.List;

public class SmlNode {
    private String name;
    private SmlAttribute attribute;
    private List<SmlElement> elementList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SmlAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(SmlAttribute attribute) {
        this.attribute = attribute;
    }

    public List<SmlElement> getElementList() {
        return elementList;
    }

    public void setElementList(List<SmlElement> elementList) {
        this.elementList = elementList;
    }
}
