package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Turtle.Function;


public class Parser {

	public enum TokenKind { tkAtomic, tkBeginLoop, tkEnd, tkBeginFunction, tkCallFunction };
	
	public class AtomicAction {
		public Function function_;
		public double value_;
		
		public AtomicAction(Function kind, double value) {
			function_ = kind;
			value_ = value;
		}
		
		public String toString() {
			return "[" + function_ + "," + value_ + "]";
		}
	}
	
	public class Token {
		String name_;
		TokenKind kind_;
		Function value_;
		
		public Token(String name, Function value, TokenKind kind) {
			name_ = name;
			value_ = value;
			kind_ = kind;
		}
	}
	
	List<Token> tokens_ = new ArrayList<Parser.Token>();
	Map<String,List<AtomicAction>> functions = new HashMap<String, List<AtomicAction>>();
	
	public void addToken(String name, Function value, TokenKind kind) {
		tokens_.add(new Token(name.toUpperCase(), value, kind));
	}

	public List<AtomicAction> parse(String[] inputLines) {
		List<String> list = new ArrayList<String>();
		for (String s : inputLines)
			list.add(s);
		return parse(list);
	}
	
	public List<AtomicAction> parse(List<String> inputLines) {
		List<AtomicAction> result = new ArrayList<AtomicAction>();
		
		while (inputLines.size() > 0) {
			String[] words = inputLines.remove(0).split(" ");
			for (Token token : tokens_) {
				if (words[0].toUpperCase().compareTo(token.name_) == 0) {
					if (token.kind_ == TokenKind.tkAtomic) {
						result.add(parseAction(token,words));
						break;
					}
					else if (token.kind_ == TokenKind.tkBeginLoop) {
						List<AtomicAction> partialResult = parse(inputLines);
						for (int i = 0; i < Integer.parseInt(words[1]); ++i) {
							result.addAll(partialResult);						
						}
						break;
					}
					else if (token.kind_ == TokenKind.tkBeginFunction) {
						functions.put(words[1], parse(inputLines));
						break;
					}
					else if (token.kind_ == TokenKind.tkEnd) {
						return result;
					}
					else if (token.kind_ == TokenKind.tkCallFunction) {
						result.addAll(functions.get(words[1]));
						break;
					}
				}
			}
		}
		return result;
	}
	
	private AtomicAction parseAction(Token token, String[] words) {
		if (words.length > 1) {
			return new AtomicAction(token.value_,Double.parseDouble(words[1]));
		}
		return new AtomicAction(token.value_,-1);
	}
}
