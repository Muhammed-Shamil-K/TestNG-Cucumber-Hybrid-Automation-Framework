package hybridAutomation.elements;

import java.util.Map;

public interface Table {
    public void initTableElements();

    public Map<String,String> getRowData(int rowNumber);

    public Map<String,String> getRowData(String columnName, String rowMatchPattern);

    public String getCellData(int rowNumber, String columnMatchPattern);

    public String getCellData(String columnName, String rowMatchPattern, String columnMatchPattern);

    public void editRow(int rowNumber);

    public void editRow(String columnName, String rowMatchPattern);

    public void clickOnCell(int rowNumber, String columnMatchPattern);

    public void clickOnCell(String columnName, String rowMatchPattern, String columnMatchPattern);

    public void filterTableData(String filterLabel, String filterOption);

    public void search(String input);

    public void clearSearch();
}
