//package chinapex.com.wallet.bean;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import chinapex.com.wallet.utils.GsonUtils;
//
///**
// * Created by SteelCabbage on 2018/7/24 0024 9:31.
// * E-Mail：liuyi_61@163.com
// */
//
//public class TestJson {
//    public static void main(String[] args) {
//        System.out.println("哈哈");
//        String json = "{\n" +
//                "    \"code\": 200,\n" +
//                "    \"result\": [\n" +
//                "        {\n" +
//                "            \"type\": 1,\n" +
//                "            \"resource\": 1,\n" +
//                "            \"title\": \"学历\",\n" +
//                "            \"data\": [\n" +
//                "                {\n" +
//                "                    \"name\": \"初中\",\n" +
//                "                    \"id\": \"0144\",\n" +
//                "                    \"type\": \"666\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"高中\",\n" +
//                "                    \"id\": \"0322\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"大学\",\n" +
//                "                    \"id\": \"0433\"\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"type\": 0,\n" +
//                "            \"resource\": 0,\n" +
//                "            \"title\": \"年收入\",\n" +
//                "            \"data\": []\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"type\": 1,\n" +
//                "            \"resource\": 1,\n" +
//                "            \"title\": \"移动运营商\",\n" +
//                "            \"data\": [\n" +
//                "                {\n" +
//                "                    \"name\": \"电信\",\n" +
//                "                    \"id\": 14\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"联通\",\n" +
//                "                    \"id\": 32\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"移动\",\n" +
//                "                    \"id\": 43\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"其他\",\n" +
//                "                    \"id\": 11\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"type\": 4,\n" +
//                "            \"resource\": 1,\n" +
//                "            \"title\": \"用户商业兴趣\",\n" +
//                "            \"data\": [\n" +
//                "                {\n" +
//                "                    \"name\": \"旅游\",\n" +
//                "                    \"id\": 0\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"旅游\",\n" +
//                "                    \"id\": 0\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"旅游\",\n" +
//                "                    \"id\": 0\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"name\": \"旅游\",\n" +
//                "                    \"id\": 0\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//
//
//        Test test = GsonUtils.json2Bean(json, Test.class);
//        if (null == test) {
//            System.out.println("test is null!");
//            return;
//        }
//
//        List<Test.ResultBean> resultBeans = test.getResult();
//        if (null == resultBeans || resultBeans.isEmpty()) {
//            System.out.println("result is null or empty!");
//            return;
//        }
//
//        for (Test.ResultBean resultBean : resultBeans) {
//            if (null == resultBean) {
//                System.out.println("resultBean is null!");
//                continue;
//            }
//
//            List<HashMap<String, String>> data = resultBean.getData();
//            if (null == data || data.isEmpty()) {
//                System.out.println("data is null or empty!");
//                continue;
//            }
//
//            for (HashMap<String, String> hashMap : data) {
//                if (null == hashMap || hashMap.isEmpty()) {
//                    System.out.println("hashMap is null or empty!");
//                    continue;
//                }
//
//                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
//                    if (null == entry) {
//                        System.out.println("entry is null!");
//                        continue;
//                    }
//
//                    System.out.println("key:" + entry.getKey());
//                    System.out.println("value:" + entry.getValue());
//                }
//            }
//
//
//        }
//    }
//}
