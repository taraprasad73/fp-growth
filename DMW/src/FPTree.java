import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FPTree {
	int minSupCount;
	FPNode root = new FPNode("ROOT");
	Map<String, Integer> supportCount, frequentItems;
	Map<String, FPNode> headerTable = new HashMap<>();
	Map<String, FPNode> currentLinkPosition = new HashMap<>();

	public FPTree(List<List<String>> db, int minSupCount) {
		//		System.out.println(db);
		this.minSupCount = minSupCount;
		calculateSupport(db);
		//		System.out.println(supportCount);
		getFrequentItems();
		//		System.out.println(frequentItems);
		buildHeaderTable();
		buildTree(db);
		//		printHeaderTable();
		//		printTree(root);
		//		System.out.println();
	}

	private void buildTree(List<List<String>> db) {
		for (List<String> items : db) {
			Transaction transaction = new Transaction(items, frequentItems);
//			System.out.println(transaction);
			insertIntoTree(transaction, root);
		}
	}

	private void buildHeaderTable() {
		for (String freqItem : frequentItems.keySet()) {
			headerTable.put(freqItem, new FPNode(freqItem)); // the head node for each item is a dummy (sentinel) node
			currentLinkPosition.put(freqItem, headerTable.get(freqItem)); // keeps track of the last occurrence of the item in the tree
		}
	}

	/*
	 * Complexity: O(frequent items in Transaction)
	 */
	private void insertIntoTree(Transaction transaction, FPNode node) {
		if (transaction.items.size() == 0)
			return;
		Item p = transaction.items.remove(0);

		// Search if the item exists in the child nodes. Use hashing.
		FPNode nextNode = null;
		for (FPNode child : node.children) {
			if (child.itemName.equals(p.name)) {
				child.count++;
				nextNode = child;
				break;
			}
		}
		if (nextNode == null) {
			nextNode = new FPNode(p.name);
			nextNode.count = 1;
			nextNode.parent = node;
			currentLinkPosition.get(p.name).nodeLink = nextNode;
			currentLinkPosition.put(p.name, nextNode);
			node.children.add(nextNode);
		}
		insertIntoTree(transaction, nextNode);
	}

	void printLinks(FPNode head) {
		if (head == null)
			return;
		System.out.print(head + " ");
		printLinks(head.nodeLink);
	}

	private void getFrequentItems() {
		frequentItems = new HashMap<String, Integer>();
		supportCount.forEach((item, support) -> {
			if (support >= minSupCount)
				frequentItems.put(item, support);
		});
	}

	private void calculateSupport(List<List<String>> db) {
		supportCount = new HashMap<String, Integer>(); // frequency
		for (List<String> transaction : db) {
			for (String item : transaction) {
				Integer count = supportCount.get(item);
				if (count == null)
					supportCount.put(item, 1);
				else
					supportCount.put(item, count + 1);
			}
		}
		//System.out.println(supportCount);
	}

	private void printTree(FPNode root) {
		System.out.print(root + " ");
		if (root.children.size() == 0) {
			System.out.print("| ");
			return;
		}
		for (FPNode child : root.children)
			printTree(child);
	}

	private void printHeaderTable() {
		System.out.println("Header Table\n-------------------------------------------------------\n");
		for (String item : headerTable.keySet()) {
			printLinks(headerTable.get(item));
			System.out.println();
		}
		System.out.println("-------------------------------------------------------\n");
	}
}