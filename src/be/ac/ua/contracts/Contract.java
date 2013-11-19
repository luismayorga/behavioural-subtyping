package be.ac.ua.contracts;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Contract retrieved from the source code
 * 
 * @author Luis Mayorga
 */
public class Contract {
	private String content;
	private AnnotationMirror am;
	private Element em;

	public String getContent() {
		return content;
	}

	public AnnotationMirror getAm() {
		return am;
	}

	public void setAm(AnnotationMirror am) {
		this.am = am;
	}

	public Element getEm() {
		return em;
	}

	public void setEm(Element em) {
		this.em = em;
	}

	public Contract(String content) {
		super();
		this.content = content;
	}

	public boolean equals(Contract c){
		return this.content.equals(c.content) 
				&& this.am.equals(c.am) 
				&& this.em.equals(c.em);
	}

	public String toString(){
		return "Contract: " + super.toString() + "\n" +
				"Annotation: " + am.getAnnotationType().toString() + "\n" +
				"Element: " + em.getSimpleName().toString();
	}

	/**
	 * Join the contract with a provided contract from the subclass. 
	 * 
	 * @param subClassContract The contract from the subclass.
	 * @return The pair of contracts resulting.
	 */
	public ContractPair join(Contract subClassContract){
		return new ContractPair(this, subClassContract);
	}

	/**
	 * Get all identifiers related to a contract. These identifiers are 
	 * only searched in the method signature or the field.
	 * 
	 * @return A list containing the name of the parameters if it is a method or
	 * the name of the field in other cases.
	 */
	public List<String> getIdentifiers(){
		ArrayList<String> temp = new ArrayList<String>();
		
		if(em.getKind().equals(ElementKind.FIELD)){
			temp.add(em.getSimpleName().toString());
			
		}else if(em.getKind().equals(ElementKind.METHOD)){
			ExecutableElement ee = (ExecutableElement) em;
			for (VariableElement ve : ee.getParameters()) {
				temp.add(ve.getSimpleName().toString());
			}
		}
		return temp;
	}

}
