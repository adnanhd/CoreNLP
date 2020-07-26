import java.io.*;
import java.nio.charset.StandardCharsets;

public class formatter {
	public static void main(String[] args) {

		File file = new File("ner");

		File[] files = file.listFiles();

		int i = 0;
		while (i != 80) {
			try {
				String filename = files[i].getName(), csvname = "";
				if (filename.endsWith(".txt"))
					csvname = filename.replace(".txt", ".csv");
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("./csv/" + csvname), StandardCharsets.UTF_8));

				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream("./ner/" + filename), StandardCharsets.UTF_8));
				String line = br.readLine();

				while (line != null) {
					bw.write(i + "," + line + "\n");
					line = br.readLine();
				}

				bw.close();
				br.close();
				i++;
			} catch (IOException e) {
				System.err.println("file not found " + files[i].getName());
				i++;
			}
		}
	}
}
