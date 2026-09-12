package com.example.fruitmachine.domain.support;

import com.example.fruitmachine.domain.ColourSelector;

import java.util.List;

/** A ColourSelector that returns a fixed, scripted sequence, so tests can force exact spins. */
public class ScriptedColourSelector implements ColourSelector {

    private final List<Integer> script;
    private int index = 0;

    public ScriptedColourSelector(Integer... colours) {
        this.script = List.of(colours);
    }

    @Override
    public int nextColour(int colourCount) {
        if (index >= script.size()) {
            throw new IllegalStateException("ScriptedColourSelector ran out of scripted colours");
        }
        return script.get(index++);
    }
}
