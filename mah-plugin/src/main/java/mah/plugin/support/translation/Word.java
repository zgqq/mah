package mah.plugin.support.translation;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zgq on 16-12-4.
 */
public class Word {
    private String word;
    private List<String> translations;
    private List<String> explains;
    private List<MeaningPair> meaningPairs;
    private volatile Date latestQueryTime;
    private AtomicInteger querCount=new AtomicInteger();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public Date getLatestQueryTime() {
        return latestQueryTime;
    }

    public void setLatestQueryTime(Date latestQueryTime) {
        this.latestQueryTime = latestQueryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word1 = (Word) o;

        return word.equals(word1.word);
    }

    public List<MeaningPair> getMeaningPairs() {
        return meaningPairs;
    }

    public void setMeaningPairs(List<MeaningPair> meaningPairs) {
        this.meaningPairs = meaningPairs;
    }

    public AtomicInteger getQuerCount() {
        return querCount;
    }

    public void setQuerCount(AtomicInteger querCount) {
        this.querCount = querCount;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    public int getQueryCount() {
        return querCount.get();
    }

    public int incrementQueryCount() {
        return querCount.incrementAndGet();
    }

    public void updateQueryTime() {
        this.latestQueryTime = new Date();
    }


    public static class MeaningPair  {
        private final String key;
        private final List<String> value;

        public MeaningPair(String key, List<String> value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public List<String> getValue() {
            return value;
        }
    }
}
