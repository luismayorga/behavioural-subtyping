package be.ac.ua.visitors;

import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.parboiled.Parboiled;
import org.parboiled.support.ParsingResult;
import org.parboiled.parserunners.ReportingParseRunner;

import be.ac.ua.parser.ContractParser;

public class ContractParserAVV implements AnnotationValueVisitor<ParsingResult<?>, Void> {

	@Override
	public ParsingResult<?> visit(AnnotationValue av, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visit(AnnotationValue av) {
		return null;
	}

	@Override
	public ParsingResult<?> visitBoolean(boolean b, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitByte(byte b, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitChar(char c, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitDouble(double d, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitFloat(float f, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitInt(int i, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitLong(long i, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitShort(short s, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitString(String s, Void p) {
		ContractParser parser = Parboiled.createParser(ContractParser.class);
		ReportingParseRunner<?> rpr = new ReportingParseRunner<Object>(parser.Contract());
		ParsingResult<?> result = rpr.run(s);
		//TODO Process parsing result
		Arrays.toString(result.parseErrors.toArray());
		return result;
	}

	@Override
	public ParsingResult<?> visitType(TypeMirror t, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitEnumConstant(VariableElement c, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitAnnotation(AnnotationMirror a, Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitArray(List<? extends AnnotationValue> vals,
			Void p) {
		return null;
	}

	@Override
	public ParsingResult<?> visitUnknown(AnnotationValue av, Void p) {
		return null;
	}

}
