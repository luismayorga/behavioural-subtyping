package org.ua.processor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.ua.processor.IdentifierCheckerEV.CheckerInfo;

@SupportedAnnotationTypes({"org.ua.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

	private Messager messager;
	private Types typeUtils;
	private Elements elementUtils;
	private Set<Element> checked;
	private SMTSolver SMT;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv){
		super.init(processingEnv);
		messager = processingEnv.getMessager();
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
		SMT = SMTSolver.getInstance();
		checked = new HashSet<Element>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {

		if(!roundEnv.processingOver()){
			// Type of annotations present in the analyzed code
			for (TypeElement annotation : annotations) {
				// Elements annotated with them
				for(Element el: roundEnv.getElementsAnnotatedWith(annotation)){

					if (!checked.contains(el.getEnclosingElement())) {
						processClass(el.getEnclosingElement());
					}
				}
			}
		}
		return true;
	}

	/**
	 * Processes a class, extracting its structure to an internal representation 
	 * and stores the contracts within it as well as the relationship with the
	 * other classes.
	 * 
	 * @param element The class to be analyzed
	 */
	private void processClass(Element el){
		checked.add(el);
		TypeMirror clase = el.asType();
		//The element is not a class or an interface
		if (!clase.getKind().equals(TypeKind.DECLARED)) {
			messager.printMessage(Kind.ERROR,
					"The annotated element must be a method", el);
		}else{
			// Check if there is a superclass
			TypeMirror dt = ((TypeElement)el).getSuperclass();
			boolean iguales = typeUtils.isSameType(elementUtils
					.getTypeElement("java.lang.Object").asType(), dt);
			// If does not inherit from Object
			if(!iguales){
				processHierarchy(el, typeUtils.asElement(dt));
			}
		}
	}

	/** 
	 * Process both classes of a simple hierarchy.
	 * 
	 * @param subClassType Subclass of the hierarchy
	 * @param superClassType Superclass of the hierarchy
	 */
	private void processHierarchy(Element subClass, Element superClass ){

		for(Element subClassElement : subClass.getEnclosedElements()){

			for (Element superClassElement : superClass.getEnclosedElements()) {

				if(subClassElement.getModifiers().contains(Modifier.PRIVATE) ||
						superClassElement.getModifiers().contains(Modifier.PRIVATE)){
					continue;
				} else if((superClassElement.getKind().equals(ElementKind.METHOD) 
						&& subClassElement.getKind().equals(ElementKind.METHOD)
						&& typeUtils.isSameType(subClassElement.asType(),
								superClassElement.asType()))
								|| (superClassElement.getKind().equals(ElementKind.FIELD) 
										&& subClassElement.getKind().equals(ElementKind.FIELD) 
										&& subClassElement.getSimpleName()
										.equals(superClassElement.getSimpleName()))){

					checkAnnotations(superClassElement);
					checkAnnotations(subClassElement);
					joinContracts(superClassElement, subClassElement);	
				}else{
					continue;
				}
			}
		}
	}

	/**
	 * Join the annotations in the first element with the same type of annotations
	 * present in the second one. If there is one of the annotations not present in
	 * the second element, the inheritance from the superclass is warned. If there
	 * are annotations of the second element not present in the first one, throws
	 * an error.
	 * 
	 * @param superE Superclass element
	 * @param subE Subclass element
	 * @return A list of contract pairs
	 */
	private List<ContractPair> joinContracts(Element superE, Element subE){

		List<ContractPair> contracts = new ArrayList<ContractPair>();
		Set<DeclaredType> typesSuper = new HashSet<DeclaredType>();
		boolean found = false;

		for ( AnnotationMirror annotation1 : superE.getAnnotationMirrors()) {
			typesSuper.add(annotation1.getAnnotationType());
			for ( AnnotationMirror annotation2 : subE.getAnnotationMirrors()) {

				if(typeUtils.isSameType(annotation1.getAnnotationType(),
						annotation2.getAnnotationType())){
					contracts.add(new ContractPair(annotation1, annotation2));
					found = true;
					break;
				}
			}
			if(!found){
				messager.printMessage(Kind.NOTE, "Inheriting superclass contract",
						subE, annotation1);
			}
		}
		//Get the annotations from the second element that
		//were not in the first one and throw error
		for ( AnnotationMirror annotation2 : subE.getAnnotationMirrors()) {
			if (!typesSuper.contains(annotation2.getAnnotationType())) {
				messager.printMessage(Kind.ERROR, "Subclass element contract more "
						+"restrictive than the corresponding parent",
						subE, annotation2);
			}
		}
		return contracts;
	}

	/**
	 * Checks if the annotations of an element are well formed and
	 * if all the identifiers used on them correspond to the element.
	 * 
	 * @param e The element whose annotations must be checked
	 */
	private void checkAnnotations(Element e){
		ElementVisitor<Void, CheckerInfo> ev;
		CheckerInfo ci;

		for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
			ev = new IdentifierCheckerEV();
			ci = new CheckerInfo(retrieveIdentifiers(
					annotation.getElementValues().values().iterator().next()),
					messager,
					annotation);
			e.accept(ev, ci);
		}
	}

	/**
	 * Extracts the identifiers used within an annotation.
	 * 
	 * @param av The annotation value
	 * @return The array of identifiers
	 */
	private String[] retrieveIdentifiers(AnnotationValue av){
		final String discardRegex = "\\W+";
		return 	av.toString()
				.replaceFirst(discardRegex, "")
				.split(discardRegex);
	}
}
