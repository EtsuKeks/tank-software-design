package ru.mipt.bit.platformer.keepers;

public interface Observer {
    void notifyBorn(Object o);
    void notifyDead(Object o);
    void commitKill();
}
