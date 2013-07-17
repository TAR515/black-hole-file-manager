package ro.edu.ubb.blackhole.libs.datastructures;

/**
 * Container object for containing two any {@link Object}-s.
 * @author Turdean Arnold Robert
 *
 * @version 1.0
 * @param <T> Any {@link Object}.
 * @param <U> Any {@link Object}.
 */
public class Tuple2<T, U> {
	public final T t;
	public final U u;

	public Tuple2(T t, U u) {
		this.t = t;
		this.u = u;
	}
}
