package com.github.denisura.nytseacher.data.network;

import java.util.HashMap;
import java.util.Map;

public class ArticleSearchOptions extends HashMap<String, String> {

    private final static String QUERY = "q";
    private final static String BEGIN_DATE = "begin_date";
    private final static String SORT = "sort"; //(newest,oldest,none)
    private final static String PAGE = "page";
    private final static String LIST_FIELDS = "fl";

    public static class Builder {
        private static Map<String, String> options = new HashMap<>();

        static {
            options.put(PAGE, "0");
            options.put(LIST_FIELDS, "headline,multimedia,web_url");
        }

        public Builder(String query) {
            if (!query.isEmpty()) {
                options.put(QUERY, query);
            }
        }

        public Builder beginDate(String val) {
            options.put(BEGIN_DATE, val);
            return this;
        }

        public Builder sort(String val) {
            options.put(SORT, val);
            return this;
        }

        public Builder page(int val) {
            options.put(PAGE, String.valueOf(val));
            return this;
        }

        public ArticleSearchOptions build() {
            return new ArticleSearchOptions(this);
        }
    }

    private ArticleSearchOptions(Builder builder) {
        this.putAll(builder.options);
    }
}
