package be.ac.ua.visitors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import be.ac.ua.contracts.Contract;
import be.ac.ua.contracts.ContractList;

/**
 * Class that adds to an Element the functionality
 * of retrieving the contracts within it.
 * 
 * @author Luis Mayorga
 */
public class AnnotationsParserEV implements ElementVisitor<ContractList, Void> {

	/**
	 * Return the contracts present in the element.
	 * 
	 * @param e Element containing the annotations.
	 */
	@Override
	public ContractList visit(Element e, Void p) {
		ContractList cons = new ContractList();
		ContractProcessorAVV Parser;
		for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
				Parser = new ContractProcessorAVV();
				Contract cont = annotation.getElementValues().values().iterator().next()
				.accept(Parser, null);
				cont.setAm(annotation);
				cont.setEm(e);
				cons.add(cont);
		}
		return cons;
	}

	@Override
	public ContractList visit(Element e) {
		return visit(e,null);
	}

	@Override
	public ContractList visitPackage(PackageElement e, Void p) {
		return null;
	}

	@Override
	public ContractList visitType(TypeElement e, Void p) {
		return null;
	}

	@Override
	public ContractList visitVariable(VariableElement e, Void p) {
		return visit(e,null);
	}

	@Override
	public ContractList visitExecutable(ExecutableElement e, Void p) {
		return visit(e,null);
	}

	@Override
	public ContractList visitTypeParameter(TypeParameterElement e, Void p) {
		return null;
	}

	@Override
	public ContractList visitUnknown(Element e, Void p) {
		return null;
	}

}
