package at.lernplattform.application.tasks.tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.springframework.jdbc.core.JdbcTemplate;

import at.lernplattform.application.tasks.tests.sql.SQLConditionCollection;
import at.lernplattform.application.tasks.tests.sql.SQLConditionElement;
import at.lernplattform.application.tasks.tests.sql.SQLElement;
import at.lernplattform.application.tasks.tests.sql.SQLSelectStatement;
import at.lernplattform.application.tasks.tests.sql.SQLStringElement;
import at.lernplattform.application.tasks.tests.sql.Token;
import at.lernplattform.rest.api.model.SchemaModel;
import io.swagger.models.Operation;

public class Utilities {
	
	public static String FormatSql(String sql)
    {	
        String s = sql.replace("\r", " ");
        s = s.replace("\n", " ");
        s = s.replace(";", "");

        s = s.replaceAll("\\s+", " ");
        s = s.toLowerCase();
        return s;
    }
	
	public static SQLSelectStatement BuildSelectFromStatement (String SelectText) {
		String FormatedStatement = Utilities.FormatSql(SelectText);
		SQLSelectStatement statement = new SQLSelectStatement();
		Stack<Token> stack = new Stack<Token>();
		
		String[] split = IntelligentSplit(FormatedStatement, ' ', "['\"", "]'\"");
		SQLConditionElement condition = new SQLConditionElement();
		String Operator = "";
		
		for(int i = 0; i < split.length; i++) {
			TokenReturn tok = GetToken(split, i);
			
			i = handleSelectBlock(statement, stack, split, i, tok);
			
			i = handleFromBlock(statement, stack, split, i, tok);
			
			//-------------------------------
			// On Block 
			//-------------------------------
			if(stack.peek() == Token.ON) {
				if(tok.tok == Token.WHERE ||
						tok.tok == Token.GROUP_BY ||
						tok.tok == Token.HAVING ||
						tok.tok == Token.ORDER_BY 						
						) {
					condition = closeOnCondition(statement, condition, Operator);
					Operator = "";
				}
				else if(tok.tok == Token.BOOL_OPERATORS)
				{
					condition = closeOnCondition(statement, condition, Operator);
					Operator = split[i];					
				}
				else if(tok.tok == Token.JOIN)
				{
					condition = closeOnCondition(statement, condition, Operator);
					i = handleJoinBlock(statement, stack, split, i);
					Operator = "";
				} else
					i = createConditionBlock(split, condition, i, tok);		
			}
			
			
			//-------------------------------
			// Where Block
			//-------------------------------
			if(tok.tok == Token.WHERE) {
				if(stack.peek() == Token.FROM ||
						stack.peek() == Token.SELECT) 
					stack.pop();
				
				stack.push(tok.tok);
				Operator = "";
				
			// Inside where Statement
			}else if (stack.peek() == Token.WHERE) {
				if(tok.tok == Token.GROUP_BY ||
						tok.tok == Token.HAVING ||
						tok.tok == Token.ORDER_BY
						)
					condition = closeWhereCondition(statement, condition, Operator);
				else if(tok.tok == Token.BOOL_OPERATORS)
				{
					condition = closeWhereCondition(statement, condition, Operator);
					Operator = split[i];					
				}
				else
					i = createConditionBlock(split, condition, i, tok);		
			}
			
			
			//-------------------------------
			// Group By Block
			//-------------------------------
			if(tok.tok == Token.GROUP_BY) {
				if(stack.peek() == Token.FROM ||
						stack.peek() == Token.SELECT ||
						stack.peek() == Token.WHERE) 
					stack.pop();
				
				stack.push(tok.tok);
				
			// Inside Group by Statement
			}else if (stack.peek() == Token.GROUP_BY && tok.tok == Token.STRING) {
				SQLElement elem = new SQLElement();
				elem.Element = split[i];				
				statement.GroupByList.add(elem);
			}
			
			if(tok.tok == Token.HAVING) {
				if(stack.peek() == Token.FROM ||
						stack.peek() == Token.SELECT ||
						stack.peek() == Token.WHERE ||
						stack.peek() == Token.GROUP_BY) 
					stack.pop();
				
				stack.push(tok.tok);
				
			//Inside Having Statement
			}else if (stack.peek() == Token.HAVING) {
				if(tok.tok == Token.ORDER_BY)
					condition = closeHavingCondition(statement, condition, Operator);
				
				else if(tok.tok == Token.BOOL_OPERATORS)
				{
					condition = closeHavingCondition(statement, condition, Operator);
					Operator = split[i];					
				}
				else
					i = createConditionBlock(split, condition, i, tok);		
			}
			
			if(tok.tok == Token.ORDER_BY) {
				if(stack.peek() == Token.FROM ||
						stack.peek() == Token.SELECT ||
						stack.peek() == Token.WHERE ||
						stack.peek() == Token.GROUP_BY ||
						stack.peek() == Token.HAVING) 
					stack.pop();
				
				stack.push(tok.tok);
				
			// Inside Order by Statement
			}else if (stack.peek() == Token.ORDER_BY) {
				SQLElement elem = new SQLElement();
				elem.Element = split[i];			
				
				statement.OrderByList.add(elem);
			}
			
			
			if(tok.index != i)
				i = tok.index;
		}

		if(stack.peek() == Token.WHERE)
			closeWhereCondition(statement, condition, Operator);
		if(stack.peek() == Token.ON)
			closeOnCondition(statement, condition, Operator);
		if(stack.peek() == Token.HAVING)
			closeHavingCondition(statement, condition, Operator);
		
		return statement;
	}

	private static int createConditionBlock(String[] split, SQLConditionElement condition, int i, TokenReturn tok) {
		if(tok.tok == Token.STRING && condition.Operation.equals(""))
				condition.Comperator1 += split[i] + " "; 
		else if(tok.tok == Token.STRING && !condition.Operation.equals(""))
				condition.Comperator2 += split[i] + " ";
		else if(tok.tok == Token.OPERATOR) {
				if(split[i].equals("is")) {
					condition.Operation = "is ";
					i++;
					
					if(split[i].equals("not")) {
						condition.Operation += "not ";
						i++;
					}
		
					condition.Operation += split[i];
						
				} else 
					condition.Operation = split[i];					
		}
		return i;
	}

	private static int handleFromBlock(SQLSelectStatement statement, Stack<Token> stack, String[] split, int i,
			TokenReturn tok) {
		if(tok.tok == Token.FROM) {
			if(stack.peek() == Token.SELECT) 
				stack.pop();
			
			stack.push(tok.tok);
			
		// Inside From statement
		}else if (stack.peek() == Token.FROM) {
			if (tok.tok == Token.STRING) {
				statement.From.addTable(split[i]);
			} else if (tok.tok == Token.JOIN) {
				i = handleJoinBlock(statement, stack, split, i);
			}
		}
		return i;
	}

	private static int handleJoinBlock(SQLSelectStatement statement, Stack<Token> stack, String[] split, int i) {
		String table = split[i+1];
		String alias = "";
		i++;
		
		if (GetToken(split, i + 1).tok == Token.AS)
			i++;
		
		if (GetToken(split, i + 1).tok == Token.STRING)
		{
			alias = split[i + 1];
			i++;
		}
			
		if (GetToken(split, i + 1).tok == Token.ON) {
			stack.push(Token.ON);
			i++;
		}
		
		statement.From.addTable(table, alias);
		return i;
	}

	private static SQLConditionElement closeOnCondition(SQLSelectStatement statement, SQLConditionElement condition, String Operator) {		
		// Change order, so we don't have to handle the different Ways to write lower / bigger
		if(condition.Operation.equals("<") ||
				condition.Operation.equals("<="))
		{
			Object ConditionSave = condition.Comperator1;
			condition.Comperator1 = condition.Comperator2;
			condition.Comperator2 = ConditionSave;

			if(condition.Operation.equals("<"))
				condition.Operation = ">";
			else if(condition.Operation.equals("<="))
				condition.Operation = ">=";
		}
		
		if(statement.From.OnClauses == null)
			statement.From.OnClauses = new SQLConditionCollection(condition);
		else 
			statement.From.OnClauses.addCondition(condition, Operator);
		
		condition = new SQLConditionElement();
		return condition;
	}

	private static SQLConditionElement closeWhereCondition(SQLSelectStatement statement, SQLConditionElement condition, String Operator) {		
		// Change order, so we don't have to handle the different Ways to write lower / bigger
		if(condition.Operation.equals("<") ||
				condition.Operation.equals("<="))
		{
			Object ConditionSave = condition.Comperator1;
			condition.Comperator1 = condition.Comperator2;
			condition.Comperator2 = ConditionSave;

			if(condition.Operation.equals("<"))
				condition.Operation = ">";
			else if(condition.Operation.equals("<="))
				condition.Operation = ">=";
		}
		
		if(statement.WhereClauses == null)
			statement.WhereClauses = new SQLConditionCollection(condition);
		else 
			statement.WhereClauses.addCondition(condition, Operator);
		
		condition = new SQLConditionElement();
		return condition;
	}
	
	private static SQLConditionElement closeHavingCondition(SQLSelectStatement statement, SQLConditionElement condition, String Operator) {		
		// Change order, so we don't have to handle the different Ways to write lower / bigger
		if(condition.Operation.equals("<") ||
				condition.Operation.equals("<="))
		{
			Object ConditionSave = condition.Comperator1;
			condition.Comperator1 = condition.Comperator2;
			condition.Comperator2 = ConditionSave;

			if(condition.Operation.equals("<"))
				condition.Operation = ">";
			else if(condition.Operation.equals("<="))
				condition.Operation = ">=";
		}
				
		if(statement.HavingConditions == null)
			statement.HavingConditions = new SQLConditionCollection(condition);
		else 
			statement.HavingConditions.addCondition(condition, Operator);
		
		condition = new SQLConditionElement();
		return condition;
	}

	private static int handleSelectBlock(SQLSelectStatement statement, Stack<Token> stack, String[] split, int i,
			TokenReturn tok) {
		if(tok.tok == Token.SELECT)
			//TODO: Check if it is root select
			stack.push(tok.tok);
		
		//Inside select statement
		else if(stack.peek() == Token.SELECT ) {
			if (tok.tok == Token.STRING) {
				if(GetToken(split, i + 1).tok == Token.AS && GetToken(split, i + 1).tok == Token.STRING)
				{
					statement.Select.add(split[i], split[i+2]);
					i+=2;
				}
				else
					statement.Select.add(split[i]);
			}
			else if(tok.tok == Token.DISTINCT)
				statement.Select.distinct = true;
			else if(tok.tok == Token.TOP && GetToken(split, i + 1).tok == Token.STRING)
			{
				i++;
				statement.Select.top = split[i];
			}
		}
		return i;
	}

	public static String[] IntelligentSplit(String formatedStatement, char delim, String FieldDelim, String FieldEndDelim) {
		ArrayList<String> result = new ArrayList<String>();
        String field = "";
        boolean infield = false;
        int delimID = -1;

        for (int i = 0; i < formatedStatement.length(); i++)
        {
            if (infield)
            {
                if (FieldEndDelim.charAt(delimID) == formatedStatement.charAt(i))
                {
                    infield = false;
                }

                field += formatedStatement.charAt(i);
            }
            else
            {
                if ((delimID = FieldDelim.indexOf(formatedStatement.charAt(i))) >= 0)
                {
                	infield = true;
                }
                else if(formatedStatement.charAt(i) == delim) {
                    result.add(field.trim());
                    field = "";
                }
                field += formatedStatement.charAt(i);
            }
        }

        result.add(field.trim());
        field = "";

		return GetStringArray(result);
	}
	
	 // Function to convert ArrayList<String> to String[] 
    public static String[] GetStringArray(ArrayList<String> arr) 
    { 
  
        // declaration and initialise String Array 
        String str[] = new String[arr.size()]; 
  
        // ArrayList to Array Conversion 
        for (int j = 0; j < arr.size(); j++) { 
  
            // Assign each value to String array 
            str[j] = arr.get(j); 
        } 
  
        return str; 
    } 

	private static TokenReturn GetToken(String[] split,  int index) {
		try {
			switch(split[index]) {
				case "select":
					return new TokenReturn(Token.SELECT, index);
				case "distinct":
					return new TokenReturn(Token.DISTINCT, index);
				case "top":
					return new TokenReturn(Token.TOP, index);
				case "into":
					return new TokenReturn(Token.INTO, index);
				case "from":
					return new TokenReturn(Token.FROM, index);
				case "where":
					return new TokenReturn(Token.WHERE, index);
				case "group":
					if(split[index+1].equals("by"))
						return new TokenReturn(Token.GROUP_BY, index + 1);
					break;
				case "having":
					return new TokenReturn(Token.HAVING, index);
				case "order":
					if(split[index+1].equals("by"))
						return new TokenReturn(Token.ORDER_BY, index + 1);
					break;
					
				case "union":
					if(split[index+1].equals("all"))
						return new TokenReturn(Token.UNION_ALL, index + 1);
					return new TokenReturn(Token.UNION, index);
	
				case "=":
				case "==":
				case ">":
				case ">=":
				case "<":
				case "<=":
				case "!=":
				case "<>":
				case "like":
				case "in":
					return new TokenReturn(Token.OPERATOR, index);
					
				case "is":
					int saveIndex = index;
					if(split[index + 1] == "not")
						saveIndex++;
					
					if(split[saveIndex + 1] == "NULL" ||
						split[saveIndex + 1] == "TRUE" ||
						split[saveIndex + 1] == "FALSE" )
						return new TokenReturn(Token.OPERATOR, saveIndex + 1);
				
				case "and":
				case "or":
					return new TokenReturn(Token.BOOL_OPERATORS, index);
			}
		}
		catch (Exception e) { 
			return new TokenReturn(Token.ERROR, index);
		}
		return new TokenReturn(Token.STRING, index);
	}

	public static String CompareSQL(SQLSelectStatement baseStatement, SQLSelectStatement comparisionStatement) {
		//TODO: Extended Mode Dynamic setting
		return CompareSQL(baseStatement, comparisionStatement, true);
	}
	
	public static String CompareSQL(SQLSelectStatement baseStatement, SQLSelectStatement comparisionStatement, boolean ExtendedMode) {
		String ResultString = "";
		
		ResultString = checkSelectStatement(baseStatement, comparisionStatement, ExtendedMode);

		//Check Table List
		ResultString += compareSQLElementList(baseStatement.From.Tables, comparisionStatement.From.Tables, 
				ExtendedMode, "from");

		//Check Order By List
		ResultString += compareSQLElementList(baseStatement.OrderByList, comparisionStatement.OrderByList, 
				ExtendedMode, "order by");

		//Check Group By List
		ResultString += compareSQLElementList(baseStatement.GroupByList, comparisionStatement.GroupByList, 
				ExtendedMode, "group by");		

		//Check Where List
		ResultString += compareClauseElementList(baseStatement.WhereClauses, comparisionStatement.WhereClauses, 
				ExtendedMode, "where");		

		//Check On List
		ResultString += compareClauseElementList(baseStatement.From.OnClauses, comparisionStatement.From.OnClauses, 
				ExtendedMode, "on");		

		//Check having List
		ResultString += compareClauseElementList(baseStatement.HavingConditions, comparisionStatement.HavingConditions, 
				ExtendedMode, "having");		
		
		
		return ResultString;
	}

	private static String compareClauseElementList(SQLConditionCollection baseClauses,
			SQLConditionCollection comparisionClauses, boolean extendedMode, String Type) {
		String ResultString = "";
		String wrongClauses = "";
		
		if(coalesce(baseClauses, comparisionClauses) == null)
			return "";
		else if (coalesce(baseClauses, comparisionClauses) == coalesce(comparisionClauses, baseClauses)) {
			if(baseClauses == null)
				return String.format("Unnescesary Clause in %s\n", Type);
			else
				return String.format("Missing Clause in %s\n", Type);
		}
		
		Character curSym = 'a';
		HashMap<String, Character> replacementList = new HashMap<>();
		for (Object o : baseClauses.ConditionList) {
			if(o.getClass().getName().equals("java.lang.String"))
				continue;
			
			if(!replacementList.containsValue(o)) {
				replacementList.put(FormatSql(o.toString()), curSym);
				curSym++;			
			}
		}
		
		int i = 0;
		for (Object o : comparisionClauses.ConditionList) {
			if(o.getClass().getName().equals("java.lang.String"))
				continue;
			
			if(!replacementList.containsKey(FormatSql(o.toString()))) {
				wrongClauses += String.format("Unnessesary Clause in %s: %s\n", Type, o.toString())	;
			}else {
				i++;
			}
		} 
		
		if(i < replacementList.size()) {
			wrongClauses += String.format("%s Missing Clause(s) in %s", replacementList.size() - i, Type)	;
		}
		
		if(wrongClauses.length() > 0)
			return wrongClauses;

		String baseClauseString = generateClauseString(baseClauses, replacementList);
		String compClauseString = generateClauseString(comparisionClauses, replacementList);
		
		if(baseClauseString.equals(compClauseString))
			return "";
		
		if(!eval(baseClauseString, compClauseString, new ArrayList(replacementList.values()))) {
			return String.format("Error in boolean algebra of the %s clause. Check AND, OR and XOR\n", Type);
		}
		
		return "";
	}
	
	private static boolean eval(String baseClauseString, String compClauseString, List<Character> values) {
		return eval(baseClauseString, compClauseString, values, 0);
	}

	private static boolean eval(String baseClauseString, String compClauseString, List<Character> values, int pos) {
		String baseCaseF = baseClauseString.replaceAll(values.get(pos).toString(), "0");  
		String baseCaseT = baseClauseString.replaceAll(values.get(pos).toString(), "1");  
		
		String compCaseF = compClauseString.replaceAll(values.get(pos).toString(), "0");  
		String compCaseT = compClauseString.replaceAll(values.get(pos).toString(), "1");  
		
		if(values.size() == pos + 1) {
			return evaluateBoolExpr(baseCaseF) == evaluateBoolExpr(compCaseF) &&
					evaluateBoolExpr(baseCaseT) == evaluateBoolExpr(compCaseT);
		}else {
			return eval(baseCaseF, compCaseF, values, pos + 1) && eval(baseCaseT, compCaseT, values, pos + 1);
		}
	}

	private static String generateClauseString(SQLConditionCollection clauses, HashMap<String, Character> replacementList) {
		String clauseString = ""; 
		for (Object o : clauses.ConditionList) {
			if(o.getClass().getName().equals("java.lang.String")){
				switch((String)o) {
					case "and":
						clauseString += "A";
						break;
					case "or":
						clauseString += "B";
						break;
					case "xor":
						clauseString += "C";
						break;
				}
			} else {
				clauseString += replacementList.get(FormatSql(o.toString()));
			}
		}
		
		return clauseString;		
	}
	
	private static boolean evaluateBoolExpr(String str) 
    { 
		StringBuffer s = new StringBuffer(str);
        int n = s.length(); 
       
        // Traverse all operands by jumping 
        // a character after every iteration. 
        for (int i = 0; i < n; i += 2) { 
       
            // If operator next to current operand 
            // is AND. 
            if( i + 1 < n && i + 2 < n) 
            { 
                if (s.charAt(i + 1) == 'A') { 
                    if (s.charAt(i + 2) == '0' ||  
                            s.charAt(i) == 0) 
                        s.setCharAt(i + 2, '0'); 
                    else
                        s.setCharAt(i + 2, '1'); 
                } 
           
                // If operator next to current operand 
                // is OR. 
                else if ((i + 1) < n &&  
                           s.charAt(i + 1 ) == 'B') { 
                    if (s.charAt(i + 2) == '1' || 
                          s.charAt(i) == '1') 
                        s.setCharAt(i + 2, '1'); 
                    else
                        s.setCharAt(i + 2, '0'); 
                } 
                  
                // If operator next to current operand 
                // is XOR (Assuming a valid input) 
                else { 
                    if (s.charAt(i + 2) == s.charAt(i)) 
                        s.setCharAt(i + 2, '0'); 
                    else
                        s.setCharAt(i + 2 ,'1'); 
                } 
            } 
        } 
        return (s.charAt(n - 1) - '0') == 1; 
    } 

	private static String checkSelectStatement(SQLSelectStatement baseStatement,
			SQLSelectStatement comparisionStatement, boolean ExtendedMode) {
		String ResultString = "";
		
		// Check if too many Fields are selected
		if(comparisionStatement.Select.selects.size() > baseStatement.Select.selects.size())
			ResultString += "Too many fields returned [Check your selected Fields]\n";

		// Check for missing Fields
		ResultString += compareSQLElementList(baseStatement.Select.selects, comparisionStatement.Select.selects, 
				ExtendedMode, "select");
		return ResultString;
	}

	private static String compareSQLElementList(ArrayList<SQLElement> baseStatement,
			ArrayList<SQLElement> comparisionStatement, boolean ExtendedMode, String Type) {
		String ResultString = "";
		
		ArrayList<SQLElement> copiedSQLList = (ArrayList<SQLElement>)baseStatement.clone();
		for(int i = 0, j = 0; i < comparisionStatement.size(); i++, j = 0) {
			boolean found = false; 
			for(; j <  copiedSQLList.size(); j++) {				
				if(copiedSQLList.get(j).getClass().getName().toString().equals("at.lernplattform.application.tasks.tests.sql.SQLStringElement")) {
					if(comparisionStatement.get(j).getClass().getName().toString().equals("at.lernplattform.application.tasks.tests.sql.SQLStringElement")) {
						if(((SQLStringElement)copiedSQLList.get(j)).equals((SQLStringElement)comparisionStatement.get(i)))
						{
							found = true;
							break;
						}
					}
				}else {				
					if(copiedSQLList.get(j).equals(comparisionStatement.get(i)))
					{
						found = true;
						break;
					}
				}
			}
			
			if(found) 
				copiedSQLList.remove(j);
		}
		if(copiedSQLList.size() > 0)
			ResultString += "Too few fields returned [Check your " + Type + " Fields]\n";
		
		if(ExtendedMode) {
			for(int i = 0; i < copiedSQLList.size(); i++) {				
				SQLElement s = copiedSQLList.get(i);
				if(Type.equals("from"))
					ResultString += String.format("Mising Field: Table %s\n", s.Table != "" ? s.Table : "DefaultTable" );
				else
					ResultString += String.format("Mising " + Type + " Field: %s from Table %s\n", ((SQLStringElement)s).Value, s.Table != "" ? s.Table : "DefaultTable" );
				
			}
		}
		return ResultString;
	}

	public static SQLSelectStatement ReplaceWildcards(SQLSelectStatement statement, SchemaModel schema,
			JdbcTemplate jdbcTemplate) {
		
		ArrayList<SQLElement> selectsCopy = (ArrayList<SQLElement>) statement.Select.selects.clone();
		
		for(var elem : selectsCopy){
			if(SQLStringElement.class.isInstance(elem) && ((SQLStringElement)elem).Value.equals("*"))
			{
				statement.Select.selects.remove(elem);
				for(var table : statement.From.Tables) {
					//TODO: Behandlung fuer Alias/Table definition
					//TODO: Implementierung fuer andere SQL Server
					String sql = String.format("select column_name from information_schema.columns where table_schema = '%s' and table_name = '%s'", 
							schema.getName(),
							table.Table);
					try {					
						Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement();
						
						ResultSet rs = stm.executeQuery(sql);
						while(rs.next()) {
							statement.Select.add(rs.getString("column_name"));
						}
						
					}catch (Exception e) {}
				}
			}
		}
		
		return statement;
	}
			  
    // function to generate a random string of length n 
    public static String getAlphaNumericString(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
	
	public static <T> T coalesce(T a, T b) {
	    return a == null ? b : a;
	}
}

