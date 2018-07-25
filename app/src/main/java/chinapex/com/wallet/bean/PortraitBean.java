package chinapex.com.wallet.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SteelCabbage on 2018/7/25 0025 15:43.
 * E-Mail：liuyi_61@163.com
 */

public class PortraitBean {
    /**
     * type : 1
     * resource : 1
     * title : 学历
     * data : [{"name":"初中","id":"0144","type":"666"},{"name":"高中","id":"0322"},{"name":"大学",
     * "id":"0433"}]
     */

    private int type;
    private int resource;
    private String title;
    private List<HashMap<String, String>> data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HashMap<String, String>> getData() {
        return data;
    }

    public void setData(List<HashMap<String, String>> data) {
        this.data = data;
    }
}
