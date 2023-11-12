package top.sharehome.usermodule;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.sharehome.usermodule.mapper.TeacherRoleMapper;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
public class UserModuleApplicationTests {
    @Resource
    private TeacherRoleMapper teacherRoleMapper;

    @Test
    public void testSelectBatch() {

    }

    public static void main(String[] args) {
        HashMap<Long, String> hashMap = new HashMap<>();
        hashMap.put(1L, "1");
        hashMap.put(2L, "2");
        String jsonStr = JSONUtil.toJsonStr(hashMap);
        System.out.println(jsonStr);
    }
}
