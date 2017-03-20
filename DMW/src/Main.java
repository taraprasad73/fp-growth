import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	private static List<List<String>> readData(String fileName) throws IOException {
		List<List<String>> db = new ArrayList<List<String>>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			String line = br.readLine();
			while (line != null) {
				db.add(Arrays.asList(line.split(" ")));
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return db;
	}

	public static void main(String[] args) {
		int minSupportCount = 3;
		try {
			List<List<String>> database = readData("data/mushroom.dat");
			FPTree fpTree = new FPTree(database, minSupportCount); // database compressed into a tree
			System.out.println("FP Tree created. Starting FPGeneration...");
			FPGenerator fpGen = new FPGenerator(fpTree, minSupportCount);
			for (Pattern freqPattern : fpGen.getPatterns()) {
				System.out.println(freqPattern);
			}
		} catch (IOException e) {
			System.err.println("IOException");
		}
	}

}
