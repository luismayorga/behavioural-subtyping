package org.ua.processor;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

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
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
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

	/**Processes a class, extracting its structure to an internal representation 
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
				processHierarchy(clase, dt);
			}
		}
	}

	/** Process both classes of a simple hierarchy
	 * 
	 * @param subClassType Subclass of the hierarchy
	 * @param superClassType Superclass of the hierarchy
	 */
	private void processHierarchy(TypeMirror subClassType, TypeMirror superClassType ){
		Element subClass = typeUtils.asElement(subClassType);
		Element superclass = typeUtils.asElement(superClassType);

		for(Element subClassElement : subClass.getEnclosedElements()){
			for (Element superClassElement : superclass.getEnclosedElements()) {

				if(subClassElement.getModifiers().contains(Modifier.PRIVATE) ||
						superClassElement.getModifiers().contains(Modifier.PRIVATE)){
					continue;
				} else if(superClassElement.getKind().equals(ElementKind.METHOD) &&
						subClassElement.getKind().equals(ElementKind.METHOD)){

					//Same signature
					if(haveSameSignature((ExecutableElement)subClassElement, (ExecutableElement)superClassElement)){
						compareAnnotations((ExecutableElement)subClassElement, (ExecutableElement)superClassElement);
					}

				}else if (superClassElement.getKind().equals(ElementKind.FIELD) &&
						subClassElement.getKind().equals(ElementKind.FIELD)){
					//TODO fill case
				}else{
					continue;
				}
			}
		}
	}

	/**	Check whether the two methods provided have the same signature 
	 * 
	 * @param method1
	 * @param method2
	 * @return true if the methods have the same signature, false otherwise
	 */
	private boolean haveSameSignature(ExecutableElement method1, ExecutableElement method2){

		return  method1.getSimpleName().equals(method2.getSimpleName()) &&
				method1.getReturnType().equals(method2.getReturnType()) &&
				method1.getTypeParameters().equals(method2.getTypeParameters()) &&
				method1.getModifiers().equals(method2.getModifiers()) &&
				typeUtils.isSubsignature((ExecutableType)method1.asType(), (ExecutableType)method2.asType()) &&
				typeUtils.isSubsignature((ExecutableType)method2.asType(), (ExecutableType)method1.asType());
	}

	/** Compare two sets of annotations, using as base the first set.
	 * Annotations of the second list not contained in the first one will not be
	 * analyzed
	 * 
	 * @param lista Set of annotations
	 * @param listb Set of annotations
	 */
	private void compareAnnotations(ExecutableElement method1,
			ExecutableElement method2){

		for (AnnotationMirror subAnn : method1.getAnnotationMirrors()) {
			for (AnnotationMirror supAnn : method2.getAnnotationMirrors()) {
				//Same annotation
				if(typeUtils.isSameType(subAnn.getAnnotationType(),supAnn.getAnnotationType())){
					if(subAnn.getElementValues().size()>1 || supAnn.getElementValues().size()>1){
						//TODO complain about the annotation having more than one value
					}

					Collection<? extends AnnotationValue> valuesA = subAnn.getElementValues().values();
					Collection<? extends AnnotationValue> valuesB = supAnn.getElementValues().values();
					processAnnotation(valuesA.iterator().next(), method1, subAnn);
					processAnnotation(valuesB.iterator().next(), method2, supAnn);
				}
			}
		}
	}

	/**
	 * Converts an annotation in a string in a form that the SMT Solver is able
	 * to understand
	 * 
	 * @param av Annotation string to convert
	 */
	private void processAnnotation(AnnotationValue av, ExecutableElement ee, AnnotationMirror am){
		final String discardRegex = "\\W+";
		String[] identifiers = av.toString()
				.replaceFirst("\\W+", "")
				.split(discardRegex);
		ee.accept(new IdentifierCheckerEV(), new IdentifierCheckerEV.CheckerInfo(identifiers, messager, am));
	}
}
