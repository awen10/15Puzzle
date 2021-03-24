import java.util.Comparator;

/**
 *
 * @author Heisenberg
 */
public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.getPriorityFunction() < node2.getPriorityFunction()) {
            return -1;
        } else if (node1.getPriorityFunction() > node2.getPriorityFunction()) {
            return 1;
        } else {
            return 0;
        }
    }
}