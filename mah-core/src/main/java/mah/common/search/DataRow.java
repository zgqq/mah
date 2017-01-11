package mah.common.search;

import java.util.List;

/**
 * Created by zgq on 2017-01-11 12:33
 */
public class DataRow implements Searchable{

    private final List<String> data;

    public DataRow(List<String> data) {
        this.data = data;
    }

    @Override
    public List<String> fetchData() {
        return data;
    }
}
