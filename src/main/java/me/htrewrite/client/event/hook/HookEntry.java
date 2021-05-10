package me.htrewrite.client.event.hook;

public class HookEntry {
    public final String eventName;
    public final Object[] args;
    public HookEntry(String eventName, Object... args) {
        this.eventName = eventName;
        this.args = args;
    }
}