package ILBprocessing.beans;

import ILBprocessing.beans.helpers.CCDMHelperComponent;
import lib.pattern.NodeForParsedCatalogue;

public class NodeCCDM extends NodeForParsedCatalogue {
    public static String uniqueCatalogueID = "NodeCCDM";

    public CCDMHelperComponent el1;
    public CCDMHelperComponent el2;
    public NodeCCDM() {
    }
}
