package org.ua.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

public class IdentifierCheckerEV implements ElementVisitor<Void, IdentifierCheckerEV.CheckerInfo> {

	static class CheckerInfo{
		String[] identifiers;
		Messager msgr;
		AnnotationMirror am;
		public CheckerInfo(String[] identifiers, Messager msgr, AnnotationMirror am) {
			super();
			this.identifiers = identifiers;
			this.msgr = msgr;
			this.am = am;
		}
	}

	@Override
	public Void visit(Element e, CheckerInfo p) {
		return null;
	}

	@Override
	public Void visit(Element e) {
		return null;
	}

	@Override
	public Void visitPackage(PackageElement e, CheckerInfo p) {
		return null;
	}

	@Override
	public Void visitType(TypeElement e, CheckerInfo p) {
		return null;
	}

	@Override
	public Void visitVariable(VariableElement e, CheckerInfo p) {
		boolean found = false;
		for (String identifier : p.identifiers) {
			if(identifier.length()>0 && !Character.isDigit(identifier.charAt(0))){
				found = identifier.equals(e.getSimpleName().toString());
				if(!found){
					p.msgr.printMessage(Kind.ERROR, "The identifier '" +
							identifier  + 
							"' is not a valid method parameter", e, p.am);
				}
			}
		}
		return null;
	}

	@Override
	public Void visitExecutable(ExecutableElement e, CheckerInfo p) {
		boolean found = false;
		for (String identifier : p.identifiers) {
			if(identifier.length()>0 && !Character.isDigit(identifier.charAt(0))){
				found = false;
				for(VariableElement param : e.getParameters()){
					if(param.getSimpleName().toString().equals(identifier)){
						found = true;
						break;
					}
				}
				if(!found){
					p.msgr.printMessage(Kind.ERROR, "The identifier '" +
							identifier  + 
							"' is not a valid method parameter", e, p.am);
				}
			}
		}
		return null;
	}

	@Override
	public Void visitTypeParameter(TypeParameterElement e, CheckerInfo p) {
		return null;
	}

	@Override
	public Void visitUnknown(Element e, CheckerInfo p) {
		return null;
	}

}