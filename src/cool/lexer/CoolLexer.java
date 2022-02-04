// Generated from /home/student/Documents/Tema1/src/cool/lexer/CoolLexer.g4 by ANTLR 4.9.1

    package cool.lexer;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, CLASS=2, ELSE=3, FI=4, IF=5, IN=6, INHERITS=7, ISVOID=8, LET=9, 
		LOOP=10, POOL=11, THEN=12, WHILE=13, CASE=14, ESAC=15, NEW=16, OF=17, 
		NOT=18, AT=19, PLUS=20, MINUS=21, MULT=22, DIV=23, ASSIGN=24, LT=25, LE=26, 
		EQ=27, ARROW=28, LPAR=29, RPAR=30, LBRACE=31, RBRACE=32, NEG=33, SEMI=34, 
		COLON=35, COMMA=36, DOT=37, INT=38, BOOL=39, ID=40, TYPE=41, STRING=42, 
		COMMENT=43, BLOCK_COMMENT=44, HANGING_COMMENT=45, WS=46, INVALID_CHARACTER=47;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"CLASS", "ELSE", "FI", "IF", "IN", "INHERITS", "ISVOID", "LET", "LOOP", 
			"POOL", "THEN", "WHILE", "CASE", "ESAC", "NEW", "OF", "NOT", "AT", "PLUS", 
			"MINUS", "MULT", "DIV", "ASSIGN", "LT", "LE", "EQ", "ARROW", "LPAR", 
			"RPAR", "LBRACE", "RBRACE", "NEG", "SEMI", "COLON", "COMMA", "DOT", "DIGIT", 
			"LLETTER", "ULETTER", "NEWLINE", "INT", "BOOL", "ID", "TYPE", "STRING", 
			"COMMENT", "BLOCK_COMMENT", "HANGING_COMMENT", "WS", "INVALID_CHARACTER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'class'", "'else'", "'fi'", "'if'", "'in'", "'inherits'", 
			"'isvoid'", "'let'", "'loop'", "'pool'", "'then'", "'while'", "'case'", 
			"'esac'", "'new'", "'of'", "'not'", "'@'", "'+'", "'-'", "'*'", "'/'", 
			"'<-'", "'<'", "'<='", "'='", "'=>'", "'('", "')'", "'{'", "'}'", "'~'", 
			"';'", "':'", "','", "'.'", null, null, null, null, null, null, null, 
			"'*)'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "CLASS", "ELSE", "FI", "IF", "IN", "INHERITS", "ISVOID", 
			"LET", "LOOP", "POOL", "THEN", "WHILE", "CASE", "ESAC", "NEW", "OF", 
			"NOT", "AT", "PLUS", "MINUS", "MULT", "DIV", "ASSIGN", "LT", "LE", "EQ", 
			"ARROW", "LPAR", "RPAR", "LBRACE", "RBRACE", "NEG", "SEMI", "COLON", 
			"COMMA", "DOT", "INT", "BOOL", "ID", "TYPE", "STRING", "COMMENT", "BLOCK_COMMENT", 
			"HANGING_COMMENT", "WS", "INVALID_CHARACTER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	    
	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 44:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		case 46:
			BLOCK_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 47:
			HANGING_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 49:
			INVALID_CHARACTER_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			        String s = getText();
			        s = s.replace("\\\r\n", "\r\n");
			        s = s.replace("\\n", "\n");
			        s = s.replace("\\t", "\t");
			        s = s.replace("\\b", "\b");
					s = s.replace("\\f", "\f");
					for (int i = s.length() - 1; i  > 0; i--){
						if (s.charAt(i - 1) == '\\' && s.charAt(i) != '\\' && (i < 2 || s.charAt(i - 2) != '\\')) {
							s = new StringBuilder(s).deleteCharAt(i - 1).toString();
						}
					}
					s = s.replace("\\\\",  "\\");
					s = s.substring(1, s.length() - 1);
					setText(s);
			        if (s.length() > 1024) {
			            raiseError("String constant too long");
			        }
			        if (s.contains("\0")) {
			            raiseError("String contains null character");
			        }
			    
			break;
		case 1:
			 raiseError("Unterminated string constant"); 
			break;
		case 2:
			 raiseError("EOF in string constant"); 
			break;
		}
	}
	private void BLOCK_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			 skip(); 
			break;
		case 4:
			 raiseError("EOF in comment"); 
			break;
		}
	}
	private void HANGING_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			 raiseError("Unmatched *)"); 
			break;
		}
	}
	private void INVALID_CHARACTER_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			 raiseError("Invalid character: " + getText()); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\61\u0155\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31"+
		"\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37"+
		"\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\5)"+
		"\u00ea\n)\3)\3)\3*\6*\u00ef\n*\r*\16*\u00f0\3+\3+\3+\3+\3+\3+\3+\3+\3"+
		"+\5+\u00fc\n+\3,\3,\3,\3,\3,\7,\u0103\n,\f,\16,\u0106\13,\3-\3-\3-\3-"+
		"\3-\7-\u010d\n-\f-\16-\u0110\13-\3.\3.\3.\3.\3.\3.\7.\u0118\n.\f.\16."+
		"\u011b\13.\3.\3.\3.\3.\3.\3.\3.\5.\u0124\n.\3/\3/\3/\3/\7/\u012a\n/\f"+
		"/\16/\u012d\13/\3/\3/\5/\u0131\n/\3/\3/\3\60\3\60\3\60\3\60\3\60\7\60"+
		"\u013a\n\60\f\60\16\60\u013d\13\60\3\60\3\60\3\60\3\60\3\60\3\60\5\60"+
		"\u0145\n\60\3\61\3\61\3\61\3\61\3\61\3\62\6\62\u014d\n\62\r\62\16\62\u014e"+
		"\3\62\3\62\3\63\3\63\3\63\5\u0119\u012b\u013b\2\64\3\4\5\5\7\6\t\7\13"+
		"\b\r\t\17\n\21\13\23\f\25\r\27\16\31\17\33\20\35\21\37\22!\23#\24%\25"+
		"\'\26)\27+\30-\31/\32\61\33\63\34\65\35\67\369\37; =!?\"A#C$E%G&I\'K\2"+
		"M\2O\2Q\2S(U)W*Y+[,]-_.a/c\60e\61\3\2\6\3\2\62;\3\2c|\3\2C\\\5\2\13\f"+
		"\16\17\"\"\2\u0166\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E"+
		"\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2"+
		"\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2"+
		"\3g\3\2\2\2\5m\3\2\2\2\7r\3\2\2\2\tu\3\2\2\2\13x\3\2\2\2\r{\3\2\2\2\17"+
		"\u0084\3\2\2\2\21\u008b\3\2\2\2\23\u008f\3\2\2\2\25\u0094\3\2\2\2\27\u0099"+
		"\3\2\2\2\31\u009e\3\2\2\2\33\u00a4\3\2\2\2\35\u00a9\3\2\2\2\37\u00ae\3"+
		"\2\2\2!\u00b2\3\2\2\2#\u00b5\3\2\2\2%\u00b9\3\2\2\2\'\u00bb\3\2\2\2)\u00bd"+
		"\3\2\2\2+\u00bf\3\2\2\2-\u00c1\3\2\2\2/\u00c3\3\2\2\2\61\u00c6\3\2\2\2"+
		"\63\u00c8\3\2\2\2\65\u00cb\3\2\2\2\67\u00cd\3\2\2\29\u00d0\3\2\2\2;\u00d2"+
		"\3\2\2\2=\u00d4\3\2\2\2?\u00d6\3\2\2\2A\u00d8\3\2\2\2C\u00da\3\2\2\2E"+
		"\u00dc\3\2\2\2G\u00de\3\2\2\2I\u00e0\3\2\2\2K\u00e2\3\2\2\2M\u00e4\3\2"+
		"\2\2O\u00e6\3\2\2\2Q\u00e9\3\2\2\2S\u00ee\3\2\2\2U\u00fb\3\2\2\2W\u00fd"+
		"\3\2\2\2Y\u0107\3\2\2\2[\u0111\3\2\2\2]\u0125\3\2\2\2_\u0134\3\2\2\2a"+
		"\u0146\3\2\2\2c\u014c\3\2\2\2e\u0152\3\2\2\2gh\7e\2\2hi\7n\2\2ij\7c\2"+
		"\2jk\7u\2\2kl\7u\2\2l\4\3\2\2\2mn\7g\2\2no\7n\2\2op\7u\2\2pq\7g\2\2q\6"+
		"\3\2\2\2rs\7h\2\2st\7k\2\2t\b\3\2\2\2uv\7k\2\2vw\7h\2\2w\n\3\2\2\2xy\7"+
		"k\2\2yz\7p\2\2z\f\3\2\2\2{|\7k\2\2|}\7p\2\2}~\7j\2\2~\177\7g\2\2\177\u0080"+
		"\7t\2\2\u0080\u0081\7k\2\2\u0081\u0082\7v\2\2\u0082\u0083\7u\2\2\u0083"+
		"\16\3\2\2\2\u0084\u0085\7k\2\2\u0085\u0086\7u\2\2\u0086\u0087\7x\2\2\u0087"+
		"\u0088\7q\2\2\u0088\u0089\7k\2\2\u0089\u008a\7f\2\2\u008a\20\3\2\2\2\u008b"+
		"\u008c\7n\2\2\u008c\u008d\7g\2\2\u008d\u008e\7v\2\2\u008e\22\3\2\2\2\u008f"+
		"\u0090\7n\2\2\u0090\u0091\7q\2\2\u0091\u0092\7q\2\2\u0092\u0093\7r\2\2"+
		"\u0093\24\3\2\2\2\u0094\u0095\7r\2\2\u0095\u0096\7q\2\2\u0096\u0097\7"+
		"q\2\2\u0097\u0098\7n\2\2\u0098\26\3\2\2\2\u0099\u009a\7v\2\2\u009a\u009b"+
		"\7j\2\2\u009b\u009c\7g\2\2\u009c\u009d\7p\2\2\u009d\30\3\2\2\2\u009e\u009f"+
		"\7y\2\2\u009f\u00a0\7j\2\2\u00a0\u00a1\7k\2\2\u00a1\u00a2\7n\2\2\u00a2"+
		"\u00a3\7g\2\2\u00a3\32\3\2\2\2\u00a4\u00a5\7e\2\2\u00a5\u00a6\7c\2\2\u00a6"+
		"\u00a7\7u\2\2\u00a7\u00a8\7g\2\2\u00a8\34\3\2\2\2\u00a9\u00aa\7g\2\2\u00aa"+
		"\u00ab\7u\2\2\u00ab\u00ac\7c\2\2\u00ac\u00ad\7e\2\2\u00ad\36\3\2\2\2\u00ae"+
		"\u00af\7p\2\2\u00af\u00b0\7g\2\2\u00b0\u00b1\7y\2\2\u00b1 \3\2\2\2\u00b2"+
		"\u00b3\7q\2\2\u00b3\u00b4\7h\2\2\u00b4\"\3\2\2\2\u00b5\u00b6\7p\2\2\u00b6"+
		"\u00b7\7q\2\2\u00b7\u00b8\7v\2\2\u00b8$\3\2\2\2\u00b9\u00ba\7B\2\2\u00ba"+
		"&\3\2\2\2\u00bb\u00bc\7-\2\2\u00bc(\3\2\2\2\u00bd\u00be\7/\2\2\u00be*"+
		"\3\2\2\2\u00bf\u00c0\7,\2\2\u00c0,\3\2\2\2\u00c1\u00c2\7\61\2\2\u00c2"+
		".\3\2\2\2\u00c3\u00c4\7>\2\2\u00c4\u00c5\7/\2\2\u00c5\60\3\2\2\2\u00c6"+
		"\u00c7\7>\2\2\u00c7\62\3\2\2\2\u00c8\u00c9\7>\2\2\u00c9\u00ca\7?\2\2\u00ca"+
		"\64\3\2\2\2\u00cb\u00cc\7?\2\2\u00cc\66\3\2\2\2\u00cd\u00ce\7?\2\2\u00ce"+
		"\u00cf\7@\2\2\u00cf8\3\2\2\2\u00d0\u00d1\7*\2\2\u00d1:\3\2\2\2\u00d2\u00d3"+
		"\7+\2\2\u00d3<\3\2\2\2\u00d4\u00d5\7}\2\2\u00d5>\3\2\2\2\u00d6\u00d7\7"+
		"\177\2\2\u00d7@\3\2\2\2\u00d8\u00d9\7\u0080\2\2\u00d9B\3\2\2\2\u00da\u00db"+
		"\7=\2\2\u00dbD\3\2\2\2\u00dc\u00dd\7<\2\2\u00ddF\3\2\2\2\u00de\u00df\7"+
		".\2\2\u00dfH\3\2\2\2\u00e0\u00e1\7\60\2\2\u00e1J\3\2\2\2\u00e2\u00e3\t"+
		"\2\2\2\u00e3L\3\2\2\2\u00e4\u00e5\t\3\2\2\u00e5N\3\2\2\2\u00e6\u00e7\t"+
		"\4\2\2\u00e7P\3\2\2\2\u00e8\u00ea\7\17\2\2\u00e9\u00e8\3\2\2\2\u00e9\u00ea"+
		"\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00ec\7\f\2\2\u00ecR\3\2\2\2\u00ed"+
		"\u00ef\5K&\2\u00ee\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00ee\3\2\2"+
		"\2\u00f0\u00f1\3\2\2\2\u00f1T\3\2\2\2\u00f2\u00f3\7v\2\2\u00f3\u00f4\7"+
		"t\2\2\u00f4\u00f5\7w\2\2\u00f5\u00fc\7g\2\2\u00f6\u00f7\7h\2\2\u00f7\u00f8"+
		"\7c\2\2\u00f8\u00f9\7n\2\2\u00f9\u00fa\7u\2\2\u00fa\u00fc\7g\2\2\u00fb"+
		"\u00f2\3\2\2\2\u00fb\u00f6\3\2\2\2\u00fcV\3\2\2\2\u00fd\u0104\5M\'\2\u00fe"+
		"\u0103\5K&\2\u00ff\u0103\5M\'\2\u0100\u0103\5O(\2\u0101\u0103\7a\2\2\u0102"+
		"\u00fe\3\2\2\2\u0102\u00ff\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0101\3\2"+
		"\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0105\3\2\2\2\u0105"+
		"X\3\2\2\2\u0106\u0104\3\2\2\2\u0107\u010e\5O(\2\u0108\u010d\5K&\2\u0109"+
		"\u010d\5M\'\2\u010a\u010d\5O(\2\u010b\u010d\7a\2\2\u010c\u0108\3\2\2\2"+
		"\u010c\u0109\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\u0110"+
		"\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010fZ\3\2\2\2\u0110"+
		"\u010e\3\2\2\2\u0111\u0119\7$\2\2\u0112\u0113\7^\2\2\u0113\u0118\7$\2"+
		"\2\u0114\u0115\7^\2\2\u0115\u0118\5Q)\2\u0116\u0118\13\2\2\2\u0117\u0112"+
		"\3\2\2\2\u0117\u0114\3\2\2\2\u0117\u0116\3\2\2\2\u0118\u011b\3\2\2\2\u0119"+
		"\u011a\3\2\2\2\u0119\u0117\3\2\2\2\u011a\u0123\3\2\2\2\u011b\u0119\3\2"+
		"\2\2\u011c\u011d\7$\2\2\u011d\u0124\b.\2\2\u011e\u011f\5Q)\2\u011f\u0120"+
		"\b.\3\2\u0120\u0124\3\2\2\2\u0121\u0122\7\2\2\3\u0122\u0124\b.\4\2\u0123"+
		"\u011c\3\2\2\2\u0123\u011e\3\2\2\2\u0123\u0121\3\2\2\2\u0124\\\3\2\2\2"+
		"\u0125\u0126\7/\2\2\u0126\u0127\7/\2\2\u0127\u012b\3\2\2\2\u0128\u012a"+
		"\13\2\2\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u012c\3\2\2\2"+
		"\u012b\u0129\3\2\2\2\u012c\u0130\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u0131"+
		"\5Q)\2\u012f\u0131\7\2\2\3\u0130\u012e\3\2\2\2\u0130\u012f\3\2\2\2\u0131"+
		"\u0132\3\2\2\2\u0132\u0133\b/\5\2\u0133^\3\2\2\2\u0134\u0135\7*\2\2\u0135"+
		"\u0136\7,\2\2\u0136\u013b\3\2\2\2\u0137\u013a\5_\60\2\u0138\u013a\13\2"+
		"\2\2\u0139\u0137\3\2\2\2\u0139\u0138\3\2\2\2\u013a\u013d\3\2\2\2\u013b"+
		"\u013c\3\2\2\2\u013b\u0139\3\2\2\2\u013c\u0144\3\2\2\2\u013d\u013b\3\2"+
		"\2\2\u013e\u013f\7,\2\2\u013f\u0140\7+\2\2\u0140\u0141\3\2\2\2\u0141\u0145"+
		"\b\60\6\2\u0142\u0143\7\2\2\3\u0143\u0145\b\60\7\2\u0144\u013e\3\2\2\2"+
		"\u0144\u0142\3\2\2\2\u0145`\3\2\2\2\u0146\u0147\7,\2\2\u0147\u0148\7+"+
		"\2\2\u0148\u0149\3\2\2\2\u0149\u014a\b\61\b\2\u014ab\3\2\2\2\u014b\u014d"+
		"\t\5\2\2\u014c\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e\u014c\3\2\2\2\u014e"+
		"\u014f\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0151\b\62\5\2\u0151d\3\2\2\2"+
		"\u0152\u0153\13\2\2\2\u0153\u0154\b\63\t\2\u0154f\3\2\2\2\23\2\u00e9\u00f0"+
		"\u00fb\u0102\u0104\u010c\u010e\u0117\u0119\u0123\u012b\u0130\u0139\u013b"+
		"\u0144\u014e\n\3.\2\3.\3\3.\4\b\2\2\3\60\5\3\60\6\3\61\7\3\63\b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}