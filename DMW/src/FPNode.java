import java.util.ArrayList;
import java.util.List;

public class FPNode {
	String itemName;
	int count;
	List<FPNode> children;
	FPNode nodeLink, parent;

	FPNode(String itemName) {
		this.itemName = itemName;
		nodeLink = null;
		parent = null;
		children = new ArrayList<FPNode>();
	}

	@Override
	public String toString() {
		return "{" + itemName + ": " + count + " -> " + children.size() + " }";
	}
}