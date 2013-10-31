package org.ua.processor;

import java.util.Arrays;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.naming.OperationNotSupportedException;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"org.ua.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

	private Messager messager;
	private Types typeUtils;
	private Elements elementUtils;
	private Set<Element> checked;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv){
		super.init(processingEnv);
		messager = processingEnv.getMessager();
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
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
					if (!checked.contains(el)) {
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
			DeclaredType dt = (DeclaredType)((TypeElement)el).getSuperclass();
			boolean iguales = typeUtils.isSameType(elementUtils
					.getTypeElement("java.lang.Object").asType(), dt);
			// If does not inherit from Object
			if(!iguales){
				try {
					processHierarchy(el, typeUtils.asElement(dt));
				} catch (OperationNotSupportedException e) {
					messager.printMessage(Kind.ERROR,
							"The annotated element must be a method", el);
				}
			}
		}
	}

	/**
	 * 
	 * @param subClass Subclass of the hierarchy
	 * @param superClass Superclass of the hierarchy
	 */
	private void processHierarchy(Element subClass, Element superClass ) throws OperationNotSupportedException{
		if(!subClass.getKind().equals(ElementKind.CLASS) || !superClass.getKind().equals(ElementKind.CLASS));
		TypeElement subC = (TypeElement) subClass;
		TypeElement superC = (TypeElement) superClass;
		List<ExecutableElement> subMethods = (List<ExecutableElement>) subC.getEnclosedElements();
		List<ExecutableElement> superMethods = (List<ExecutableElement>) superC.getEnclosedElements();
		//Delete private methods		
		
		System.out.println(Arrays.toString(subMethods.toArray()));
		System.out.println(Arrays.toString(superMethods.toArray()));
	}
}
