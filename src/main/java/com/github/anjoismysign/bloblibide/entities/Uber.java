package com.github.anjoismysign.bloblibide.entities;

/**
 * A class that holds any object with the purpose of being updated multiple times and later being retrieved.
 * A perfect use of this case is inside lambda expressions since it forces you to have a final variable (which means
 * you can no longer assign a new value to it) and you can't use a global variable since it's not final, but you can
 * still have a final variable and update their attributes through setters and later retrieve them through
 * getters.
 *
 * @param <T>
 */
public class Uber<T> {
    private T value;

    /**
     * Creates a new Uber object with the given value.
     * An example of how to use it:
     * <p>
     * //check how 'uber' is used in the lambda expression without being final
     * Uber&lt;Integer&gt; uber = Uber.drive(0);
     * () -> {
     * uber.talk(uber.thanks() + 3);
     * uber.talk(uber.thanks() * 5);
     * };
     * System.out.println(uber.thanks());
     * // Prints 15
     *
     * @param value The default value to be stored.
     * @param <T>   The type of the value.
     * @return The Uber object.
     */
    public static <T> Uber<T> drive(T value) {
        return new Uber<>(value);
    }

    /**
     * Creates a new Uber object with a null value.
     * An example of how to use it:
     * <p>
     * //check how 'uber' is used in the lambda expression without being final
     * Uber&lt;String&gt; uber = Uber.fly();
     * () -> {
     * uber.talk("Hello World!");
     * };
     * System.out.println(uber.thanks());
     * // Prints "Hello World!"
     *
     * @param <T> The type of the value.
     * @return The Uber object.
     */
    public static <T> Uber<T> fly() {
        return new Uber<>(null);
    }

    /**
     * Creates a new Uber object with the given value.
     *
     * @param value The default value to be stored.
     */
    public Uber(T value) {
        this.value = value;
    }

    /**
     * Retrieves the value of the Uber object.
     *
     * @return The value.
     */
    public T thanks() {
        return value;
    }

    /**
     * Updates the value of the Uber object.
     *
     * @param value The new value.
     */
    public void talk(T value) {
        this.value = value;
    }
}