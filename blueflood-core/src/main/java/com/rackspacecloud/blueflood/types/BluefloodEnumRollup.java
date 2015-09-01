package com.rackspacecloud.blueflood.types;

import java.io.IOException;
import java.util.*;

public class BluefloodEnumRollup implements Rollup {
    private Map<String, Long> rawEnumValues = new HashMap<String, Long>();
    private Map<Long,Long> hashedEnum2Value = new HashMap<Long, Long>();

    public BluefloodEnumRollup withEnumValue(String valueName, Long value) {
        this.rawEnumValues.put(valueName, value);
        this.hashedEnum2Value.put((long) valueName.hashCode(), value);
        return this;
    }

    public BluefloodEnumRollup withHashedEnumValue(Long hashedEnumValue, Long value) {
        this.hashedEnum2Value.put(hashedEnumValue, value);
        return this;
    }

    @Override
    public Boolean hasData() {
        return true;
    }

    @Override
    public RollupType getRollupType() {
        return RollupType.ENUM;
    }

    public int getCount() {
        return hashedEnum2Value.size();
    }

    public Map<Long, Long> getHashes() {
        return this.hashedEnum2Value;
    }

    public Map<String, Long> getRawValues() { return this.rawEnumValues; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BluefloodEnumRollup)) {
            return false;
        }
        BluefloodEnumRollup other = (BluefloodEnumRollup)obj;
        return hashedEnum2Value.equals(other.hashedEnum2Value);
    }

    public static BluefloodEnumRollup buildRollupFromEnumRollups(Points<BluefloodEnumRollup> input) throws IOException {
        BluefloodEnumRollup rollup = new BluefloodEnumRollup();
        for (Points.Point<BluefloodEnumRollup> point : input.getPoints().values()) {
            Map<Long,Long> mapFromPoint = point.getData().getHashes();
            for (Long i : mapFromPoint.keySet()) {
                if (rollup.getHashes().containsKey(i)) {
                    Long value = rollup.getHashes().get(i);
                    value += mapFromPoint.get(i);
                    rollup.getHashes().put(i,value);
                }
                else {
                    rollup.getHashes().put(i, mapFromPoint.get(i));
                }
            }
        }
        return rollup;
    }

}
