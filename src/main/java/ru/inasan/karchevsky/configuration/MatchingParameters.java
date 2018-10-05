package ru.inasan.karchevsky.configuration;

/**
 * Created by Алекс on 08.01.2017.
 */

/**
 * Base interface. Contains default configuration for matching selector.
 * Serve only for increasing calculation speed.
 * !IMPORTANT! For production version all values should be decreased.
 */

public interface MatchingParameters {
    double COORDINATES_MATCHING_LIMIT_SAME = 0.005;
    double COORDINATES_MATCHING_LIMIT_SAME_EQ = 0.0005;
    double COORDINATES_MATCHING_LIMIT_EQUALS = 0.0000003;
    double ANGLE_MATCHING_LIMIT = 0.2;
}
