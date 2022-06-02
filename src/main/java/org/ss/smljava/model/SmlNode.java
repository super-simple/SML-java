package org.ss.smljava.model;

import java.util.List;

public class SmlNode {
    private String name;
    private List<SmlAttributePair> pairList;
    private List<SmlElement> elementList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SmlAttributePair> getPairList() {
        return pairList;
    }

    public void setPairList(List<SmlAttributePair> pairList) {
        this.pairList = pairList;
    }

    public List<SmlElement> getElementList() {
        return elementList;
    }

    public void setElementList(List<SmlElement> elementList) {
        this.elementList = elementList;
    }
}
