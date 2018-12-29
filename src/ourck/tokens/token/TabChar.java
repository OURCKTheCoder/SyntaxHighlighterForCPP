package ourck.tokens.token;

/**
 * 制表符Token
 * @author ourck
 */
public class TabChar extends Token {
	public TabChar(int pos) {
		super("(TABCHAR)", pos, pos);
	}
}