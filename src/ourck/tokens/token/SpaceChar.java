package ourck.tokens.token;

/**
 * 空格符Token
 * @author ourck
 */
public class SpaceChar extends Token {
	public SpaceChar(int pos) {
		super("(_)", pos, pos);
	}
}
