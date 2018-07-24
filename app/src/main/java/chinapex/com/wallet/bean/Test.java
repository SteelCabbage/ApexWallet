//package chinapex.com.wallet.bean;
//
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by SteelCabbage on 2018/7/24 0024 9:12.
// * E-Mail：liuyi_61@163.com
// */
//
//public class Test {
//
//
//    /**
//     * code : 200
//     * result : [{"type":1,"resource":1,"title":"学历","data":[{"name":"初中","id":"0144",
//     * "type":"666"},{"name":"高中","id":"0322"},{"name":"大学","id":"0433"}]},{"type":0,
//     * "resource":0,"title":"年收入","data":[]},{"type":1,"resource":1,"title":"移动运营商",
//     * "data":[{"name":"电信","id":14},{"name":"联通","id":32},{"name":"移动","id":43},{"name":"其他",
//     * "id":11}]},{"type":4,"resource":1,"title":"用户商业兴趣","data":[{"name":"旅游","id":0},
//     * {"name":"旅游","id":0},{"name":"旅游","id":0},{"name":"旅游","id":0}]}]
//     */
//
//    private int code;
//    private List<ResultBean> result;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public List<ResultBean> getResult() {
//        return result;
//    }
//
//    public void setResult(List<ResultBean> result) {
//        this.result = result;
//    }
//
//    public static class ResultBean {
//        /**
//         * type : 1
//         * resource : 1
//         * title : 学历
//         * data : [{"name":"初中","id":"0144","type":"666"},{"name":"高中","id":"0322"},{"name":"大学",
//         * "id":"0433"}]
//         */
//
//        private int type;
//        private int resource;
//        private String title;
//        private List<HashMap<String, String>> data;
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public int getResource() {
//            return resource;
//        }
//
//        public void setResource(int resource) {
//            this.resource = resource;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public List<HashMap<String, String>> getData() {
//            return data;
//        }
//
//        public void setData(List<HashMap<String, String>> data) {
//            this.data = data;
//        }
//    }
//}
