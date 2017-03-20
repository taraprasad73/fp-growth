import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FPGenerator {
	int minSupCount;
	List<Pattern> patterns = new ArrayList<Pattern>();

	List<Pattern> getPatterns() {
		patterns.sort(new Comparator<Pattern>() {
			@Override
			public int compare(Pattern lhs, Pattern rhs) {
				return lhs.support > rhs.support ? -1 : (lhs.support < rhs.support) ? 1 : 0;
			}
		});
		return patterns;
	}

	public FPGenerator(FPTree fpTree, int minSupportCount) {
		this.minSupCount = minSupportCount;
		fpGrowth(fpTree, null);
	}

	private void fpGrowth(FPTree fpTree, List<String> suffix) {
		if (fpTree.root.children.size() == 0)
			return;

		for (String freqItem : fpTree.frequentItems.keySet()) {
			List<String> newSuffix = new ArrayList<String>();
			newSuffix.add(freqItem);
			if (suffix != null)
				for (String item : suffix)
					newSuffix.add(item);
			patterns.add(new Pattern(newSuffix, fpTree.frequentItems.get(freqItem))); // suffix of the item unique to this fpTree passed
			//			System.out.println(patterns);
			List<List<String>> conditionalDatabase = generateConditionalDatabase(fpTree, freqItem);
			FPTree conditionalFPTree = new FPTree(conditionalDatabase, minSupCount);
			System.out.println("FPTree created.");
			fpGrowth(conditionalFPTree, newSuffix);
		}
	}

	private List<List<String>> generateConditionalDatabase(FPTree fpTree, String freqItem) {
		List<List<String>> conditionalDatabase = new ArrayList<List<String>>();
		FPNode headNode = fpTree.headerTable.get(freqItem);
		// head is a sentinel, start with the next node
		for (FPNode alphaNode = headNode.nodeLink; alphaNode != null; alphaNode = alphaNode.nodeLink) {
			List<String> transformedPrefixPath = new ArrayList<String>(); // Use parent pointers to reach till the root, don't include the root and the tempNode
			for (FPNode pathNode = alphaNode.parent; pathNode.parent != null; pathNode = pathNode.parent) {
				transformedPrefixPath.add(pathNode.itemName);
			}
			// Insert this transaction tempNode,count times into the database
			for (int i = 0; i < alphaNode.count; i++) {
				conditionalDatabase.add(transformedPrefixPath);
			}
		}
		return conditionalDatabase;
	}
}

class Pattern {
	List<String> items; // should be a set
	int support;

	public Pattern(List<String> items, int support) {
		this.items = items;
		this.support = support;
	}

	@Override
	public String toString() {
		return items.toString() + ": " + support;
	}
}