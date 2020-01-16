package egrep;
/**
 * A node that hold a value.
 * The value is the association of the key.
 * 
 *
 * @param <A>
 */
public class WithValue<A> extends Node<A> {
	
	A val;
	
	public WithValue(A val) {
		super();
		this.val = val;
	}
}
