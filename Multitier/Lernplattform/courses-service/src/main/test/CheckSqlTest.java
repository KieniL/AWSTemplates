import at.lernplattform.application.tasks.CreateFeedbackCommand;
import at.lernplattform.domain.usecase.GetFeedback;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CheckSqlTest {




    @Test
    public void validTestSelect() {
        assertEquals("The Statement is correct", GetFeedback.checkSQLStatement("select * from employees", "select * from employees"));
    }
    @Test
    public void validTestSelectAndSelect() {
        assertEquals("The Statement is correct", GetFeedback.checkSQLStatement("  select ProductID, ProductName" +
                "    from products" +
                "    where Discontinued = 'N'", "  select ProductID, ProductName" +
                "    from products" +
                "    where Discontinued = 'N'"));
    }
    @Test
    public void invalidTestWrongSelect() {
        assertNotEquals("The Statement is correct", GetFeedback.checkSQLStatement("  select ProductID, ProductName" +
                "    from products" +
                "    where Discontinued = 'N'", "  select ProductID, Discontinued" +
                "    from products" +
                "    where Discontinued = 'N'"));
    }
    @Test
    public void invalidTestMissingSelect() {
        assertNotEquals("The Statement is correct", GetFeedback.checkSQLStatement("  select ProductID, ProductName" +
                "    from products" +
                "    where Discontinued = 'N'", "  select ProductID" +
                "    from products" +
                "    where Discontinued = 'N'"));
    }
    @Test
    public void invalidTestAdditinalWhereCause() {
        assertNotEquals("The Statement is correct", GetFeedback.checkSQLStatement("  select ProductID, ProductName" +
                "    from products" +
                "    where Discontinued = 'N'", "  select ProductID" +
                "    from products" +
                "    where ReorderLevel >= 30"));
    }

    @Test
    public void validTestWithWrongTableName() {
        assertNotEquals("The Statement is correct", GetFeedback.checkSQLStatement("select * from employees", "select * from empl"));
    }

    @Test
    public void validTestWithJoin() {
        assertEquals("The Statement is correct", GetFeedback.checkSQLStatement("select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID", "select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID"));
    }
    @Test
    public void validTestWithJoinAndWhere() {
        assertEquals("The Statement is correct", GetFeedback.checkSQLStatement("select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where b.Discontinued = 'N'", "select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where b.Discontinued = 'N'"));
    }
    @Test
    public void validTestWithJoinAndWhereAndOrderBy() {
        assertEquals("The Statement is correct", GetFeedback.checkSQLStatement("select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where b.Discontinued = 'N'" + "order by b.ProductName", "select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where b.Discontinued = 'N'" + "order by b.ProductName"));
    }
    @Test
    public void invalidTestWithJoinAndWhereAndOrderByWithWrongWhere() {
        assertNotEquals("The Statement is correct", GetFeedback.checkSQLStatement("select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where b.Discontinued = 'N'" + "order by b.ProductName", "select distinct b.*, a.CategoryName" + "from Categories a " + "join Products b on a.CategoryID = b.CategoryID" + "where a.CategoryName = 'N'" + "order by b.ProductName"));
    }
}
