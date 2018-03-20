package ourck.tokens;

public abstract class TokenManager {
	public abstract void add(Token t); // Add a token into list.
	public abstract boolean cmp(String t);// Compare a token with the list.
	public abstract StringBuilder analyze(StringBuilder token);

//	public void addHTMLTagBlock(Token t) {
//		HTMLTagList.add(t);
//	}
//	public boolean isPtrInsideHTMLTagBlock(int index) {
//		for(Token tag : HTMLTagList) {
//			if(index >= tag.getIndex() && index <= tag.getRear()) {
//				return true;
//			}
//		}
//		return false;
//	}
//	public void onUpdateHTMLTagBlockPos(Integer deltaPos) {
//		for(Token tag : HTMLTagList) {
//			tag.setIndex(tag.getIndex() + deltaPos);
//			tag.setRear(tag.getRear() + deltaPos);
//		}
//	}
}
