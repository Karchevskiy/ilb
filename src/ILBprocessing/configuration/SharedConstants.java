package ILBprocessing.configuration;

public interface SharedConstants {
    String FILE_NAME_DEFAULT = "020517part";
    String FILE_RESULT_FORMAT = ".txt";

    String INPUT_FOLDER = "C:/WDSparser/";
    String OUTPUT_FOLDER = "C:/WDSparser2/";

    boolean REMOVE_DUPLICATED_ENTITIES = false;

    String WDS_GENERATED_STUFF = "WDS";
    String CCDM_GENERATED_STUFF = "CCDM";
    String TDSC_GENERATED_STUFF = "TDSC";
    String INT4_GENERATED_STUFF = "INT";

    boolean WDS_SPLIT_BEFORE_PROCESSING=false;
    boolean CCDM_SPLIT_BEFORE_PROCESSING=false;
    boolean INT4_SPLIT_BEFORE_PROCESSING=false;
    boolean TDSC_SPLIT_BEFORE_PROCESSING=false;
    String[] WDS_SOURCE_FILES = {"data06.txt", "data12.txt", "data18.txt", "data24.txt"};
    String[] CCDM_SOURCE_FILES = {"CCDMCORR.dat"};
    String[] TDSC_SOURCE_FILES = {"dataTDSC.dat"};
    String[] INT4_SOURCE_FILES = {"dataINT4.txt"};


    String SCO_SOURCE_FILE = "dataSCO.txt";
    String CEV_SOURCE_FILE = "dataCEV.txt";
    String SB9_SOURCE_FILE = "dataSB9main.txt";
    String SB9_PARAMS_FILE = "dataSB9.dta";
    String ILB_RESULT_FILE = "ILB.txt";

    boolean LOGGING_LEVEL_VERBOSE_ENABLED=false;
    boolean LOGGING_LEVEL_SOURCE_ENABLED=false;

}
