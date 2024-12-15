package ru.mipt.bit.platformer.config;

public class GameConfig {
    public TankModelConfig playerTankModelConfig;
    public TankModelConfig botTankModelConfig;
    public TreeModelConfig treeModelConfig;
    public LevelConfig levelConfig;

    public static class TankModelConfig {
        public float tankMovementSpeed;
        public float bulletMovementSpeed;
        public float bulletDefaultDamage;
        public String tankTexturePath;
        public String bulletTexturePath;
    }

    public static class TreeModelConfig {
        public String texturePath;
    }

    public static class LevelConfig {
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
}
