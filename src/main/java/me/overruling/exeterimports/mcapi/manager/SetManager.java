package me.overruling.exeterimports.mcapi.manager;

import me.overruling.client.Overruling;

import java.util.HashSet;
import java.util.Set;

public class SetManager<T> implements Manager<T> {
    protected final Overruling overruling;

    private final Set<T> set;

    public SetManager() {
        this.set = new HashSet<>();
        overruling = Overruling.INSTANCE;
    }

    @Override
    public void register(T object) {
        set.add(object);
    }

    @Override
    public void unregister(T object) {
        set.remove(object);
    }

    @Override
    public int size() {
        return set.size();
    }

    public Set<T> getSet() {
        return set;
    }
}