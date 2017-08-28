package org.cqframework.cql.tools.formatter;

import org.cqframework.cql.cql2elm.Cql2ElmVisitor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.cqframework.cql.tools.formatter.CqlFormatterVisitor.*;

/**
 * Created by Christopher on 7/20/2017.
 */
public class CqlFormatterVisitorTest {

    boolean inError = false;

    private void runTest(String fileName) throws IOException {
        String input = getInputStreamAsString(getInput(fileName));
        FormatResult result = getFormattedOutput(getInput(fileName));
        inError = result.errors.size() > 0 ? true : false;
        Assert.assertTrue(inputMatchesOutput(input, result.output));
    }

    @Test
    public void TestFormatterSpecific() throws IOException {
        runTest("comments.cql");
        try {
            // this test has an extra "`", which is ignored - causing the input to differ from the output.
            runTest("git-issue-206-a.cql");
        } catch (AssertionError ae) {
            Assert.assertFalse(inError);
        }
        try {
            // this test has an extra """, which is not ignored - causing a syntax error.
            runTest("git-issue-206-b.cql");
        } catch (AssertionError ae) {
            Assert.assertTrue(inError);
        }
        runTest("git-issue-210-a.cql");
        Assert.assertFalse(inError);
        runTest("git-issue-210-b.cql");
        Assert.assertFalse(inError);
        runTest("git-issue-210-c.cql");
        Assert.assertFalse(inError);
        runTest("comment-after.cql");
        Assert.assertFalse(inError);
        runTest("comment-before.cql");
        Assert.assertFalse(inError);
        runTest("comment-first.cql");
        Assert.assertFalse(inError);
        runTest("comment-in-clause.cql");
        Assert.assertFalse(inError);
        runTest("comment-last.cql");
        Assert.assertFalse(inError);
        try {
            runTest("invalid-syntax.cql");
        } catch (AssertionError ae) {
            Assert.assertTrue(inError);
        }
    }

    @Test
    public void RunCql2ElmRegressionTestSuite() throws IOException {
        runTest("CMS146v2_Test_CQM.cql");
        runTest("CodeAndConceptTest.cql");
        runTest("DateTimeLiteralTest.cql");
        runTest("EscapeSequenceTests.cql");
        runTest("InTest.cql");
        runTest("ParameterTest.cql");
        runTest("PropertyTest.cql");
        runTest("SignatureResolutionTest.cql");
        runTest("TranslationTests.cql");
        runTest("LibraryTests/BaseLibrary.cql");
        runTest("LibraryTests/DuplicateExpressionLibrary.cql");
        runTest("LibraryTests/FHIRHelpers-1.8.cql");
        runTest("LibraryTests/InvalidLibraryReference.cql");
        runTest("LibraryTests/InvalidReferencingLibrary.cql");
        runTest("LibraryTests/MissingLibrary.cql");
        runTest("LibraryTests/ReferencingLibrary.cql");
        runTest("ModelTests/ModelTest.cql");
        runTest("OperatorTests/AggregateOperators.cql");
        runTest("OperatorTests/ArithmeticOperators.cql");
        runTest("OperatorTests/ComparisonOperators.cql");
        runTest("OperatorTests/CqlComparisonOperators.cql");
        runTest("OperatorTests/CqlIntervalOperators.cql");
        runTest("OperatorTests/CqlListOperators.cql");
        runTest("OperatorTests/DateTimeOperators.cql");
        runTest("OperatorTests/ForwardReferences.cql");
        runTest("OperatorTests/Functions.cql");
        runTest("OperatorTests/ImplicitConversions.cql");
        runTest("OperatorTests/IntervalOperatorPhrases.cql");
        runTest("OperatorTests/IntervalOperators.cql");
        runTest("OperatorTests/InvalidCastExpression.cql");
        runTest("OperatorTests/InvalidSortClauses.cql");
        runTest("OperatorTests/ListOperators.cql");
        runTest("OperatorTests/LogicalOperators.cql");
        runTest("OperatorTests/MessageOperators.cql");
        runTest("OperatorTests/MultiSourceQuery.cql");
        runTest("OperatorTests/NameHiding.cql");
        runTest("OperatorTests/NullologicalOperators.cql");
        runTest("OperatorTests/RecursiveFunctions.cql");
        runTest("OperatorTests/Sorting.cql");
        runTest("OperatorTests/StringOperators.cql");
        runTest("OperatorTests/TimeOperators.cql");
        runTest("OperatorTests/TupleAndClassConversions.cql");
        runTest("OperatorTests/TypeOperators.cql");
        runTest("OperatorTests/UndeclaredForward.cql");
        runTest("OperatorTests/UndeclaredSignature.cql");
        runTest("PathTests/PathTests.cql");
    }

    private boolean inputMatchesOutput(String input, String output) {
        return input.replaceAll("\\s", "").equals(output.replaceAll("\\s", ""));
    }

    private InputStream getInput(String fileName) {
        InputStream is = Cql2ElmVisitor.class.getResourceAsStream(fileName);

        if (is == null) {
            is = CqlFormatterVisitorTest.class.getResourceAsStream(fileName);

            if (is == null) {
                throw new IllegalArgumentException(String.format("Invalid test resource: %s not in %s or %s", fileName, Cql2ElmVisitor.class.getSimpleName(), CqlFormatterVisitor.class.getSimpleName()));
            }
        }

        return is;
    }
}
