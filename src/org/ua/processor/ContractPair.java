package org.ua.processor;

import javax.lang.model.element.AnnotationMirror;

public class ContractPair {
	AnnotationMirror subContract;
	AnnotationMirror superContract;
	
	public ContractPair(AnnotationMirror subContract,
			AnnotationMirror superContract) {
		super();
		this.subContract = subContract;
		this.superContract = superContract;
	}
	
}
