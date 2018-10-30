package ru.inasan.karchevsky.catalogues.tdsc;

import lombok.NoArgsConstructor;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

import java.io.Serializable;

/**
 * Created by Алекс on 10.02.2017.
 */
@NoArgsConstructor
public class NodeTDSC extends NodeForParsedCatalogue implements Serializable {
    public static String uniqueCatalogueID = "TDSC";
    public NodeTDSCHelperComponent el1;
    public NodeTDSCHelperComponent el2;

    @Override
    public Double getXel1() {
        return Double.valueOf(el1.params.get(KeysDictionary.X));
    }

    @Override
    public Double getYel1() {
        return Double.valueOf(el1.params.get(KeysDictionary.Y));
    }

    @Override
    public Double getXel2() {
        return Double.valueOf(el2.params.get(KeysDictionary.X));
    }

    @Override
    public Double getYel2() {
        return Double.valueOf(el2.params.get(KeysDictionary.Y));
    }
}


