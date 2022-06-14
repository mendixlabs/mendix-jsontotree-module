package jsontotree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONObject;
import jsontotree.proxies.JSONNode;

public class Misc {

	/**
	 * Traverse a JSON Tree 
	 * 
	 * @param ctx Context for the current Java Action
	 * @param rootObject Root object that binds all Node objects
	 * @param rootNodes Array/Single Tree object
	 * @param childKey Key that represents the array with child nodes
	 */
	public static void traverseRootObjects(
			IContext ctx, 
			IMendixObject rootObject, 
			JsonNode rootNodes,
			String childKey
	) {
		traverse(rootObject, ctx, rootNodes, childKey, null);
	}

	/**
	 * Root traverse mode, will traverse through an array or single object
	 * 
	 * @param rootObject Root object that binds all Node objects
	 * @param ctx Context for the current Java Action
	 * @param node current node
	 * @param childKey Key that represents the array with child nodes
	 * @param parentMXObject Mendix JSONNode object that is the parent
	 */
	private static void traverse(IMendixObject rootObject, IContext ctx, JsonNode node, String childKey, IMendixObject parentMXObject) {
		if (node.getNodeType() == JsonNodeType.ARRAY) {
			traverseArray(rootObject, ctx, node, childKey, parentMXObject);
		} else if (node.getNodeType() == JsonNodeType.OBJECT) {
			traverseObject(rootObject, ctx, node, childKey, parentMXObject);
		} else {
			throw new com.mendix.systemwideinterfaces.MendixRuntimeException("Problem with traversing JSON, node type not implemented yet: " + node.getNodeType().toString());
		}
	}

	/**
	 * Traverse through single node
	 * 
	 * @param rootObject Root object that binds all Node objects
	 * @param ctx Context for the current Java Action
	 * @param node current node
	 * @param childKey Key that represents the array with child nodes
	 * @param parentMXObject Mendix JSONNode object that is the parent
	 */
	private static void traverseObject(IMendixObject rootObject, IContext ctx, JsonNode node, String childKey, IMendixObject parentMXObject) {
		JsonNode child = node.get(childKey);
		boolean hasChild;
		if (child == null){
			hasChild = false;
		} else {
			hasChild = traversable(child);
		}
		
		JSONObject copy = createJSONObject(node, childKey);
		String jsonContent = copy.toString();
		
		IMendixObject jsonObj = createNodeObject(ctx, rootObject, parentMXObject, jsonContent);

		if (hasChild) {
			traverse(rootObject, ctx, child, childKey, jsonObj);
		}
	}

	/**
	 * Traverse through array of nodes 
	 * 
	 * @param rootObject Root object that binds all Node objects
	 * @param ctx Context for the current Java Action
	 * @param node current node
	 * @param childKey Key that represents the array with child nodes
	 * @param parentMXObject Mendix JSONNode object that is the parent
	 */
	private static void traverseArray(IMendixObject rootObject, IContext ctx, JsonNode node, String childKey, IMendixObject parentMXObject) {
		for (JsonNode jsonArrayNode : node) {
			if (traversable(jsonArrayNode)) {
				traverse(rootObject, ctx, jsonArrayNode, childKey, parentMXObject);
			}
		}
	}

	/**
	 * Check if the node is not empty, not an object (we don't allow that), but an array
	 * 
	 * @param node
	 * @return Is this a traversable array?
	 */
	private static boolean traversable(JsonNode node) {
		return !node.isEmpty()
				&& (node.getNodeType() == JsonNodeType.OBJECT || node.getNodeType() == JsonNodeType.ARRAY);

	}
	
	/**
	 * Create a JSON object that holds all the fields from the node, with the exception of the children
	 * 
	 * @param node
	 * @param childKey
	 * @return
	 */
	private static JSONObject createJSONObject(JsonNode node, String childKey) {
		JSONObject copy = new JSONObject();

		node.fieldNames().forEachRemaining((String fieldName) -> {
			JsonNode childNode = node.get(fieldName);

			if (!fieldName.equalsIgnoreCase(childKey)) {
				Object value = null;
				if (childNode.isTextual()) {
					value = childNode.textValue();
				} else if (childNode.isNumber()) {
					value = childNode.numberValue();
				} else if (childNode.isDouble()) {
					value = childNode.doubleValue();
				} else if (childNode.isLong()) {
					value = childNode.asLong();
				} else if (childNode.isBoolean()) {
					value = childNode.asBoolean();
				} else {
					value = childNode.asText();
				}

				copy.put(fieldName, value);
			}
		});
		
		return copy;
	}
	
	/**
	 * Create a Mendix JSONNode object
	 * 
	 * @param ctx
	 * @param rootObject
	 * @param parentMXObject
	 * @param jsonContent
	 * @return
	 */
	private static IMendixObject createNodeObject(IContext ctx, IMendixObject rootObject, IMendixObject parentMXObject, String jsonContent) {
		IMendixObject JSONRepresentationObject = Core.instantiate(ctx, JSONNode.getType());

		JSONRepresentationObject.setValue(ctx, JSONNode.MemberNames.Content.toString(),
				jsonContent);
		JSONRepresentationObject.setValue(ctx, JSONNode.MemberNames.JSONNode_Root.toString(),
				rootObject.getId());
		
		if (parentMXObject != null) {
			JSONRepresentationObject.setValue(ctx, JSONNode.MemberNames.Parent.toString(), parentMXObject.getId());
		}

		try {
			Core.commit(ctx, JSONRepresentationObject);
		} catch (CoreException e) {
			throw new com.mendix.systemwideinterfaces.MendixRuntimeException("Issue with committing temporary JSON Object");
		}
		
		return JSONRepresentationObject;
	}

}
