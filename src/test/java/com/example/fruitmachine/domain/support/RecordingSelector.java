package com.example.fruitmachine.domain.support;

import com.example.fruitmachine.domain.ColourSelector;

// src/test/java/com/example/fruitmachine/domain/support/RecordingSelector.java
public class RecordingSelector implements ColourSelector {

    public int lastColourCount = -1;
    public int calls = 0;

    @Override public int nextColour(int colourCount) {
        this.lastColourCount = colourCount;
        this.calls++;
        return 0;
    }
}
