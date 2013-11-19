package be.ac.ua.visitors;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import be.ac.ua.contracts.Contract;

/**
 * Transform an annotation into a contract.
 * 
 * @author Luis Mayorga
 */
public class ContractProcessorAVV implements AnnotationValueVisitor<Contract, Void> {

	@Override
	public Contract visit(AnnotationValue av, Void p) {
		return null;
	}

	@Override
	public Contract visit(AnnotationValue av) {
		return null;
	}

	@Override
	public Contract visitBoolean(boolean b, Void p) {
		return null;
	}

	@Override
	public Contract visitByte(byte b, Void p) {
		return null;
	}

	@Override
	public Contract visitChar(char c, Void p) {
		return null;
	}

	@Override
	public Contract visitDouble(double d, Void p) {
		return null;
	}

	@Override
	public Contract visitFloat(float f, Void p) {
		return null;
	}

	@Override
	public Contract visitInt(int i, Void p) {
		return null;
	}

	@Override
	public Contract visitLong(long i, Void p) {
		return null;
	}

	@Override
	public Contract visitShort(short s, Void p) {
		return null;
	}

	/**
	 * Creates a contract from a string.
	 * 
	 * @param s The string containing the contract.
	 */
	@Override
	public Contract visitString(String s, Void p) {
		Contract cont = new Contract(s);
		return cont;
	}

	@Override
	public Contract visitType(TypeMirror t, Void p) {
		return null;
	}

	@Override
	public Contract visitEnumConstant(VariableElement c, Void p) {
		return null;
	}

	@Override
	public Contract visitAnnotation(AnnotationMirror a, Void p) {
		return null;
	}

	@Override
	public Contract visitArray(List<? extends AnnotationValue> vals,
			Void p) {
		return null;
	}

	@Override
	public Contract visitUnknown(AnnotationValue av, Void p) {
		return null;
	}

}
