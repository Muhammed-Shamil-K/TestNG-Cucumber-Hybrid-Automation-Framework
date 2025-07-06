package hybridAutomation.Core;

public final class CustomElementProcessor {
    private CustomElementProcessor() {

    }

    /**
     *  Gets the wrapper class ( descended from TestElementImpl) for the annotation @CustomImplement.
     *
     * @param interFace interface to process the annotations
     * @param <T> type of the wrapped class
     * @return The class name of the class in question
     *
     */
    public static <T> Class<?> getWrapperClass(Class<T> interFace) {

        if (interFace.isAnnotationPresent(CustomImplement.class)) {
            CustomImplement annotation = interFace.getAnnotation(CustomImplement.class);
            return annotation.value();
        }
        throw new UnsupportedOperationException("Apply @CustomImplement to interface " + interFace.getCanonicalName());

    }
}
