package ourck.tokens.manager;

import java.util.LinkedList;

import ourck.tokens.token.*;

/**
 * 左右尖括号管理器。<br>
 * 在HTML中，字符'<'及'>'需要做特殊转义处理。例如#include语句后跟的标准库名。<br>
 * @author ourck
 */
public class LTGTMaker extends TokenManager {
	
	private final String LTCHAR = "&lt;";
	private final String GTCHAR = "&gt;";
	private LinkedList<LTGTChar> LTCharList = new LinkedList<LTGTChar>();
	
	@Override
	public StringBuilder analyze(StringBuilder txt) {
		// 1.Record position.
		char[] charAry = txt.toString().toCharArray();
		LinkedList<Character> chars = new LinkedList<Character>();
		for(char c : charAry) {
			chars.add(c);
		}
		for(int i = 0; i < chars.size(); i++) {
			if(chars.get(i).equals('<')) {
				LTCharList.add(new LTChar(i)); // Backward
			}
			if(chars.get(i).equals('>')) {
				LTCharList.add(new GTChar(i)); // Backward
			}
		}
		
		// 2.
		int baseIndex = 0;
		for(LTGTChar t : LTCharList) {
			int pos = t.getIndex();
			if(t.getStr().equals("lt")) {
				txt.insert(pos + baseIndex, LTCHAR);
				
				for(Token cmtBlock : CmtManager.getCmtList()) {
					if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
						cmtBlock.setIndex(cmtBlock.getIndex() + LTCHAR.length() - 1);
						cmtBlock.setRear(cmtBlock.getRear() + LTCHAR.length() - 1);
					}
				}
				
				baseIndex += LTCHAR.length();
			}
			else {
				txt.insert(pos + baseIndex, GTCHAR);
				
				for(Token cmtBlock : CmtManager.getCmtList()) {
					if(CmtManager.ptrPosByBlock(cmtBlock, pos + baseIndex) == -1) {
						// 减一是因为原先的大于/小于符号被删掉了
						cmtBlock.setIndex(cmtBlock.getIndex() + LTCHAR.length() - 1);
						cmtBlock.setRear(cmtBlock.getRear() + LTCHAR.length() - 1);
					}
				}
				
				baseIndex += GTCHAR.length();
			}
			txt.deleteCharAt(pos + baseIndex);
			baseIndex--;
		}
		
		doNext(txt);

		return txt;
	}

	@Override
	public void reset() { LTCharList.clear(); }
}
