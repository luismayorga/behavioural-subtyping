package be.ac.ua.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.parboiled.trees.GraphNode;

/**
 * Generic abstract syntax tree (AST) node; can hold a type name, attributes and child nodes
 * @author Tim Molderez
 */
public class AstNode implements GraphNode<AstNode> {

	private String type="";
	private HashMap<String, String> attributes=new HashMap<String, String>();
	private ArrayList<String> flags=new ArrayList<String>();
	private ArrayList<AstNode> children = new ArrayList<AstNode>();
	private ArrayList<String> childLabels = new ArrayList<String>();
	private int id=-1; // Nodes are given unique ID numbers when they're exported to a graph

	/**
	 * Default constructor
	 */
	public AstNode() {
		super();
	}
	
	/**
	 * Constructor
	 * @param type		this node's type
	 */
	public AstNode(String type) {
		super();
		this.type=type;
	}
	
	/**
	 * Retrieve the ID number of this node
	 * @return			the ID number
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set the ID number of this node
	 * @param id		the ID number (must be a positive integer)
	 */
	public void setId(int id) {
		this.id=id;
	}
	
	/**
	 * Is the ID of this node set?
	 * @return			true if the ID has been set; false otherwise
	 */
	public boolean idIsSet() {
		return id!=-1;
	}
	
	/**
	 * Retrieve this node's type
	 * @return			the node's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set this node's type
	 * @param type		the node's type
	 * @return			true
	 */
	public boolean setType(String type) {
		this.type = type;
		return true;
	}

	/**
	 * Retrieve an attribute
	 * @param key		the attribute's name
	 * @return			the attribute's value
	 */
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	/**
	 * Remove an attribute
	 * @param key		the attribute's name
	 */
	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	/**
	 * Add an attribute to this node
	 * @param key		the attribute's name
	 * @param value		the attribute's value
	 * @return			true
	 */
	public boolean addAttribute(String key, String value) {
		this.attributes.put(key, "string:\"" + value + "\"");
		return true;
	}
	
	/**
	 * Adds a flag to this node
	 * @param flag		flag name
	 * @return			true
	 */
	public boolean addFlag(String flag) {
		this.flags.add(flag);
		return true;
	}
	
	/**
	 * Removes a flag from this node
	 * @param flag		flag name
	 */
	public void removeFlag(String flag) {
		this.flags.remove(flag);
	}
	
	/**
	 * Add an attribute to this node
	 * @param key		the attribute's name
	 * @param value		the attribute's value
	 * @param type		the type of the attribute ("string", "int", "bool", ...)
	 * @return			true
	 */
	public boolean addAttribute(String key, String value, String type) {
		if (type.equals("string")) {
			value= "\"" + value + "\"";
		}
		this.attributes.put(key, type + ":" + value);
		return true;
	}
	
	/**
	 * Add a child to this node
	 * @param label		the label of the edge going from this node towards the child
	 * @param child		the child node
	 * @return			true
	 */
	public boolean addChild(String label, AstNode child) {
		childLabels.add(label);
		children.add(child);
		return true;
	}
	
	/**
	 * Remove a child from this node
	 * @param i			the index of the child to be removed
	 */
	public void removeChild(int i) {
		children.remove(i);
		childLabels.remove(i);
	}

	public String toString() {
		String result="";
		result+=type;
		Iterator<Entry<String, String>> it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> pair = it.next();
			result += " " + pair.getKey() + ": " + pair.getValue();
		}
		return result;
	}

	public List<AstNode> getChildren() {
		return children;
	}
	
	/**
	 * Retrieve the labels going from this node towards its children
	 * @return			a list of all child labels (in the same order as the list of child nodes)
	 */
	public List<String> getChildLabels() {
		return childLabels;
	}
	
	/**
	 * Retrieve all attributes
	 * @return			all of this node's attributes (a map where each attribute name is mapped to its value)
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	/**
	 * Retrieve all flags
	 * @return			all of this node's flags
	 */
	public List<String> getFlags() {
		return flags;
	}
}