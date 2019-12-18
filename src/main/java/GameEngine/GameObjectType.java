package GameEngine;

/**
 * The enum Game object type.
 */
public enum GameObjectType {
    /**
     * Solid game object type is used for objects that collide and can't go through other solids.
     */
    SOLID,
    /**
     * Sensor game object type is used for objects that have body that registers collision with other objects,
     * but lets them move through.
     */
    SENSOR,
    /**
     * Decorative game object type is used for objects that wont interact with other objects in the world.
     */
    DECORATIVE
}
