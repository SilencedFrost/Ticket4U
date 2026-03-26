package com.ticket4u.event.constants;

public class RelatedEvents {
    public static final int MAX_COUNT = 8;
    public static final int FETCH_LIMIT_MULTIPLIER = 5;
    /**
     * Weights and cutoff for relevancy feature
     * Outside of cutoff ~ 0 relevance
     */
    public static final class WEIGHTS {
        // General weight for semantic similarity
        public static final int SEMANTIC = 600;
        // Fallback weight is semantic is not available
        public static final int CATEGORY = 300;
        // Locational data is somewhat important, because concerts and events tends to have "exclusivity" so users tend to spend more time travelling to it
        // TODO: implement dynamic location weight based on event size (fanbase) and user behavior
        public static final int LOCATION = 150;
        // unit: km
        public static final int LOCATION_CUTOFF = 25;
        // Steep slope
        public static final float LOCATION_SIGMOID_BIAS = 0.15f;
        public static final float LOCATION_SIGMOID_WEIGHT = 25;
        // Date data almost make no difference unless it's specific dates like weekends (which can't be sorted by relevancy)
        public static final int DATE = 100;
        // unit: days
        // 3 weeks is a good buffer for "discovery" mode
        public static final int DATE_CUTOFF = 21;
        // Gradual slope
        public static final float DATE_SIGMOID_BIAS = 0.25f;
        public static final float DATE_SIGMOID_WEIGHT = 9;
    }
}
