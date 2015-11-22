package com.ramogi.xboxme.clustering.algo.clustering.algo;

import com.ramogi.xboxme.clustering.algo.clustering.Cluster;
import com.ramogi.xboxme.clustering.algo.clustering.ClusterItem;

import java.util.Collection;
import java.util.Set;

/**
 * Logic for computing clusters
 */
public interface Algorithm<T extends ClusterItem> {
    void addItem(T item);

    void addItems(Collection<T> items);

    void clearItems();

    void removeItem(T item);

    Set<? extends Cluster<T>> getClusters(double zoom);

    Collection<T> getItems();
}