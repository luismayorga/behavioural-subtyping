package be.ac.ua.visitors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

/**
 * Class that implements an ElementVisitor that adds the functionality
 * of checking if the annotations are well formed and
 * if all the identifiers used on them correspond to the element.
 */
public class AnnotationsParserEV implements ElementVisitor<Void, Void> {

	@Override
	public Void visit(Element e, Void p) {
		ContractParserAVV Parser;
		for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
			Parser = new ContractParserAVV();
			if(annotation.getElementValues().size()>1){
				//TODO throw error
			} else {
				annotation.getElementValues().values().iterator().next()
				.accept(Parser, null);
			}
		}
		return null;
	}

	@Override
	public Void visit(Element e) {
		return visit(e,null);
	}

	@Override
	public Void visitPackage(PackageElement e, Void p) {
		return null;
	}

	@Override
	public Void visitType(TypeElement e, Void p) {
		return null;
	}

	@Override
	public Void visitVariable(VariableElement e, Void p) {
		return visit(e,null);
	}

	@Override
	public Void visitExecutable(ExecutableElement e, Void p) {
		return visit(e,null);
	}

	@Override
	public Void visitTypeParameter(TypeParameterElement e, Void p) {
		return null;
	}

	@Override
	public Void visitUnknown(Element e, Void p) {
		return null;
	}

}
