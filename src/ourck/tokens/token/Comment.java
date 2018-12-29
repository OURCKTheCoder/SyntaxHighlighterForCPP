package ourck.tokens.token;

/**
 * 注释Token
 * @author ourck
 */
public class Comment extends Token {
	public Comment(int head, int rear){
		super("(COMMENTS)", head, rear);
	}
}