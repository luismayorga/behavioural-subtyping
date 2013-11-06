package be.ac.ua.processor;

import javax.lang.model.element.AnnotationMirror;

public class ContractPair {
	private AnnotationMirror subContract;
	private AnnotationMirror superContract;
	
	public AnnotationMirror getSubContract() {
		return subContract;
	}

	public AnnotationMirror getSuperContract() {
		return superContract;
	}

	public ContractPair(AnnotationMirror subContract,
			AnnotationMirror superContract) {
		super();
		this.subContract = subContract;
		this.superContract = superContract;
	}
	
}
