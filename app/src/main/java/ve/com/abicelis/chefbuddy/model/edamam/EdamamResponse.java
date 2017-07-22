package ve.com.abicelis.chefbuddy.model.edamam;

import java.util.List;

/**
 * Created by abicelis on 21/7/2017.
 */

public class EdamamResponse {

    private String query;

    private List<EdamamHit> hits;

    public String getQuery() {
        return query;
    }

    public List<EdamamHit> getHits() {
        return hits;
    }
}
