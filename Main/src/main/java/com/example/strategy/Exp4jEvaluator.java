package com.example.strategy;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class Exp4jEvaluator implements ExpressionEvaluator {
    @Override
    public String evaluate(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            double result = exp.evaluate();

            if (Double.isInfinite(result)) {
                return "Ошибка. Деление на 0";
            }

            return String.valueOf(result);
        } catch (ArithmeticException e) {
            return "Ошибка. Деление на 0";
        }
    }
}
