package org.ua.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.ua.processor.JavaExample.TestFailedException;

import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

@SupportedAnnotationTypes({"org.ua.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationProcessor extends AbstractProcessor {

	@Override
	public void init(ProcessingEnvironment processingEnv){
		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
		for (TypeElement typeElement : typeElements) {
			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.NOTE, "warning!!",typeElement.getEnclosingElement());
		}
		return true;
	}

}
