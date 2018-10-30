package ru.inasan.karchevsky.catalogues.ccdm;

import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

import java.io.Serializable;

public class NodeCCDM extends NodeForParsedCatalogue implements Serializable {
    public static String uniqueCatalogueID = "CCDM";

    public CCDMHelperComponent el1;
    public CCDMHelperComponent el2;
    boolean coordinatesNotFoundInCCDM = false;

    @Override
    public Double getXel1() {
        return el1.x;
    }

    @Override
    public Double getYel1() {
        return el1.y;
    }

    @Override
    public Double getXel2() {
        return el2.x;
    }

    @Override
    public Double getYel2() {
        return el2.y;
    }
}
