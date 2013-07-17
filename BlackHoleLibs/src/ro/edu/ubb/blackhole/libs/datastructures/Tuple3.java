package ro.edu.ubb.blackhole.libs.datastructures;

/**
 * Container object for containing three any {@link Object}-s.
 * @author Turdean Arnold Robert
 *
 * @version 1.0
 * @param <T> Any {@link Object}.
 * @param <U> Any {@link Object}.
 * @param <V> Any {@link Object}.
 */
public class Tuple3 <T, U, V> {
	public final T t;
	public final U u;
	public final V v;

	public Tuple3(T t, U u, V v) {
		this.t = t;
		this.u = u;
		this.v = v;
	}
}
