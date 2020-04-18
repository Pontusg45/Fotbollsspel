package view;

/**
 * @author Pontus Gustafsson
 */
public class GameCloseException extends Exception{
	private static final long serialVersionUID = -1822574920170126156L;

	public GameCloseException(String message) {
		super(message);
	}
}