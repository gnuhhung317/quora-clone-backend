package net.duchung.quora.common.utils;

public class Constant {
    public static final int COMMENT_POINTS = 1;
    public static final int VOTE_POINTS = 2;
    public static final int SEARCH_RESULTS_PER_PAGE = 10;
    public static final String IMAGE_FOLDER = "images";
    public static final String VIDEO_FOLDER = "videos";
    public static final int MAX_LIMIT = 4;
    public static final int MAX_RECOMMENDATION = 10;

    public static final float FOLLOWING_QUESTION_WEIGHT = 2.2F;
    public static final float VIRAL_ANSWER_WEIGHT = 2F;
    public static final float FOLLOWING_USER_ANSWER_WEIGHT = 1.8F;
    public static final float FOLLOWING_USER_QUESTION_WEIGHT = 1.6F;
    public static final float FOLLOWING_USER_FEED_WEIGHT = 1.4F;
    public static final float VIRAL_ANSWER_ALL_TOPIC_WEIGHT = 1.2F;
    public static final float RECENT_ANSWER_IN_TOPIC_WEIGHT = 1F;
    public static final long BALANCE_ARGUMENT = 1000000000;

    private Constant() {
    }


}
