package ILBprocessing;

import ILBprocessing.storage.CachedStorageILB;
import WDSNotesParser.Base;
import WDSNotesParser.StorageEntity;
import ILBprocessing.configuration.KeysDictionary;

import java.util.ArrayList;

/**
 * Created by Алекс on 06.09.2016.
 */
public class NotesInterpreter extends CachedStorageILB {
    public static ArrayList<StorageEntity> storage;
    public static void interpreteNotes(){
        storage= Base.storage;
        for(int i=0;i<storage.size();i++){
            for(int sys = 0; sys<sysList.size(); sys++){
                if(sysList.get(sys).params.get(KeysDictionary.WDSSYSTEM).equals(storage.get(i).system)){
                    for(int j = 0; j<sysList.get(sys).pairs.size(); j++){
                        //TODO here stuff with observer
                        /*if(sysList.get(sys).pairs.get(j).params.get(KeysDictionary.OBSERVER).equals(storage.get(i).pair.substring(0,7))){

                                if (storage.get(i).object.equals(Dictionary.PAIR)){
                                    sysList.get(sys).pairs.get(j).params.put(storage.get(i).property,""+storage.get(i).yORn);
                                }else if(storage.get(i).object.equals(Dictionary.PRIMARY)){
                                    sysList.get(sys).pairs.get(j).el1.params.put(storage.get(i).property,""+storage.get(i).yORn);
                                }else if(storage.get(i).object.equals(Dictionary.THIS)){
                                    sysList.get(sys).pairs.get(j).params.put(storage.get(i).property,""+storage.get(i).yORn);
                                }else if(storage.get(i).object.equals(Dictionary.PRINCIPAL)){
                                    sysList.get(sys).pairs.get(j).el1.params.put(storage.get(i).property,""+storage.get(i).yORn);
                                }else if(storage.get(i).object.equals(Dictionary.SECONDARY)){
                                    sysList.get(sys).pairs.get(j).el2.params.put(storage.get(i).property,""+storage.get(i).yORn);
                                }else{
                                    if(sysList.get(sys).pairs.get(j).el1.nameInILB.equals(storage.get(i).object)){
                                        sysList.get(sys).pairs.get(j).el1.params.put(storage.get(i).property,""+storage.get(i).yORn);
                                    }else if(sysList.get(sys).pairs.get(j).el2.nameInILB.equals(storage.get(i).object)){
                                        sysList.get(sys).pairs.get(j).el2.params.put(storage.get(i).property,""+storage.get(i).yORn);
                                    }
                                }
                            Base.storage.remove(i);
                        }*/
                    }
                }
            }
        }
    }
}
