
public class Item {
	String name;
	int support;

	Item(String name, int support) {
		this.name = name;
		this.support = support;
	}

	@Override
	public String toString() {
		return "{" + name + ": " + support + "}";
	}
}