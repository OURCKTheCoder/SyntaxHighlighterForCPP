package ourck.mainconsole;

import ourck.tokens.*;
import ourck.textfile.*;
import java.io.*;

public class MainConsole {
	public static void main(String[] args) {
		try {
			FileAsText txtFile = new FileAsText(args[0]); // TODO Path here.
//			FileAsText txtFile = new FileAsText("main.cpp"); // TODO Debug path here.
			
			LTGTMaker lgMgr = new LTGTMaker();
//			NbspMaker nbMgr = new NbspMaker();
			StrManager strMgr = new StrManager();
			ConstManager csMgr = new ConstManager();
			
//			BRMaker brMgr = new BRMaker();
//			TABMaker tbMgr = new TABMaker();
			
			CmtManager cmMgr = new CmtManager();
			PPManager ppMgr = new PPManager();
			KwdManager kwMgr = new KwdManager();
			
			
			// ---------------- The show begins! ----------------
			String[] lines = txtFile.toStrAryByLines();
			StringBuilder newFile = new StringBuilder();
			for(String line : lines) {
				newFile.append(line);
			}
			
			lgMgr.analyze(newFile);
			cmMgr.analyze(newFile); // [!] Analyze in advance for these method:
//			nbMgr.analyze(newFile);
			strMgr.analyze(newFile);
			csMgr.analyze(newFile);
			
//			brMgr.analyze(newFile);
//			tbMgr.analyze(newFile);
			
			cmMgr.analyze(newFile); cmMgr.highlightAll(newFile);
			ppMgr.analyze(newFile);
			kwMgr.analyze(newFile);
			
			char[] formatted = newFile.toString().toCharArray();
			int baseindex = 0;
			for(int i = 0; i < formatted.length; i++) {
				if(formatted[i] == '\n') {
					newFile.insert(i + baseindex, "</pre><pre>");
					baseindex += "</pre><pre>".length();
				}
			}
			
			newFile.insert(0, "<font size=\"4\">\n" + "<pre>\n");
			newFile.append('\n' + "</pre>\n</font>"); // 早知道有pre标签还换个头的格式...
			
			System.out.println("-----------------OUTPUT-----------------");
			System.out.println(newFile);
			System.out.println("--------------=== ENDS ===--------------");
			
			
			BufferedWriter out;
			if(args.length == 2) 
				out = new BufferedWriter(new FileWriter(
						new File(args[1]).getAbsoluteFile()));
			else 
				throw new ArrayIndexOutOfBoundsException("PLZ give valid input path & output path!");
			out.write(newFile.toString());
			out.close();
			
		} catch(NullPointerException e) {
			System.err.println(" [!] Open file failed!");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(" [!] Ary out of bounds! \n" + e.getMessage());
		} catch (IOException e) {
			System.err.println(" [!] Saving failed! ");
		} catch(RuntimeException e) {
			System.err.println(" [!] Runtime failed: " + e.getCause().toString());
		} catch (Exception e) {
			System.err.println(" [!] Program failed!" );
			e.printStackTrace();
			System.err.println("Goodluck next time...");
		} finally {
			System.exit(0);
		}
	}
}
