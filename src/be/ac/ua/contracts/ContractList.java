package be.ac.ua.contracts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import be.ac.ua.processor.AnnotationProcessor;

/**
 * Implementation of a list of contracts.
 * 
 * @author Luis Mayorga
 */
public class ContractList implements List<Contract>{

	private ArrayList<Contract> list;

	public ContractList() {
		super();
		list = new ArrayList<Contract>();
	}

	/**
	 * Joins a list of contracts with another joining the contracts by their
	 * type.
	 * 
	 * @param subList Annotations present on the overriding element of the subclass
	 * @return A list containing pairs of the same type of contract.
	 */
	public List<ContractPair> join(ContractList subList){
		
		ArrayList<ContractPair> temp = new ArrayList<ContractPair>();
		Set<DeclaredType> set = new HashSet<DeclaredType>();
		boolean found;
		
		for (Contract contract : list) {
			found = false;
			for (Contract con : subList) {
				if(AnnotationProcessor.getTypeUtils().isSameType(
						contract.getAm().getAnnotationType(), 
						con.getAm().getAnnotationType())){
					temp.add(contract.join(con));
					set.add(contract.getAm().getAnnotationType());
					found = true;
					break;
				}
			}
			// That type of contract is not attached to the element of the subclass
			// Being inherited
			if(!found){
				AnnotationProcessor.getMessager().printMessage(Kind.NOTE, 
						"Contract not defined for subclass", contract.getEm(), contract.getAm());
			}
		}
		// Contracts that override none from the superclass.
		// Error due to the contract being more restrictive
		for (Contract con : subList) {
			if(!set.contains(con.getAm().getAnnotationType())){
				AnnotationProcessor.getMessager().printMessage(Kind.ERROR, 
						"Contract not defined in superclass", con.getEm(), con.getAm());
			}
		}
		return temp;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<Contract> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(Contract e) {
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Contract> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Contract> c) {
		return list.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public Contract get(int index) {
		return list.get(index);
	}

	@Override
	public Contract set(int index, Contract element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, Contract element) {
		list.add(index, element);
	}

	@Override
	public Contract remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Contract> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Contract> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<Contract> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
