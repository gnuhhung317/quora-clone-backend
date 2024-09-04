package net.duchung.quora.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {
    public final static int COMMENT_POINTS = 1;
    public final static int VOTE_POINTS = 2;
    public final static int SEARCH_RESULTS_PER_PAGE = 10;
    public final static String IMAGE_FOLDER = "images";
    public final static String VIDEO_FOLDER = "videos";
    public final static int MAX_LIMIT = 4;
    public final static int MAX_RECOMMENDATION = 10;
    public static LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // Convert LocalDateTime to Timestamp
    public static Timestamp convertToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

}
