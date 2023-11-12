package top.sharehome.usermodule;

import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * @author AntonyCheng
 * @since 2023/8/22 00:09:00
 */

public class Demo {
    public static void main(String[] args) {
        HashMap<Long, String> hashMap = new HashMap<>();
        hashMap.put(1L, "1");
        hashMap.put(2L, "2");
        String jsonStr = JSONUtil.toJsonStr(hashMap);
        System.out.println(jsonStr);
    }
}
