package hybridAutomation.elements;

import hybridAutomation.Core.CustomImplement;

import java.util.List;

@CustomImplement
public interface SelectMenu {

    public boolean isOpen();

    public void open();
    public void select(String item);

    public List<String> getItems();

}
