package com.ramogi.xboxme;

import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryPlusCallBack {

    public abstract void querycomplete(List<GamersLocation> gamersLocationList);
}
