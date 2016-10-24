package com.github.denisura.nytseacher.data.network;

import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.data.model.filter.SortOption;

import java.util.HashMap;
import java.util.Map;

import static com.github.denisura.nytseacher.utils.JodaUtils.formatAPIDate;

public class ArticleSearchOptions extends HashMap<String, String> {

    private final static String QUERY = "q";
    private final static String BEGIN_DATE = "begin_date";
    private final static String SORT = "sort"; //(newest,oldest,none)
    private final static String PAGE = "page";
    private final static String LIST_FIELDS = "fl";
    private final static String FILTER_QUERY = "fq";

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

        public Builder filter(SearchFilter filter) {
            if (filter.getBeginDate() != null) {
                beginDate(formatAPIDate(filter.getBeginDate()));
            }

            if (filter.getSort() != null && !filter.getSort().equals(SortOption.SORT_RELEVANCY)) {
                sort(filter.getSort());
            }

            if (filter.getNewsDesk() != null && !filter.getNewsDesk().isEmpty()) {
                String value = "news_desk:(\"" + android.text.TextUtils.join("\" \"", filter.getNewsDesk()) + "\")";
                options.put(FILTER_QUERY, value);
            }

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
