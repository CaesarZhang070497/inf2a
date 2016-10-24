// File:   MH_Lexer.java
// Date:   October 2013, modified October 2016.

// Java template file for lexer component of Informatics 2A Assignment 1.
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.Reader;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

    static class VarAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "VAR";
        }

        @Override
        public int numberOfStates() {
            return 3;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return CharTypes.isSmall(input) ? 1 : numberOfStates() - 1;
                case 1:
                    return CharTypes.isSmall(input) || CharTypes.isLarge(input) || CharTypes.isDigit(
                            input) || input == '\'' ? 1 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;

            }
        }

        @Override
        boolean accepting(int state) {
            return state == 1;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class NumAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "NUM";
        }

        @Override
        public int numberOfStates() {
            return 4;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return input == '0' ? 1 : CharTypes.isDigit(input) ? 2 : numberOfStates() - 1;
                case 1:
                    return numberOfStates() - 1;
                case 2:
                    return CharTypes.isDigit(input) ? 2 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;
            }
        }

        @Override
        boolean accepting(int state) {
            return state == 1 || state == 2;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class BooleanAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "BOOLEAN";
        }

        @Override
        public int numberOfStates() {
            return 10;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return input == 'T' ? 1 : input == 'F' ? 4 : numberOfStates() - 1;
                case 1:
                    return input == 'r' ? 2 : numberOfStates() - 1;
                case 2:
                    return input == 'u' ? 3 : numberOfStates() - 1;
                case 3:
                    return input == 'e' ? 8 : numberOfStates() - 1;
                case 4:
                    return input == 'a' ? 5 : numberOfStates() - 1;
                case 5:
                    return input == 'l' ? 6 : numberOfStates() - 1;
                case 6:
                    return input == 's' ? 7 : numberOfStates() - 1;
                case 7:
                    return input == 'e' ? 8 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;
            }
        }

        @Override
        boolean accepting(int state) {
            return state == 8;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class SymAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "SYM";
        }

        @Override
        public int numberOfStates() {
            return 3;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return CharTypes.isSymbolic(input) ? 1 : numberOfStates() - 1;
                case 1:
                    return CharTypes.isSymbolic(input) ? 1 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;
            }
        }

        @Override
        boolean accepting(int state) {
            return state == 1;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class WhitespaceAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "";
        }

        @Override
        public int numberOfStates() {
            return 3;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return CharTypes.isWhitespace(input) ? 1 : numberOfStates() - 1;
                case 1:
                    return CharTypes.isWhitespace(input) ? 1 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;
            }
        }

        @Override
        boolean accepting(int state) {
            return state == 1;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class CommentAcceptor extends GenAcceptor implements DFA {
        @Override
        public String lexClass() {
            return "";
        }

        @Override
        public int numberOfStates() {
            return 6;
        }

        @Override
        int nextState(int state, char input) {
            switch (state) {
                case 0:
                    return input == '-' ? 1 : numberOfStates() - 1;
                case 1:
                    return input == '-' ? 2 : numberOfStates() - 1;
                case 2:
                    return input == '-' ? 2 : 3;
                case 3:
                    return !(CharTypes.isSymbolic(input) || CharTypes.isNewline(input)) ? 4 : numberOfStates() - 1;
                case 4:
                    return !CharTypes.isNewline(input) ? 4 : numberOfStates() - 1;
                default:
                    return numberOfStates() - 1;
            }
        }

        @Override
        boolean accepting(int state) {
            return state == 2 || state == 3 || state == 4;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }
    }

    static class TokAcceptor extends GenAcceptor implements DFA {

        String tok;
        int tokLen;

        TokAcceptor(String tok) {
            this.tok = tok;
            tokLen = tok.length();
        }

        @Override
        public String lexClass() {
            return tok;
        }

        @Override
        public int numberOfStates() {
            return tokLen + 2;
        }

        @Override
        int nextState(int state, char input) {
            return (state < tokLen) && (tok.charAt(state) == input) ? state + 1 : numberOfStates() - 1;
        }

        @Override
        boolean accepting(int state) {
            return state == tokLen;
        }

        @Override
        int deadState() {
            return numberOfStates() - 1;
        }

    }

    // add definition of MH_acceptors here
    private static final DFA varAcceptor;
    private static final DFA numAcceptor;
    private static final DFA booleanAcceptor;
    private static final DFA symAcceptor;
    private static final DFA whitespaceAcceptor;
    private static final DFA commentAcceptor;
    private static final DFA integerKeywordAcceptor;
    private static final DFA boolKeywordAcceptor;
    private static final DFA ifKeywordAcceptor;
    private static final DFA thenKeywordAcceptor;
    private static final DFA elseKeywordAcceptor;
    private static final DFA leftBracketKeywordAcceptor;
    private static final DFA rightBracketKeywordAcceptor;
    private static final DFA semicolonKeywordAcceptor;
    private static final DFA[] MH_acceptors;

    static {
        varAcceptor = new VarAcceptor();
        numAcceptor = new NumAcceptor();
        booleanAcceptor = new BooleanAcceptor();
        symAcceptor = new SymAcceptor();
        whitespaceAcceptor = new WhitespaceAcceptor();
        commentAcceptor = new CommentAcceptor();
        integerKeywordAcceptor = new TokAcceptor("Integer");
        boolKeywordAcceptor = new TokAcceptor("Bool");
        ifKeywordAcceptor = new TokAcceptor("if");
        thenKeywordAcceptor = new TokAcceptor("then");
        elseKeywordAcceptor = new TokAcceptor("else");
        leftBracketKeywordAcceptor = new TokAcceptor("(");
        rightBracketKeywordAcceptor = new TokAcceptor(")");
        semicolonKeywordAcceptor = new TokAcceptor(";");
        MH_acceptors = new DFA[]{integerKeywordAcceptor, boolKeywordAcceptor, ifKeywordAcceptor, thenKeywordAcceptor,
                elseKeywordAcceptor, leftBracketKeywordAcceptor, rightBracketKeywordAcceptor, semicolonKeywordAcceptor,
                booleanAcceptor, numAcceptor, varAcceptor, commentAcceptor, symAcceptor, whitespaceAcceptor};
    }

    MH_Lexer(Reader reader) {
        super(reader, MH_acceptors);
    }

}
