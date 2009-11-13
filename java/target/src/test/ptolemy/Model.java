package ptolemy;
/* Generated by Ptolemy II (http://ptolemy.eecs.berkeley.edu)
Copyright (c) 2005-2009 The Regents of the University of California.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.
IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.
*/
public class Model {
    /* Generate type resolution code for .Model */
    // ConstantsBlock from codegen/java/kernel/SharedCode.j
    private final short TYPE_Token = -1;
    // #define PTCG_TYPE_String 0
    private final short TYPE_String = 0;
    // #define PTCG_TYPE_Boolean 5
    private final short TYPE_Boolean = 5;
    // #define FUNC_new 0
    // #define FUNC_isCloseTo 1
    // #define FUNC_delete 2
    // #define FUNC_convert 3
    // #define FUNC_toString 4
    // #define FUNC_add 5
    // #define FUNC_print 6
    Token emptyToken; /* Used by *_delete() and others. */
    // Token String_new (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_equals (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_isCloseTo (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_delete (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_convert (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_toString (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_add (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token String_print (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_new (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_equals (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_isCloseTo (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_convert (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_toString (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_add (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    // Token Boolean_print (Token thisToken, Token... tokens);  From codegen/java/kernel/SharedCode.j
    /* We share one method between all scalar types so as to reduce code size. */
    Token scalarDelete(Token token, Token... tokens) {
        /* We need to return something here because all the methods are declared
        * as returning a Token so we can use them in a table of functions.
        */
        return emptyToken;
    }
    Integer StringtoInteger(String string) {
        return Integer.valueOf(string);
    }
    Long StringtoLong(String string) {
        return Long.valueOf(string);
    }
    Integer DoubletoInteger(Double d) {
        return Integer.valueOf((int)Math.floor(d.doubleValue()));
    }
    Double IntegertoDouble(Integer i) {
        return Double.valueOf(i.doubleValue());
    }
    Long IntegertoLong(int i) {
        return Long.valueOf(i);
    }
    String IntegertoString(int i) {
        return Integer.toString(i);
    }
    String LongtoString(long l) {
        return Long.toString(l);
    }
    String DoubletoString(double d) {
        return Double.toString(d);
    }
    String BooleantoString(boolean b) {
        return Boolean.toString(b);
    }
    String UnsignedBytetoString(byte b) {
        return Byte.toString(b);
    }
    private final int NUM_TYPE = 2;
    private final int NUM_FUNC = 7;
    //Token (*functionTable[NUM_TYPE][NUM_FUNC])(Token, ...)= {
    //	{String_new, String_equals, String_delete, String_convert, String_toString, String_add, String_print},
    //	{Boolean_new, Boolean_equals, scalarDelete, Boolean_convert, Boolean_toString, Boolean_add, Boolean_print}
    //};
    int convert_Integer_Integer(int a) {
        return a;
    }
    // Array_add: Add an array to another array.
    // Assume the given otherToken is array type.
    // Return a new Array token.
    Token Array_add(Token thisToken, Token... tokens) {
        int i;
        int size1;
        int size2;
        int resultSize;
        Token result = new Token();
        Token otherToken;
        otherToken = tokens[0];
        size1 = ((Array)(thisToken.payload)).size;
        size2 = ((Array)(otherToken.payload)).size;
        resultSize = (size1 > size2) ? size1 : size2;
        result = Array_new(resultSize, 0);
        for (i = 0; i < resultSize; i++) {
            if (size1 == 1) {
                Array_set(result, i, add_Token_Token(Array_get(thisToken, 0), Array_get(otherToken, i)));
            } else if (size2 == 1) {
                //result.payload.Array->elements[i] = functionTable[(int)Array_get(otherToken, 0).type][FUNC_add](Array_get(thisToken, i), Array_get(otherToken, 0));
                Array_set(result, i, add_Token_Token(Array_get(thisToken, i), Array_get(otherToken, 0)));
            } else {
                //result.payload.Array->elements[i] = functionTable[(int)Array_get(thisToken, i).type][FUNC_add](Array_get(thisToken, i), Array_get(otherToken, i));
                Array_set(result, i, add_Token_Token(Array_get(thisToken, i), Array_get(otherToken, i)));
            }
        }
        return result;
    }
    // Array_toString: Return a string token with a string representation
    // of the specified array.
    Token Array_toString(Token thisToken, Token... ignored) {
        StringBuffer result = new StringBuffer("{");
            for (int i = 0; i < ((Array)(thisToken.payload)).size; i++) {
                if (i != 0) {
                    result.append(", ");
                }
                // Arrays elements could have different types?
                if (((Array)(thisToken.payload)).elements == null) {
                    result.append("elements == null");
                } else if (((Array)(thisToken.payload)).elements[i] == null) {
                    result.append("elements[" + i + "] == null");
                    throw new RuntimeException("elements[] is null");
                } else {
                    short elementType = ((Array)(thisToken.payload)).elements[i].type;
                    switch(elementType) {
                        case TYPE_Array:
                        result.append(Array_toString(((Array)(thisToken.payload)).elements[i]).payload);
                        break;
                        case TYPE_String:
                        result.append("\"" + ((Array)(thisToken.payload)).elements[i].payload.toString() + "\"");
                        break;
                        default:
                        result.append(((Array)(thisToken.payload)).elements[i].payload.toString());
                        break;
                    }
                }
            }
        result.append("}");
        return String_new(result.toString());
    }
    // Array_print: Print the contents of an array to standard out.
    Token Array_print(Token thisToken, Token... tokens) {
        // Token string = Array_toString(thisToken);
        // System.out.printf(string.payload.String);
        // free(string.payload.String);
        StringBuffer results = new StringBuffer("{");
            for (int i = 0; i < ((Array)(thisToken.payload)).size; i++) {
                if (i != 0) {
                    results.append(", ");
                }
                // Arrays elements could have different types?
                if (((Array)(thisToken.payload)).elements == null) {
                    results.append("elements == null");
                } else if (((Array)(thisToken.payload)).elements[i] == null) {
                    results.append("elements[" + i + "] == null");
                } else {
                    short elementType = ((Array)(thisToken.payload)).elements[i].type;
                    switch(elementType) {
                        case TYPE_Array:
                        results.append(Array_toString(((Array)(thisToken.payload)).elements[i]).payload);
                        break;
                        default:
                        results.append(((Array)(thisToken.payload)).elements[i].payload.toString());
                        break;
                    }
                }
            }
        results.append("}");
        System.out.println(results.toString());
        return null;
    }
    Token add_Token_Token(Token a1, Token a2) {
        Token result = null;
        switch (a1.type) {
            // #ifdef PTCG_TYPE_Double
            //    case TYPE_Double:
            //        switch (a2.type) {
                //            case TYPE_Double:
                //                    result = Double_new((Double)a1.payload + (Double)a2.payload);
                //                break;
                //            default:
                //                System.out.println("add_Token_Token(): a1 is a Double, "
                //                        + "a2 is a " + a2.type);
                //                result = null;
            //        }
            //        break;
            // #endif
            // #ifdef PTCG_TYPE_Integer
            //    case TYPE_Integer:
            //        switch (a2.type) {
                //            case TYPE_Integer:
                //                    result = Integer_new((Integer)a1.payload + (Integer)a2.payload);
                //                break;
                //            default:
                //                System.out.println("add_Token_Token(): a1 is a Integer, "
                //                        + "a2 is a " + a2.type);
                //                result = null;
            //        }
            //        break;
            // #endif
            case TYPE_Array:
            switch (a2.type) {
                case TYPE_Array:
                result = Array_add(a1, a2);
                break;
                default:
                result = null;
            }
            break;
            default:
            System.out.println("add_Token_Token(): a1 is a " + a1.type
            + "a2 is a " + a2.type);
            result = null;
        }
        if (result == null) {
            throw new InternalError("add_Token_Token_(): Add with an unsupported type. "
            + a1.type + " or " + a2.type);
        }
        return result;
    }
    void print_Token2(Token token) {
        if (token == null) {
            System.out.println("Token is null");
            return;
        }
        switch (token.type) {
            case TYPE_Integer:
            System.out.println((Integer) token.payload);
            break;
            case TYPE_Array:
            Array_print(token);
            break;
            default:
            System.out.println(token);
            break;
        }
    }
    Token toString_Token_Token(Token thisToken, Token... otherToken) {
        Token result = null;
        switch (thisToken.type) {
            // #ifdef PTCG_TYPE_Double
            //    case TYPE_Double:
            //               result = Double_toString(thisToken);
            //        result.type = TYPE_String;
            //        return result;
            // #endif
            // #ifdef PTCG_TYPE_Integer
            //    case TYPE_Integer:
            //               result = Integer_toString(thisToken);
            //        result.type = TYPE_String;
            //        return result;
            // #endif
            // #ifdef PTCG_TYPE_Array
            //     case TYPE_Array:
            //               result = Array_toString(thisToken);
            //               result.type = TYPE_String;
            //               return result;
            // #endif
            default:
            throw new InternalError("toString_Token_Token_(): unsupported type: "
            + thisToken.type);
        }
    }
    /* Make a new integer token from the given value. */
    Token String_new(String s) {
        Token result = new Token();;
        result.type = TYPE_String;
        result.payload = new String(s);
        return result;
    }
    Token String_equals(Token thisToken, Token... tokens) {
        Token otherToken = tokens[0];
        return Boolean_new(((String)(thisToken.payload)).equals((String)(otherToken.payload)));
    }
    Token String_delete(Token token, Token... ignored) {
        //free(token.payload.String);
        /* We need to return something here because all the methods are declared
        * as returning a Token so we can use them in a table of functions.
        */
        return emptyToken;
    }
    Token String_convert(Token token, Token... ignored) {
        switch (token.type) {
            // #ifdef PTCG_TYPE_Boolean
            case TYPE_Boolean:
            token.payload = BooleantoString((Boolean)(token.payload));
            token.type = TYPE_String;
            return token;
            // #endif
            // #ifdef PTCG_TYPE_Integer
            //    case TYPE_Integer:
            //        token.payload = IntegertoString((Integer)(token.payload));
            //        token.type = TYPE_String;
            //        return token;
            // #endif
            // #ifdef PTCG_TYPE_Double
            //    case TYPE_Double:
            //        token.payload = DoubletoString((Double)(token.payload));
            //        token.type = TYPE_String;
            //        return token;
            // #endif
            case TYPE_String:
            return token;
            // FIXME: not finished
            default:
            throw new RuntimeException("String_convert(): Conversion from an unsupported type: "
            + token.type);
        }
    }
    Token String_toString(Token thisToken, Token... ignored) {
        return String_new("\"" + (String)(thisToken.payload) + "\"");
    }
    Token String_add(Token thisToken, Token... tokens) {
        Token otherToken = tokens[0];
        return String_new((String)(String_convert(thisToken).payload)
        + (String)(String_convert(otherToken).payload));
    }
    Token String_print(Token thisToken, Token... tokens) {
        System.out.println((String)(thisToken.payload));
        return emptyToken;
    }
    // make a new integer token from the given value.
    Token Boolean_new(boolean b) {
        Token result = new Token();
        result.type = TYPE_Boolean;
        result.payload = Boolean.valueOf(b);
        return result;
    }
    Token Boolean_equals(Token thisToken, Token... tokens) {
        Token otherToken;
        otherToken = tokens[0];
        return Boolean_new(
        ( (Boolean)thisToken.payload && (Boolean)otherToken.payload ) ||
        ( !(Boolean)thisToken.payload && !(Boolean)otherToken.payload ));
    }
    /* Instead of Boolean_delete(), we call scalarDelete(). */
    Token Boolean_convert(Token token, Token... tokens) {
        switch (token.type) {
            case TYPE_Boolean:
            return token;
            default:
            throw new RuntimeException("Boolean_convert(): Conversion from an unsupported type.: " + token.type);
        }
    }
    Token Boolean_toString(Token thisToken, Token... ignored) {
        return String_new(BooleantoString((Boolean)thisToken.payload));
    }
    Token Boolean_add(Token thisToken, Token... tokens) {
        Token otherToken = tokens[0];
        return Boolean_new((Boolean)thisToken.payload || (Boolean)otherToken.payload);
    }
    Token Boolean_print(Token thisToken, Token... tokens) {
        System.out.println((Boolean)thisToken.payload);
        return null;
    }
    /* end shared code */
    /* Model_Ramp's referenced parameter declarations. */
    static int Model_Ramp_step_;
    /* Model_Display's input variable declarations. */
    static Token Model_Display_input[] = new Token[1];
    /* Director has a period attribute, so we track current time. */
    double _currentTime = 0;
    /* Provide the period attribute as constant. */
    public final static double PERIOD = 0.5;
    /* The preinitialization of the director. */
    /* preinitRamp */
    static int Model_Ramp__state;
    /* end preinitialize code */
    public void initialize() {
        /* Ramp's parameter initialization */
        Model_Ramp_step_ = 1;
        /* The initialization of the director. */
        /* initialize Ramp */
        Model_Ramp__state = 0;
    }
    public void wrapup() {
        /* The wrapup of the director. */
    }
    public static void main(String [] args) throws Exception {
        Model model = new Model();
        model.initialize();
        model.execute();
        model.doWrapup();
        System.exit(0);
    }
    // Don't call initialize() here, it is called in main.
    public void execute() throws Exception {
        int iteration;
        for (iteration = 0; iteration < 10; iteration ++) {
            run();
        }
    }
    public void run() throws Exception {
        /* The firing of the StaticSchedulingDirector */
        /* Fire Model_Ramp */
        Model_Display_input[0]=Model_Ramp__state;
        ;
        ;
        if (false) {
            Model_Ramp_step_ = convert_Integer_Integer(0);
        }
        Model_Ramp__state = add_Token_Token(Model_Ramp__state, Model_Ramp_step_);
        /*
        ....Begin updateOffset....Model_Ramp_trigger */
        /*
        ....Begin updateOffset....Model_Ramp_step */
        /*
        ....Begin updateConnectedPortsOffset....Model_Ramp_output */
        /*
        ....End updateConnectedPortsOffset....Model_Ramp_output */
        /* Fire Model_Display */
        System.out.println("Display: " + toString_Token_Token(Model_Display_input[0]).payload);
        /*
        ....Begin updateOffset....Model_Display_input */
        /*
        ....End updateOffset....Model_Display_input */
        /* The postfire of the director. */
        _currentTime += 0.5;
    }
    public void doWrapup() throws Exception {
    }
}
