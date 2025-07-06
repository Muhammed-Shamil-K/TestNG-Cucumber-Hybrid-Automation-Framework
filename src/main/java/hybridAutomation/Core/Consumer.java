package hybridAutomation.Core;

@FunctionalInterface
public interface Consumer<T> {
    T consume() throws Exception;
}
