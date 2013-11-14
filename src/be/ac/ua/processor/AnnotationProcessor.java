package be.ac.ua.processor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import be.ac.ua.visitors.ClassProcessorEV;

@SupportedAnnotationTypes({"be.ac.ua.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends AbstractProcessor {

	private static Messager messager;
	private static Types typeUtils;
	private static Elements elementUtils;
	private Set<Element> checked;
//	private SMTSolverWrapper SMTsw;

	public static Messager getMessager() {
		return messager;
	}

	public static Types getTypeUtils() {
		return typeUtils;
	}

	public static Elements getElementUtils() {
		return elementUtils;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv){
		super.init(processingEnv);
		messager = processingEnv.getMessager();
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
//		SMTsw = SMTSolverWrapper.getInstance();
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
						checked.add(el.getEnclosingElement());
						el.getEnclosingElement()
						.accept(new ClassProcessorEV(), null);
					}
				}
			}
		}
		return true;
	}
}
