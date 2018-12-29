package ourck.tokens.token;

/**
 * 左右尖括号Token抽象类
 * @author ourck
 */
public abstract class LTGTChar extends Token {
	public LTGTChar(String lt_gt, int pos){
		super(lt_gt, pos, pos);
	}
}


