import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Transaction {
	List<Item> items = new ArrayList<Item>();

	public Transaction(List<String> items, Map<String, Integer> frequentItems) {
		for (String item : items) {
			if (frequentItems.containsKey(item)) {
				this.items.add(new Item(item, frequentItems.get(item)));
			}
		}
		this.items.sort(new Comparator<Item>() {
			@Override
			public int compare(Item lhs, Item rhs) {
				return lhs.support > rhs.support ? -1 : (lhs.support < rhs.support) ? 1 : 0;
			}
		});
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
