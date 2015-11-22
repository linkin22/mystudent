package com.ramogi.xboxme;

import com.ramogi.xbox.backend.gamersLocationApi.model.GamersLocation;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ROchola on 11/22/2015.
 */
public abstract class QueryPlusCallBack {

    public abstract void querycomplete(ArrayList<Map<String, Object>> list);
}
