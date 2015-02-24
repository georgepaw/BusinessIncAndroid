package company.businessinc.bathtouch.data;

/**
 * Created by Grzegorz on 18/02/2015.
 */
public class Tuple<L,R> {
    private final L left;
    private final R right;

    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }
}
