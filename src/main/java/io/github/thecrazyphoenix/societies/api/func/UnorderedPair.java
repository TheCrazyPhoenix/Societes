package io.github.thecrazyphoenix.societies.api.func;

/**
 * An order-independent pair.
 * In other words, (a; b) == (b; a)
 * @param <L> The type of the left value.
 * @param <R> The type of the right value.
 */
public interface UnorderedPair<L, R> {
    /**
     * Retrieves the left object of this pair.
     * @return The left object.
     */
    L getLeft();

    /**
     * Retrieves the right object of this pair.
     * @return The right object.
     */
    R getRight();

    /**
     * Returns the hash code value for this unordered pair.  The hash code of an unordered pair <tt>p</tt> is defined to be: <pre>
     *     (p.getLeft()==null  ? 0 : p.getLeft().hashCode()) ^
     *     (p.getRight()==null ? 0 : p.getRight().hashCode()) *
     *     (p.getRight()==null ? 0 : p.getRight().hashCode()) ^
     *     (p.getLeft()==null  ? 0 : p.getLeft().hashCode)
     * </pre>
     * This ensures that <tt>p1.equals(p2)</tt> implies that
     * <tt>p1.hashCode()==p2.hashCode()</tt> for any two pairs
     * <tt>p1</tt> and <tt>p2</tt>, as required by the general
     * contract of <tt>Object.hashCode</tt>.
     *
     * @return The hash code value for this pair
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    int hashCode();

    /**
     * Checks whether the given object is equal to this one.
     * (a; b) and (b; a) must be treated as equal
     * @param other The object to compare with this one.
     * @return True if equal, false otherwise.
     * @see Object#equals(Object)
     */
    @Override
    boolean equals(Object other);
}
