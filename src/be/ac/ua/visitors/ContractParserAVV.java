package be.ac.ua.visitors;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ContractParserAVV implements AnnotationValueVisitor<Void, Void> {

	@Override
	public Void visit(AnnotationValue av, Void p) {
		return null;
	}

	@Override
	public Void visit(AnnotationValue av) {
		return null;
	}

	@Override
	public Void visitBoolean(boolean b, Void p) {
		return null;
	}

	@Override
	public Void visitByte(byte b, Void p) {
		return null;
	}

	@Override
	public Void visitChar(char c, Void p) {
		return null;
	}

	@Override
	public Void visitDouble(double d, Void p) {
		return null;
	}

	@Override
	public Void visitFloat(float f, Void p) {
		return null;
	}

	@Override
	public Void visitInt(int i, Void p) {
		return null;
	}

	@Override
	public Void visitLong(long i, Void p) {
		return null;
	}

	@Override
	public Void visitShort(short s, Void p) {
		return null;
	}

	@Override
	public Void visitString(String s, Void p) {
		System.out.println("String to parse: " + s);
		return null;
	}

	@Override
	public Void visitType(TypeMirror t, Void p) {
		return null;
	}

	@Override
	public Void visitEnumConstant(VariableElement c, Void p) {
		return null;
	}

	@Override
	public Void visitAnnotation(AnnotationMirror a, Void p) {
		return null;
	}

	@Override
	public Void visitArray(List<? extends AnnotationValue> vals,
			Void p) {
		return null;
	}

	@Override
	public Void visitUnknown(AnnotationValue av, Void p) {
		return null;
	}

}
