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

            // Проверяем деление на 0
            if (Double.isInfinite(result)) {
                return "Ошибка. Деление на 0";
            }

            return String.valueOf(result);
        } catch (ArithmeticException e) {
            return "Ошибка. Деление на 0";
        } catch (UnknownFunctionOrVariableException e) {
            return "Ошибка в выражении: " + expression;
        } catch (Exception e) {
            return "Ошибка при обработке: " + expression;
        }
    }
}
