package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation;

import ru.inasan.karchevsky.lib.errorHandling.ValueAlreadyExistsAndNotEqualsException;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

public class MatchingByIDRuleImplementation extends CachedStorage {

    public static ArrayList<? extends NodeForParsedCatalogue> resolve(String key, ArrayList<? extends NodeForParsedCatalogue> list,
                                                                      Datasource datasourceClass,
                                                                      CachedStorage cachedStorage) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            boolean alreadyMatched = false;
            boolean tooManyMatches = false;
            Pair matchedTo = null;
            for (int j = 0; j < cachedStorage.getSysList().size(); j++) {
                for (int k = 0; k < cachedStorage.getSysList().get(j).pairs.size(); k++) {
                    if (list.get(i).params.containsKey(key)) {
                        ArrayList<String> values = cachedStorage.getSysList().get(j).pairs.get(k).getParamsForByKey(key);
                        if (values.size() != 0) {
                            for (String value : values) {
                                if (list.get(i).params.get(key).equals(value)) {
                                    if (!alreadyMatched) {
                                        alreadyMatched = true;
                                        matchedTo = cachedStorage.getSysList().get(j).pairs.get(k);
                                        break;
                                    } else {
                                        tooManyMatches = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!tooManyMatches && alreadyMatched) {
                try {
                    System.err.println("MATCH! " + key + " " + list.get(i).params.get(key));
                    //System.err.println(list.get(i).source);
                    datasourceClass.getClass().newInstance().propagate(matchedTo, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                } catch (Exception e) {
                    /**System.err.println(e.getMessage());*/
                }
            } else {
                //System.err.println("TOO MANY MATCHES! "+ key + " "+ list.get(i).source);
            }
        }
        return list;
    }

    private static ArrayList<Pair> allMatchesForGlobalSystems(String key, NodeForParsedCatalogue node,
                                                              Datasource datasourceClass,
                                                              CachedStorage cachedStorage) {
        ArrayList<Pair> resultList = new ArrayList<>();
        for (int j = 0; j < cachedStorage.getSysList().size(); j++) {
            for (int k = 0; k < cachedStorage.getSysList().get(j).pairs.size(); k++) {
                if (node.params.containsKey(key)) {
                    ArrayList<String> values = cachedStorage.getSysList().get(j).pairs.get(k).getParamsForByKey(key);
                    if (values.size() != 0) {
                        for (String value : values) {
                            if (node.params.get(key).equals(value)) {
                                resultList.add(cachedStorage.getSysList().get(j).pairs.get(k));
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }

    private static ArrayList<Pair> allMatchesForPairSet(String key, NodeForParsedCatalogue node, Datasource datasourceClass, ArrayList<Pair> pairs) {
        ArrayList<Pair> resultList = new ArrayList<>();
        for (int k = 0; k < pairs.size(); k++) {
            if (node.params.containsKey(key)) {
                ArrayList<String> values = pairs.get(k).getParamsForByKey(key);
                if (values.size() != 0) {
                    for (String value : values) {
                        if (node.params.get(key).equals(value)) {
                            resultList.add(pairs.get(k));
                        }
                    }
                }
            }
        }
        return resultList;
    }

    public static ArrayList<? extends NodeForParsedCatalogue> multipleIdCriteria(String[] keySet,
                                                                                 ArrayList<? extends NodeForParsedCatalogue> list,
                                                                                 Datasource datasourceClass,
                                                                                 CachedStorage cachedStorage) {
        ArrayList<NodeForParsedCatalogue> cache = new ArrayList<>();
        for (NodeForParsedCatalogue entry : list) {
            ArrayList<Pair> resultList = allMatchesForGlobalSystems(keySet[0], entry, datasourceClass, cachedStorage);
            for (int i = 1; i < keySet.length; i++) {
                resultList = allMatchesForPairSet(keySet[i], entry, datasourceClass, resultList);
            }
            if (resultList.size() == 1) {
                try {
                    datasourceClass.getClass().newInstance().propagate(resultList.get(0), entry);
                    cache.add(entry);
                } catch (ValueAlreadyExistsAndNotEqualsException ex) {
                    /**System.err.println(ex.getMessage());*/
                    cache.add(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultList.size() > 1) {
                /**System.err.println("WEEK CRITERIA: too many matches found:"+resultList.size()+keySet.toString());*/
            }
        }
        list.removeAll(cache);
        return list;
    }
}
