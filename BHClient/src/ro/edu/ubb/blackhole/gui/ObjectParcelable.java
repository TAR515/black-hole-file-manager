package ro.edu.ubb.blackhole.gui;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Provides possibility to convert an {@link Object} to {@link Parcelable}.
 * 
 * @author Turdean Arnold Robert, created in the company of TBA.
 * @version 1.0
 */
public class ObjectParcelable implements Parcelable {

	/**
	 * This {@link HashMap} contains the informations stored by the {@link Parcelable}.
	 */
	private HashMap<String, Object> map;

	/**
	 * Constructs a new instance.
	 */
	public ObjectParcelable() {
		this.map = new HashMap<String, Object>();
	}

	/**
	 * Constructs a new instance.
	 * 
	 * @param in
	 *            Input {@link Parcelable}.
	 */
	public ObjectParcelable(Parcel in) {
		this.map = new HashMap<String, Object>();
		readFromParcel(in);
	}

	/**
	 * Creates the {@link Parcelable} object
	 */
	public final Parcelable.Creator<Object> CREATOR = new Parcelable.Creator<Object>() {
		public ObjectParcelable[] newArray(int size) {
			return new ObjectParcelable[size];
		}

		public Object createFromParcel(Parcel in) {
			return new ObjectParcelable(in);
		}
	};

	/**
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

	/**
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.map.size());
		for (String s : this.map.keySet()) {
			dest.writeString(s);
			dest.writeValue(this.map.get(s));
		}
	}

	/**
	 * Read the informations from the given {@link Parcel} object.
	 * 
	 * @param in
	 *            Input {@link Parcel} object.
	 */
	public void readFromParcel(Parcel in) {
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			this.map.put(in.readString(), in.readValue(null));
		}
	}

	/**
	 * Returns the information from the {@link Parcelable}.
	 * 
	 * @param key
	 *            Information key.
	 * @return Information
	 */
	public Object get(String key) {
		return this.map.get(key);
	}

	/**
	 * Put the given information to the {@link Parcelable} object.
	 * 
	 * @param key
	 *            Information key
	 * @param value
	 *            Information
	 */
	public void put(String key, Object value) {
		this.map.put(key, value);
	}
}
