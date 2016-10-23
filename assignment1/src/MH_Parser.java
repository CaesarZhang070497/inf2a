// File:   MH_Parser.java
// Date:   October 2013, modified October 2016.

// Java template file for parser component of Informatics 2A Assignment 1.
// Students should add a method body for tableEntry, implementing the LL(1) parse table for Micro-Haskell.


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

class MH_Parser extends GenParser implements PARSER {

    String startSymbol() {
        return "#Prog";
    }

    // Right hand sides of all productions in grammar:

    static String[] epsilon = new String[]{};
    static String[] Decl_Prog = new String[]{"#Decl", "#Prog"};
    static String[] TypeDecl_TermDecl = new String[]{"#TypeDecl", "#TermDecl"};
    static String[] VAR_has_Type = new String[]{"VAR", "::", "#Type", ";"};
    static String[] Type1_TypeOps = new String[]{"#Type1", "#TypeOps"};
    static String[] arrow_Type = new String[]{"->", "#Type"};
    static String[] Integer = new String[]{"Integer"};
    static String[] Bool = new String[]{"Bool"};
    static String[] lbr_Type_rbr = new String[]{"(", "#Type", ")"};
    static String[] VAR_Args_eq_Exp = new String[]{"VAR", "#Args", "=", "#Exp", ";"};
    static String[] VAR_Args = new String[]{"VAR", "#Args"};
    static String[] Exp1 = new String[]{"#Exp1"};
    static String[] if_then_else = new String[]{"if", "#Exp", "then", "#Exp", "else", "#Exp"};
    static String[] Exp2_Op1 = new String[]{"#Exp2", "#Op1"};
    static String[] eqeq_Exp2 = new String[]{"==", "#Exp2"};
    static String[] lteq_Exp2 = new String[]{"<=", "#Exp2"};
    static String[] Exp3_Ops2 = new String[]{"#Exp3", "#Ops2"};
    static String[] plus_Exp3_Ops2 = new String[]{"+", "#Exp3", "#Ops2"};
    static String[] minus_Exp3_Ops2 = new String[]{"-", "#Exp3", "#Ops2"};
    static String[] Exp4_Ops3 = new String[]{"#Exp4", "#Ops3"};
    static String[] VAR = new String[]{"VAR"};
    static String[] NUM = new String[]{"NUM"};
    static String[] BOOLEAN = new String[]{"BOOLEAN"};
    static String[] lbr_Exp_rbr = new String[]{"(", "#Exp", ")"};

    static Map<String, Map<String, String[]>> tableMap;

    static {
        // Class initialization of LL(1) parse table
        tableMap = new HashMap<>();
        Map<String, String[]> hm;
        // #Prog
        hm = new HashMap<>();
        tableMap.put("#Prog", hm);
        hm.put("VAR", Decl_Prog);
        hm.put(null, epsilon);

        // #Decl
        hm = new HashMap<>();
        tableMap.put("#Decl", hm);
        hm.put("VAR", TypeDecl_TermDecl);

        // #TypeDecl
        hm = new HashMap<>();
        tableMap.put("#TypeDecl", hm);
        hm.put("VAR", VAR_has_Type);

        // #Type
        hm = new HashMap<>();
        tableMap.put("#Type", hm);
        hm.put("Integer", Type1_TypeOps);
        hm.put("Bool", Type1_TypeOps);
        hm.put("(", Type1_TypeOps);

        // #TypeOps
        hm = new HashMap<>();
        tableMap.put("#TypeOps", hm);
        hm.put("->", arrow_Type);
        hm.put(";", epsilon);
        hm.put(")", epsilon);

        // #Type1
        hm = new HashMap<>();
        tableMap.put("#Type1", hm);
        hm.put("Integer", Integer);
        hm.put("Bool", Bool);
        hm.put("(", lbr_Type_rbr);

        // #TermDecl
        hm = new HashMap<>();
        tableMap.put("#TermDecl", hm);
        hm.put("VAR", VAR_Args_eq_Exp);

        // #Args
        hm = new HashMap<>();
        tableMap.put("#Args", hm);
        hm.put("VAR", VAR_Args);
        hm.put("=", epsilon);

        // #Exp
        hm = new HashMap<>();
        tableMap.put("#Exp", hm);
        hm.put("if", if_then_else);
        hm.put("(", Exp1);
        hm.put("BOOLEAN", Exp1);
        hm.put("NUM", Exp1);
        hm.put("VAR", Exp1);


        // #Exp1
        hm = new HashMap<>();
        tableMap.put("#Exp1", hm);
        hm.put("(", Exp2_Op1);
        hm.put("BOOLEAN", Exp2_Op1);
        hm.put("NUM", Exp2_Op1);
        hm.put("VAR", Exp2_Op1);

        // #Op1
        hm = new HashMap<>();
        tableMap.put("#Op1", hm);
        hm.put("==", eqeq_Exp2);
        hm.put("<=", lteq_Exp2);
        hm.put(";", epsilon);
        hm.put(")", epsilon);
        hm.put("then", epsilon);
        hm.put("else", epsilon);

        // #Exp2
        hm = new HashMap<>();
        tableMap.put("#Exp2", hm);
        hm.put("(", Exp3_Ops2);
        hm.put("BOOLEAN", Exp3_Ops2);
        hm.put("NUM", Exp3_Ops2);
        hm.put("VAR", Exp3_Ops2);

        // #Ops2
        hm = new HashMap<>();
        tableMap.put("#Ops2", hm);
        hm.put("+", plus_Exp3_Ops2);
        hm.put("-", minus_Exp3_Ops2);
        hm.put(";", epsilon);
        hm.put(")", epsilon);
        hm.put("then", epsilon);
        hm.put("else", epsilon);
        hm.put("==", epsilon);
        hm.put("<=", epsilon);

        // #Exp3
        hm = new HashMap<>();
        tableMap.put("#Exp3", hm);
        hm.put("(", Exp4_Ops3);
        hm.put("BOOLEAN", Exp4_Ops3);
        hm.put("NUM", Exp4_Ops3);
        hm.put("VAR", Exp4_Ops3);

        // #Ops3
        hm = new HashMap<>();
        tableMap.put("#Ops3", hm);
        hm.put("(", Exp4_Ops3);
        hm.put("BOOLEAN", Exp4_Ops3);
        hm.put("NUM", Exp4_Ops3);
        hm.put("VAR", Exp4_Ops3);
        hm.put(";", epsilon);
        hm.put(")", epsilon);
        hm.put("then", epsilon);
        hm.put("else", epsilon);
        hm.put("==", epsilon);
        hm.put("<=", epsilon);
        hm.put("+", epsilon);
        hm.put("-", epsilon);

        // #Exp4
        hm = new HashMap<>();
        tableMap.put("#Exp4", hm);
        hm.put("(", lbr_Exp_rbr);
        hm.put("BOOLEAN", BOOLEAN);
        hm.put("NUM", NUM);
        hm.put("VAR", VAR);
    }

    // May add auxiliary methods here if desired

    String[] tableEntry(String nonterm, String tokClass) {
        try {
            return tableMap.get(nonterm).get(tokClass);
        } catch (Exception e) {
            System.out.println(nonterm);
            System.out.println(tokClass);
        }
        return null;
    }
}


// For testing

class MH_ParserDemo {

    static PARSER MH_Parser = new MH_Parser();

    public static void main(String[] args) throws Exception {
        Reader reader = new BufferedReader(new FileReader(args[0]));
        LEX_TOKEN_STREAM MH_Lexer = new CheckedSymbolLexer(new MH_Lexer(reader));
        TREE theTree = MH_Parser.parseTokenStream(MH_Lexer);
    }
}

