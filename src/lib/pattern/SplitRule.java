package lib.pattern;

/**
 * Created by Алекс on 24.11.2016.
 */

/**
 * Base interface. Split rules for slice large files on small, to increase processing speed
 * Define corresponding criteria for current @string from catalogue to current @hour and @decade
*/
public interface SplitRule {
    boolean StringCorrespondsToFile(String string, int hour, int decade);
}
