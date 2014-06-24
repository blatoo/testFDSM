package util;

import javolution.util.FastBitSet;

/**
 * @author Conny Gu This Class ist extends from Class BitSet, it won't change
 *         itself while it does some operations.
 * 
 */

public class MyFastBitSet extends FastBitSet {

	public MyFastBitSet() {
		super();
	}

	public MyFastBitSet(FastBitSet b) {
		super();
		this.addAll(b);

	}

	
	public MyFastBitSet myand(MyFastBitSet b) {

		MyFastBitSet a = new MyFastBitSet();

		a.addAll(this);

		a.and(b);

		return a;

	}

	public MyFastBitSet myor(MyFastBitSet b) {

		MyFastBitSet a = new MyFastBitSet();

		a.addAll(this);

		a.or(b);

		return a;

	}

	public MyFastBitSet myandNot(MyFastBitSet b) {

		MyFastBitSet a = new MyFastBitSet();

		a.addAll(this);

		a.andNot(b);

		return a;

	}

}
