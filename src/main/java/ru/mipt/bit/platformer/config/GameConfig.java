package ru.mipt.bit.platformer.config;

public class GameConfig {
    public float movementSpeed;
    public String tankTexturePath;
    public String treeTexturePath;
    public String levelType;
    public String levelDescPath;
    public MapSize mapSize;
    public int treesMinCount;
    public int treesMaxCount;
    public int botTanksMinCount;
    public int botTanksMaxCount;

    public static class MapSize {
        public int width;
        public int height;
    }
}