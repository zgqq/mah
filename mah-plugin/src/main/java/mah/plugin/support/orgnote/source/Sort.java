package mah.plugin.support.orgnote.source;


import mah.plugin.support.orgnote.entity.NodeEntity;

import java.util.List;

/**
 * Created by zgq on 10/1/16.
 */
public interface Sort {
    NodeEntity getNextReviewNote(List<NodeEntity> nodeEntityList);
}
