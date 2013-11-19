package be.ac.ua.visitors;

import java.lang.reflect.Modifier;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import be.ac.ua.contracts.ContractList;
import be.ac.ua.contracts.ContractPair;
import be.ac.ua.processor.AnnotationProcessor;
/**
 * Operation for an Element. Processes a class and if this inherits from a 
 * superclass, matches the contracts from all the elements of the superclass to
 * the contracts of the overriding fields or methods and check for the latter
 * being more restrictive than the first.
 * 
 * @author Luis Mayorga
 */
public class ClassProcessorEV implements ElementVisitor<Void, Void> {

	@Override
	public Void visit(Element e, Void p) {
		//Get superclass
		TypeMirror dt = ((TypeElement)e).getSuperclass();
		boolean same = AnnotationProcessor.getTypeUtils()
				.isSameType(AnnotationProcessor.getElementUtils()
						.getTypeElement("java.lang.Object").asType(), dt);
		// If does not inherit from Object
		if(!same){
			Element superElement = AnnotationProcessor.getTypeUtils().asElement(dt);
			//Compare elements
			for(Element subClassElement : e.getEnclosedElements()){
				for (Element superClassElement : superElement.getEnclosedElements()) {
					//If its a method should not be checked
					if(subClassElement.getModifiers().contains(Modifier.PRIVATE) 
							|| superClassElement.getModifiers().contains(Modifier.PRIVATE)){
						continue;
					// If is the same type of element and has the same signature
					} else if(subClassElement.getSimpleName().equals(superClassElement.getSimpleName()) && 
							((superClassElement.getKind().equals(ElementKind.METHOD) 
							&& subClassElement.getKind().equals(ElementKind.METHOD)
							&& AnnotationProcessor.getTypeUtils().isSameType(subClassElement.asType(),
									superClassElement.asType()))
							|| (superClassElement.getKind().equals(ElementKind.FIELD) 
							&& subClassElement.getKind().equals(ElementKind.FIELD)))){
						
						//Get annotations from superclass and subclass
						ContractList superClassContracts = superClassElement
								.accept(new AnnotationsParserEV(), null);
						ContractList subClassContracts = subClassElement
								.accept(new AnnotationsParserEV(), null);
						
						//Join contracts based on their type
						List<ContractPair> join = superClassContracts.join(subClassContracts);
						//Compare all contracts
						for (ContractPair contractPair : join) {
							contractPair.compare();
						}
					}else{
						//Not an overriding method
						continue;
					}
				}
			}
		}
		return null;
	}

	@Override
	public Void visit(Element e) {
		return visit(e, null);
	}

	@Override
	public Void visitPackage(PackageElement e, Void p) {
		return null;
	}

	@Override
	public Void visitType(TypeElement e, Void p) {
		return visit(e, null);
	}

	@Override
	public Void visitVariable(VariableElement e, Void p) {
		return null;
	}

	@Override
	public Void visitExecutable(ExecutableElement e, Void p) {
		return null;
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
