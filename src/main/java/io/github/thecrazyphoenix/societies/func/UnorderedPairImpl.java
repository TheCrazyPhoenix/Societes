package io.github.thecrazyphoenix.societies.func;

import io.github.thecrazyphoenix.societies.api.func.UnorderedPair;

public class UnorderedPairImpl<L, R> implements UnorderedPair<L, R> {
    private L left;
    private R right;

    public UnorderedPairImpl(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        int lhc = left.hashCode();
        int rhc = right.hashCode();
        return lhc ^ rhc * rhc ^ lhc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof UnorderedPair)) {
            return false;
        }
        UnorderedPair other = (UnorderedPair) obj;
        return left.equals(other.getLeft()) && right.equals(other.getRight()) || left.equals(other.getRight()) && right.equals(other.getLeft());
    }
}
