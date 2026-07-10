package utilz;

import main.Game;

/**
 * The Constants class holds constant values for various aspects of the game, including object types,
 *  * enemy attributes, UI settings, and player actions.
 */
public class Constants {

    public static class ObjectConstants {

        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int TRAP = 3;
        public static final int COIN = 3;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int TRAP_WIDTH_DEFAULT = 32;
        public static final int TRAP_HEIGHT_DEFAULT = 32;
        public static final int TRAP_WIDTH = (int) (Game.SCALE * TRAP_WIDTH_DEFAULT);
        public static final int TRAP_HEIGHT = (int) (Game.SCALE * TRAP_HEIGHT_DEFAULT);

        public static final int COIN_WIDTH_DEFAULT = 12;
        public static final int COIN_HEIGHT_DEFAULT = 16;
        public static final int COIN_WIDTH = (int) (Game.SCALE * COIN_WIDTH_DEFAULT);
        public static final int COIN_HEIGHT = (int) (Game.SCALE * COIN_HEIGHT_DEFAULT);


        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case BARREL:
                    return 8;
            }
            return 1;
        }
    }

    public static class EnemyConstants {
        public static final int ORC = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int ORC_WIDTH_DEFAULT = 64;
        public static final int ORC_HEIGHT_DEFAULT = 32;

        public static final int ORC_WIDTH = (int) (ORC_WIDTH_DEFAULT * Game.SCALE);
        public static final int ORC_HEIGHT = (int) (ORC_HEIGHT_DEFAULT * Game.SCALE);

        public static final int ORC_DRAWOFFSET_X = (int)(26 *Game.SCALE);
        public static final int ORC_DRAWOFFSET_Y = (int)(3 *Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case ORC:
                    switch (enemy_state) {
                        case DEAD:
                            return 2;
                        case IDLE:
                            return 2;
                        case RUNNING:
                            return 2;
                        case ATTACK:
                            return 2;
                        case HIT:
                            return 2;
                    }
            }

            return 0;

        }

        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case ORC:
                    return 10;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case ORC:
                    return 10;
                default:
                    return 0;
            }

        }
    }
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class URMButtons{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
        }
    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
        public static final int UP = 1;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int DEAD = 4;
        public static final int HIT = 5;
        public static final int ATTACK = 6;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case DEAD:
                    return 2;
                case RUNNING:
                    return 2;
                case IDLE:
                    return 2;
                case JUMP:
                    return 2;
                case FALLING:
                    return 2;
                case HIT:
                    return 2;
                case ATTACK:
                    return 2;
                    default:
                        return 1;

            }
        }
    }
}
