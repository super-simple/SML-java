package org.ss.smljava.smlmodel;

import java.util.List;

public class SmlHeader {
    private String name;
    private List<SmlAttributePair> pairList;

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
}
