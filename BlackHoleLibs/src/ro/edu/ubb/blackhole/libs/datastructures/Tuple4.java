package ro.edu.ubb.blackhole.libs.datastructures;

/**
 * Container object for containing four any {@link Object}-s.
 * @author Turdean Arnold Robert
 *
 * @version 1.0
 * @param <T> Any {@link Object}.
 * @param <U> Any {@link Object}.
 * @param <V> Any {@link Object}.
 * @param <Z> Any {@link Object}.
 */
public class Tuple4 <T, U, V, Z>{
	public final T t;
	public final U u;
	public final V v;
	public final Z z;

	public Tuple4(T t, U u, V v, Z z) {
		this.t = t;
		this.u = u;
		this.v = v;
		this.z = z;
	}
}
