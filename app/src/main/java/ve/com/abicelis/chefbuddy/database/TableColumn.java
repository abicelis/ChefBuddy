package ve.com.abicelis.chefbuddy.database;

/**
 * Created by abicelis on 3/7/2017.
 */
public class TableColumn {

    private DataType datatype;
    private String name;

    public TableColumn(DataType datatype, String name) {
        this.datatype = datatype;
        this.name = name;
    }

    public DataType getDataType() {
        return datatype;
    }

    public String getName() {
        return name;
    }

}
