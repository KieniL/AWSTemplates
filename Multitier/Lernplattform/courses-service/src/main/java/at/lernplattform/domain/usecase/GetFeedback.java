package at.lernplattform.domain.usecase;

import at.lernplattform.application.dao.CourseDao;
import at.lernplattform.application.dao.TaskDao;
import at.lernplattform.application.database.model.Course;
import at.lernplattform.application.database.model.Task;
import at.lernplattform.application.tasks.CreateFeedbackCommand;
import at.lernplattform.application.tasks.tests.Utilities;
import at.lernplattform.application.tasks.tests.sql.SQLSelectStatement;
import at.lernplattform.domain.course.Feedback;
import at.lernplattform.domain.course.FeedbackFactory;
import at.lernplattform.rest.api.model.SchemaModel;
import javassist.compiler.SyntaxError;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


public class GetFeedback {

    private CreateFeedbackCommand createFeedbackCommand;

    public GetFeedback() {
    }

    public GetFeedback(CreateFeedbackCommand createFeedbackCommand) {
        this.createFeedbackCommand = createFeedbackCommand;
    }

    public static String checkSQLStatement(String Base, String Comparision, SchemaModel schema, JdbcTemplate jdbcTemplate) {

        String syntaxError = checkSyntax(Comparision, schema, jdbcTemplate);

        if (syntaxError != null && !syntaxError.equals(""))
            return syntaxError;

        String FormatedBase = Utilities.FormatSql(Base);
        String FormatedComparision = Utilities.FormatSql(Comparision);

        if (FormatedBase.equals(FormatedComparision))
            return "The Statement is correct";

        SQLSelectStatement BaseStatement = Utilities.BuildSelectFromStatement(FormatedBase);
        SQLSelectStatement ComparisionStatement = Utilities.BuildSelectFromStatement(FormatedComparision);

        BaseStatement = Utilities.ReplaceWildcards(BaseStatement, schema, jdbcTemplate);
        ComparisionStatement = Utilities.ReplaceWildcards(ComparisionStatement, schema, jdbcTemplate);

        String returns = Utilities.CompareSQL(BaseStatement, ComparisionStatement);

        if (returns.equals("")) {
            //TODO: Implementation for column_order
            return compareResult(Base, Comparision, schema, jdbcTemplate, false);
        }

        return returns;
    }

    public static String checkSQLStatement(String Base, String Comparision) {
        try {

            String FormatedBase = Utilities.FormatSql(Base);
            String FormatedComparision = Utilities.FormatSql(Comparision);

            if (FormatedBase.equals(FormatedComparision))
                return "The Statement is correct";

            SQLSelectStatement BaseStatement = Utilities.BuildSelectFromStatement(FormatedBase);
            SQLSelectStatement ComparisionStatement = Utilities.BuildSelectFromStatement(FormatedComparision);

            String returns = Utilities.CompareSQL(BaseStatement, ComparisionStatement);

            if (returns.equals("")) {
                //TODO: Implementation for column_order
                return "Results correct";
            }

            return returns;
        } catch (EmptyStackException e) {
            return "Error";
        }
    }

    private static String compareResult(String base, String comparision, SchemaModel schema,
                                        JdbcTemplate jdbcTemplate, boolean column_order) {
        boolean orderRelevant = base.contains("order by");

        String baseSql =
                base.replace("commit transaction", "");

        String comparisionSql =
                base.replace("commit transaction", "");

        Connection connection = null;
        String savepointString = Utilities.getAlphaNumericString(15);
        Savepoint sp = null;

        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setSchema(schema.getName());
            connection.setAutoCommit(false);
            sp = connection.setSavepoint(savepointString);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        try {
            Statement baseStm = connection.createStatement();
            Statement comparisionstm = connection.createStatement();

            ResultSet baseRs = baseStm.executeQuery(baseSql);
            ResultSet comparisionRs = comparisionstm.executeQuery(comparisionSql);

            int colCount = baseRs.getMetaData().getColumnCount();

            if (orderRelevant) {
                while (baseRs.next()) {
                    comparisionRs.next();
                    if (column_order) {
                        for (int i = 0; i < colCount; i++) {
                            String baseRes = baseRs.getString(i + 1);
                            String comparisionRes = comparisionRs.getString(i + 1);

                            if (baseRes == null || comparisionRes == null) {
                                if (baseRes != comparisionRes)
                                    return closeAndReturnError(connection, sp);
                            } else if (!baseRes.equals(comparisionRes))
                                return closeAndReturnError(connection, sp);
                        }
                    } else {
                        for (int i = 0; i < colCount; i++) {
                            String colName = baseRs.getMetaData().getColumnLabel(i + 1);
                            String baseRes = baseRs.getString(colName);
                            String comparisionRes = comparisionRs.getString(colName);

                            if (baseRes == null || comparisionRes == null) {
                                if (baseRes != comparisionRes)
                                    return closeAndReturnError(connection, sp);
                            } else if (!baseRes.equals(comparisionRes))
                                return closeAndReturnError(connection, sp);
                        }
                    }
                }
            } else {
                if (column_order) {
                    ArrayList<Map<Integer, String>> baseResults = new ArrayList<Map<Integer, String>>();
                    ArrayList<Map<Integer, String>> comparisionResults = new ArrayList<Map<Integer, String>>();
                    ;

                    while (baseRs.next()) {
                        Map<Integer, String> dict = new HashMap<Integer, String>();

                        for (int i = 0; i < colCount; i++) {
                            dict.put(i, baseRs.getString(i + 1));
                        }

                        baseResults.add(dict);
                    }

                    while (comparisionRs.next()) {
                        Map<Integer, String> dict = new HashMap<Integer, String>();

                        for (int i = 0; i < colCount; i++) {
                            dict.put(i, comparisionRs.getString(i + 1));
                        }

                        comparisionResults.add(dict);
                    }

                    if (baseResults.size() != comparisionResults.size())
                        return closeAndReturnError(connection, sp);

                    for (var baseElem : baseResults) {
                        boolean found = false;

                        for (var compElem : comparisionResults) {
                            boolean completeEquality = true;

                            for (var x : baseElem.keySet()) {
                                if (!compElem.get(x).equals(baseElem.get(x)))
                                    completeEquality = false;
                            }

                            found = completeEquality || found;
                        }

                        if (!found)
                            return closeAndReturnError(connection, sp);
                    }

                } else {
                    ArrayList<Map<String, String>> baseResults = new ArrayList<Map<String, String>>();
                    ArrayList<Map<String, String>> comparisionResults = new ArrayList<Map<String, String>>();

                    while (baseRs.next()) {
                        Map<String, String> dict = new HashMap<String, String>();

                        for (int i = 0; i < colCount; i++) {
                            String colName = baseRs.getMetaData().getColumnLabel(i + 1);
                            dict.put(colName, baseRs.getString(colName));
                        }

                        baseResults.add(dict);
                    }

                    while (comparisionRs.next()) {
                        Map<String, String> dict = new HashMap<String, String>();

                        for (int i = 0; i < colCount; i++) {
                            String colName = comparisionRs.getMetaData().getColumnLabel(i + 1);
                            dict.put(colName, comparisionRs.getString(colName));
                        }

                        comparisionResults.add(dict);
                    }

                    if (baseResults.size() != comparisionResults.size())
                        return closeAndReturnError(connection, sp);


                    for (var baseElem : baseResults) {
                        boolean found = false;

                        for (var compElem : comparisionResults) {
                            boolean completeEquality = true;

                            for (var x : baseElem.keySet()) {

                                if (!Utilities.coalesce(compElem.get(x), "").equals(Utilities.coalesce(baseElem.get(x), "")))
                                    completeEquality = false;
                            }

                            found = completeEquality || found;
                        }

                        if (!found)
                            return closeAndReturnError(connection, sp);
                    }
                }
            }
            connection.rollback(sp);
        } catch (SQLException e) {
            //TODO: error "Die Abfrage lieferte kein Ergebnis.
            return closeAndReturnError(connection, sp);
        }

        return "Results correct";
    }

    private static String closeAndReturnError(Connection connection, Savepoint sp) {
        try {
            connection.rollback(sp);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return "Results not correct";
    }

    static String checkSyntax(String comparision, SchemaModel schema, JdbcTemplate jdbcTemplate) {
        String sql = "SET search_path TO " + schema.getName() + "; " +
                "begin transaction; " +
                comparision.replace("commit transaction", "") +
                "; rollback transaction;";

        try {
            Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement();

            stm.execute(sql);
        } catch (SQLException e) {
            return e.getMessage();
        }


        return null;
    }


}
