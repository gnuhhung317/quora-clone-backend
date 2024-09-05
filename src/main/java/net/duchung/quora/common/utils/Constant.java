package net.duchung.quora.common.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Constant {
    public final static int COMMENT_POINTS = 1;
    public final static int VOTE_POINTS = 2;
    public final static int SEARCH_RESULTS_PER_PAGE = 10;
    public final static String IMAGE_FOLDER = "images";
    public final static String VIDEO_FOLDER = "videos";
    public final static int MAX_LIMIT = 4;
    public final static int MAX_RECOMMENDATION = 10;

    public final static float FOLLOWING_QUESTION_WEIGHT=2.2F;
    public final static float VIRAL_ANSWER_WEIGHT=2F;
    public final static float FOLLOWING_USER_ANSWER_WEIGHT=1.8F;
    public final static float FOLLOWING_USER_QUESTION_WEIGHT=1.6F;
    public final static float FOLLOWING_USER_FEED_WEIGHT=1.4F;
    public final static float VIRAL_ANSWER_ALL_TOPIC_WEIGHT=1.2F;
    public final static float RECENT_ANSWER_IN_TOPIC_WEIGHT=1F;
    public final static long BALANCE_ARGUMENT=1000000000;
}
