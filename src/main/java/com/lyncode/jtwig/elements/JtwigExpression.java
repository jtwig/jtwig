package com.lyncode.jtwig.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.expression.JtwigExpressionEvaluator;
import com.lyncode.jtwig.render.Argumentable;
import com.lyncode.jtwig.render.Calculable;

public class JtwigExpression implements Argumentable, Calculable {
	private ExpressionOperator operator;
	public List<Object> arguments;
	
	
	public JtwigExpression () {
		this.arguments = new ArrayList<Object>();
	}
	public JtwigExpression (ExpressionOperator op) {
		this.operator = op;
		this.arguments = new ArrayList<Object>();
	}

	public ExpressionOperator getOperator() {
		return operator;
	}

	public List<Object> getArguments() {
		return arguments;
	}
	
	public boolean setOperator (ExpressionOperator op) {
		this.operator = op;
		return true;
	}

	@Override
	public boolean add(Object obj) {
		arguments.add(obj);
		return true;
	}
	
	@Override
	public Object calculate(HttpServletRequest req, Map<String, Object> values)
			throws JtwigRenderException {
		JtwigExpressionEvaluator eval = new JtwigExpressionEvaluator(values);
		List<Object> calculatedPieces = new ArrayList<Object>();
		for (Object obj : this.arguments)
			calculatedPieces.add(eval.evaluate(req, obj));
		
		if (this.arguments.size() > 1) {
			switch (this.operator) {
				case AND:
					boolean result = true;
					for (Object obj : calculatedPieces)
						result = result && isTrue(obj);
					return result;
				case OR:
					result = false;
					for (Object obj : calculatedPieces) {
						result = result || isTrue(obj);
						if (result) break;
					}
					return result;
				case EQUAL:
					result = true;
					Object obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && equal(obj, calculatedPieces.get(i));
						if (!result) break;
					}
					return result;
				case NOTEQUAL:
					result = true;
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && !equal(obj, calculatedPieces.get(i));
						if (!result) break;
					}
					return result;
				case LE:
					result = true;
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && (lower(obj, calculatedPieces.get(i)) || equal(obj, calculatedPieces.get(i)));
						if (!result) break;
						obj = calculatedPieces.get(i);
					}
					return result;
				case GE:
					result = true;
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && (greater(obj, calculatedPieces.get(i)) || equal(obj, calculatedPieces.get(i)));
						if (!result) break;
						obj = calculatedPieces.get(i);
					}
					return result;
				case GT:
					result = true;
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && (greater(obj, calculatedPieces.get(i)));
						if (!result) break;
						obj = calculatedPieces.get(i);
					}
					return result;
				case LT:
					result = true;
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						result = result && (lower(obj, calculatedPieces.get(i)));
						if (!result) break;
						obj = calculatedPieces.get(i);
					}
					return result;
				case PLUS:
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						obj = plus(obj, calculatedPieces.get(i));
					}
					return obj;
				case DIV:
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						obj = div(obj, calculatedPieces.get(i));
					}
					return obj;
				case MINUS:
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						obj = minus(obj, calculatedPieces.get(i));
					}
					return obj;
				case MOD:
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						obj = mod(obj, calculatedPieces.get(i));
					}
					return obj;
				case STAR:
					obj = calculatedPieces.get(0);
					for (int i=1;i<calculatedPieces.size();i++) {
						obj = star(obj, calculatedPieces.get(i));
					}
					return obj;
				default:
					throw new JtwigRenderException("Unimplemented operation");
			}
		} else {
			if (this.arguments.isEmpty()) throw new JtwigRenderException("Unable to solve empty argumented expression");
			return calculatedPieces.get(0);
		}
	}
	
	public static boolean isTrue (Object obj) {
		if (obj == null) return false;
		else if (obj instanceof Boolean) return (Boolean) obj;
		else if (obj instanceof Collection<?>) return !((Collection<?>) obj).isEmpty();
		else if (obj instanceof Map<?,?>) return !((Map<?,?>) obj).isEmpty();
		else if (obj.getClass().isArray()) return ((Object[]) obj).length > 0;
		else if (obj instanceof Integer) return ((Integer) obj).intValue() != 0;
		else return true;
	}
	
	public static boolean equal (Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) return true;
		else {
			if (obj1 != null) return obj1.equals(obj2);
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean greater (Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null) return false;
		if (obj1 instanceof Comparable<?> && obj1 instanceof Comparable<?>) {
			return (((Comparable<Object>) obj1).compareTo(obj2) > 0);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean lower (Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null) return false;
		if (obj1 instanceof Comparable<?> && obj1 instanceof Comparable<?>) {
			return (((Comparable<Object>) obj1).compareTo(obj2) < 0);
		}
		return false;
	}
	
	public static Object plus (Object obj1, Object obj2) {
		if (obj1 == null) return obj2;
		if (obj2 == null) return obj1;
		if (obj1 instanceof Number && obj2 instanceof Number) {
			Number n1 = (Number) obj1;
			Number n2 = (Number) obj2;
			if (obj1 instanceof Float) return n1.floatValue() + n2.floatValue();
			else if (obj1 instanceof Double) return n1.doubleValue() + n2.doubleValue();
			else if (obj1 instanceof Long) return n1.longValue() + n2.longValue();
			else if (obj1 instanceof Short) return n1.shortValue() + n2.shortValue();
			else if (obj1 instanceof Byte) return n1.byteValue() + n2.byteValue();
			return n1.intValue() + n2.intValue();
		} else {
			return obj1.toString() + obj2.toString();
		}
	}

	public static Object minus (Object obj1, Object obj2) throws JtwigRenderException {
		if (obj1 == null) return obj2;
		if (obj2 == null) return obj1;
		if (obj1 instanceof Number && obj2 instanceof Number) {
			Number n1 = (Number) obj1;
			Number n2 = (Number) obj2;
			if (obj1 instanceof Float) return n1.floatValue() - n2.floatValue();
			else if (obj1 instanceof Double) return n1.doubleValue() - n2.doubleValue();
			else if (obj1 instanceof Long) return n1.longValue() - n2.longValue();
			else if (obj1 instanceof Short) return n1.shortValue() - n2.shortValue();
			else if (obj1 instanceof Byte) return n1.byteValue() - n2.byteValue();
			return n1.intValue() - n2.intValue();
		} throw new JtwigRenderException("Cannot calculate expression");
	}
	public static Object mod (Object obj1, Object obj2) throws JtwigRenderException {
		if (obj1 == null) return obj2;
		if (obj2 == null) return obj1;
		if (obj1 instanceof Number && obj2 instanceof Number) {
			Number n1 = (Number) obj1;
			Number n2 = (Number) obj2;
			if (obj1 instanceof Float) return n1.floatValue() % n2.floatValue();
			else if (obj1 instanceof Double) return n1.doubleValue() % n2.doubleValue();
			else if (obj1 instanceof Long) return n1.longValue() % n2.longValue();
			else if (obj1 instanceof Short) return n1.shortValue() % n2.shortValue();
			else if (obj1 instanceof Byte) return n1.byteValue() % n2.byteValue();
			return n1.intValue() % n2.intValue();
		} throw new JtwigRenderException("Cannot calculate expression");
	}

	public static Object div (Object obj1, Object obj2) throws JtwigRenderException {
		if (obj1 == null) return obj2;
		if (obj2 == null) return obj1;
		if (obj1 instanceof Number && obj2 instanceof Number) {
			Number n1 = (Number) obj1;
			Number n2 = (Number) obj2;
			if (obj1 instanceof Float) return n1.floatValue() / n2.floatValue();
			else if (obj1 instanceof Double) return n1.doubleValue() / n2.doubleValue();
			else if (obj1 instanceof Long) return n1.longValue() / n2.longValue();
			else if (obj1 instanceof Short) return n1.shortValue() / n2.shortValue();
			else if (obj1 instanceof Byte) return n1.byteValue() / n2.byteValue();
			return n1.intValue() / n2.intValue();
		} throw new JtwigRenderException("Cannot calculate expression");
	}


	public static Object star (Object obj1, Object obj2) throws JtwigRenderException {
		if (obj1 == null) return obj2;
		if (obj2 == null) return obj1;
		if (obj1 instanceof Number && obj2 instanceof Number) {
			Number n1 = (Number) obj1;
			Number n2 = (Number) obj2;
			if (obj1 instanceof Float) return n1.floatValue() * n2.floatValue();
			else if (obj1 instanceof Double) return n1.doubleValue() * n2.doubleValue();
			else if (obj1 instanceof Long) return n1.longValue() * n2.longValue();
			else if (obj1 instanceof Short) return n1.shortValue() * n2.shortValue();
			else if (obj1 instanceof Byte) return n1.byteValue() * n2.byteValue();
			return n1.intValue() * n2.intValue();
		} throw new JtwigRenderException("Cannot calculate expression");
	}
}
