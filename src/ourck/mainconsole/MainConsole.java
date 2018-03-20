package ourck.mainconsole;

import ourck.tokens.*;
import ourck.textfile.*;

public class MainConsole {
	public static void main(String[] args) {
		try {
//			FileAsText txtFile = new FileAsText(args[0]); // TODO Path here.
			FileAsText txtFile = new FileAsText("main.cpp"); // TODO Debug path here.
			
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
			StringBuilder newToken = new StringBuilder();
			for(String line : lines) {
				newToken.append(line);
			}
			
			lgMgr.analyze(newToken);
			cmMgr.analyze(newToken); // [!] Analyze in advance for these method:
//			nbMgr.analyze(newToken);
			strMgr.analyze(newToken);
			csMgr.analyze(newToken);
			
//			brMgr.analyze(newToken);
//			tbMgr.analyze(newToken);
			
			cmMgr.analyze(newToken); cmMgr.highlightAll(newToken);
			ppMgr.analyze(newToken);
			kwMgr.analyze(newToken);
			
			char[] formatted = newToken.toString().toCharArray();
			int baseindex = 0;
			for(int i = 0; i < formatted.length; i++) {
				if(formatted[i] == '\n') {
					newToken.insert(i + baseindex, "</pre><pre>");
					baseindex += "</pre><pre>".length();
				}
			}
			
			newToken.insert(0, "<font size=\"4\">\n" + "<pre>\n");
			newToken.append('\n' + "</pre>\n</font>"); // 早知道有pre标签还换个头的格式...
			
			System.out.println(newToken);
			
		} catch(NullPointerException e) {
			System.err.println(" [!] Open file failed!");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(" [!] PLZ give a valid path!");
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
