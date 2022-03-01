package com.glaze.autumn.circulardependency.exception;

import java.util.*;

public class CircularDependencyExceptionHandler {
    private final StringBuilder builder = new StringBuilder();

    public void handleCircularDependencyConflict(Class<?> causedBy, Stack<Class<?>> currentBranch) {
        List<Class<?>> inBetweenClasses = this.getInBetweenClasses(causedBy, currentBranch.stream().toList());

        builder.append("""
                A circular dependency has been found among the following classes.\s
                .______.
                |      |
                """);

        this.addClassesToErrorMessage(inBetweenClasses);
        builder.append("""
        |_______|
        """);
        String errorMessage = builder.toString();
        throw new CircularDependencyCheckException(errorMessage);
    }

    private List<Class<?>> getInBetweenClasses(Class<?> conflictingClass, List<Class<?>> nodes) {
        boolean hasFoundConflictingClass = false;
        List<Class<?>> inBetweenClasses = new ArrayList<>();

        for(Class<?> clazz : nodes) {
            if(!hasFoundConflictingClass && clazz.equals(conflictingClass)) {
                hasFoundConflictingClass = true;
                inBetweenClasses.add(clazz);
            }else{
                inBetweenClasses.add(clazz);
            }
        }

        return inBetweenClasses;
    }

    private void addClassesToErrorMessage(List<Class<?>> inBetweenClasses) {
        for(Class<?> clazz : inBetweenClasses) {
            String toAppend = String.format("""
            |   %s
            |      👇
            """, clazz.getName());
            builder.append(toAppend);
        }
    }

}
